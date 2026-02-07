(ns stripe-clojure.http.request
  (:require [hato.client :as hato]
            [stripe-clojure.http.throttle :as throttle]
            [stripe-clojure.http.retry :as retry]
            [stripe-clojure.http.pagination.core :as pagination]
            [stripe-clojure.http.events :as events]
            [stripe-clojure.http.response :as response]
            [stripe-clojure.http.util :as util]
            [stripe-clojure.http.api-version :as api-version]
            [stripe-clojure.http.encoding :as encoding]
            [stripe-clojure.schemas.request :as schema]
            [malli.core :as m]
            [malli.error :as me]))

(def ^:private default-timeout-ms 80000)
(def ^:private ^java.util.regex.Pattern url-pattern (re-pattern "^https?://.*"))
(def ^:private supported-methods #{:get :post :delete})
(def ^:private idempotent-methods #{:get :put :delete :head :options})

(defn send-stripe-api-request [method url options]
  (when-not (supported-methods method)
    (throw (ex-info "Unsupported HTTP method" {:method method})))
  (try
    (case method
      :get    (hato/get url options)
      :post   (hato/post url options)
      :delete (hato/delete url options))
    (catch Exception e
      (or (ex-data e)
          {:status 500 :error e :error-type "network-error" :message (.getMessage e)}))))

(defn generate-idempotency-key ^String []
  (str (java.util.UUID/randomUUID)))

(defn- validate-request-opts! [opts]
  (when-let [explanation (m/explain schema/RequestOpts opts)]
    (throw (ex-info (str "Invalid request options: " (pr-str (me/humanize explanation)))
                    {:errors (me/humanize explanation) :provided-options opts}))))

(defn- validate-url! [^String full-url base-url endpoint]
  (when-not (re-matches url-pattern full-url)
    (throw (ex-info "Invalid URL format" {:url full-url :base-url base-url :endpoint endpoint}))))

(defn- construct-base-url ^String [{:keys [protocol host port]}]
  (if (and port (not= port 443))
    (str protocol "://" host ":" port)
    (str protocol "://" host)))

(defn- build-headers [{:keys [api-key api-version stripe-account idempotency-key
                               test-clock stripe-beta custom-headers content-type]}]
  (cond-> {"authorization" (str "Bearer " api-key)
           "content-type" content-type
           "stripe-version" api-version}
    stripe-account (assoc "stripe-account" stripe-account)
    idempotency-key (assoc "idempotency-key" idempotency-key)
    test-clock (assoc "stripe-test-clock" test-clock)
    stripe-beta (assoc "stripe-beta" stripe-beta)
    (seq custom-headers) (merge custom-headers)))

(defn- build-http-options [{:keys [headers timeout http-client method params query-params is-v2 multipart]}]
  (let [is-get (identical? method :get)
        base {:headers headers :as :json :timeout timeout :http-client http-client}]
    (if multipart
      (-> base
          (assoc :multipart multipart)
          (update :headers dissoc "content-type"))
      (if is-v2
        (cond-> base
          (and (seq params) is-get) (assoc :query-params params)
          (and (seq params) (not is-get)) (assoc :body params)
          (and (seq query-params) (not is-get)) (assoc :query-params query-params))
        (cond-> base
          (seq params) (assoc (if is-get :query-params :form-params) params))))))

(defn- encode-request-params [{:keys [params expand-params is-v2 is-get detected-version]}]
  (if (and is-v2 (not is-get))
    (encoding/encode-params detected-version params)
    (let [flattened (util/flatten-params params)]
      (if (seq expand-params) (merge flattened expand-params) flattened))))

(defn- prepare-request-context [method url params opts config]
  (let [merged (merge config opts)
        base-url (or (:base-url opts) (:base-url config) (construct-base-url config))
        full-url (str base-url url)
        multipart (:multipart opts)
        _ (validate-url! full-url base-url url)
        detected-version (api-version/detect-version url)
        is-v2 (identical? detected-version api-version/V2)
        is-get (identical? method :get)
        expansion-fields (encoding/get-expansion-fields detected-version opts)
        expand-params (encoding/format-expansion detected-version expansion-fields)
        encoded-params (encode-request-params {:params params
                                               :expand-params expand-params
                                               :is-v2 is-v2
                                               :is-get is-get
                                               :detected-version detected-version})
        v2-post-query-params (when (and is-v2 (not is-get) (seq expand-params)) expand-params)
        headers (build-headers {:api-key (:api-key merged)
                                :api-version (:api-version merged)
                                :stripe-account (:stripe-account merged)
                                :idempotency-key (or (:idempotency-key opts)
                                                     (when (idempotent-methods method)
                                                       (generate-idempotency-key)))
                                :test-clock (:test-clock opts)
                                :stripe-beta (:stripe-beta opts)
                                :custom-headers (:custom-headers opts)
                                :content-type (encoding/content-type detected-version)})
        timeout (or (:timeout merged) default-timeout-ms)
        options (build-http-options {:headers headers
                                     :timeout timeout
                                     :http-client (:http-client merged)
                                     :method method
                                     :params encoded-params
                                     :query-params v2-post-query-params
                                     :is-v2 is-v2
                                     :multipart multipart})]
    {:full-url full-url
     :options options
     :headers headers
     :timeout timeout
     :is-v2 is-v2
     :is-get is-get
     :detected-version detected-version
     :expand-params expand-params
     :merged merged}))

(defn- make-page-request-fn [{:keys [headers timeout http-client method is-v2 is-get
                                      detected-version expand-params full-url max-retries
                                      full-response? kebabify-keys?]}]
  (fn [page-params]
    (let [page-encoded (encode-request-params {:params page-params
                                               :expand-params expand-params
                                               :is-v2 is-v2
                                               :is-get is-get
                                               :detected-version detected-version})
          page-query (when (and is-v2 (not is-get) (seq expand-params)) expand-params)
          page-opts (build-http-options {:headers headers
                                         :timeout timeout
                                         :http-client http-client
                                         :method method
                                         :params page-encoded
                                         :query-params page-query
                                         :is-v2 is-v2})
          req-fn (retry/with-retry #(send-stripe-api-request method full-url page-opts) max-retries)]
      (response/process-response (req-fn) full-response? kebabify-keys?))))

(defn make-request
  "Makes an HTTP request to the Stripe API."
  [method url params opts config]
  (validate-request-opts! opts)
  (let [{:keys [full-url options headers timeout is-v2 is-get
                detected-version expand-params merged]} (prepare-request-context method url params opts config)
        {:keys [max-network-retries full-response? listeners kebabify-keys? throttler http-client]} merged
        request-fn (retry/with-retry #(send-stripe-api-request method full-url options) max-network-retries)
        should-paginate? (and is-get (:auto-paginate? opts) (pagination/is-paginated-endpoint? full-url))
        start-time (System/currentTimeMillis)]

    (when (and listeners (seq @listeners))
      (events/emit-event listeners :request
                         {:method method :url url :api-version (:api-version merged)
                          :request-start-time start-time :account (:stripe-account merged)}
                         nil kebabify-keys?))

    (throttle/with-throttling throttler method full-url (:api-key merged)
      (fn []
        (let [raw-result (request-fn)
              result (response/process-response raw-result full-response? kebabify-keys?)
              final-result (if should-paginate?
                             (pagination/paginate method full-url params opts
                               (make-page-request-fn {:headers headers
                                                      :timeout timeout
                                                      :http-client http-client
                                                      :method method
                                                      :is-v2 is-v2
                                                      :is-get is-get
                                                      :detected-version detected-version
                                                      :expand-params expand-params
                                                      :full-url full-url
                                                      :max-retries max-network-retries
                                                      :full-response? full-response?
                                                      :kebabify-keys? kebabify-keys?}))
                             result)]
          (when (and listeners (seq @listeners))
            (events/emit-event listeners :response
                               {:method method :url url :api-version (:api-version merged)
                                :request-start-time start-time :account (:stripe-account merged)}
                               {:request-end-time (System/currentTimeMillis)
                                :elapsed (- (System/currentTimeMillis) start-time)
                                :status (:status raw-result)
                                :request-id (get-in raw-result [:headers "request-id"])}
                               kebabify-keys?))
          final-result)))))
