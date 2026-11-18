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

(defn send-stripe-api-request [method url options]
  (when-not (supported-methods method)
    (throw (ex-info "Unsupported HTTP method" {:method method})))
  (try
    (case method
      :get    (hato/get url options)
      :post   (hato/post url options)
      :delete (hato/delete url options))
    (catch Exception e
      (let [data (ex-data e)]
        (if (:status data)
          data
          {:status 500
           :headers {}
           :body {:error {:message (.getMessage e)
                          :type "network_error"}}})))))

(defn generate-idempotency-key ^String []
  (str (java.util.UUID/randomUUID)))

(def ^:private valid-request-opts? (m/validator schema/RequestOpts))

(defn- validate-request-opts! [opts]
  (when-not (valid-request-opts? opts)
    (let [humanized (me/humanize (m/explain schema/RequestOpts opts))]
      (throw (ex-info (str "Invalid request options: " (pr-str humanized))
                      {:errors humanized :provided-options opts})))))

(defn- validate-url! [^String full-url base-url endpoint]
  (when-not (re-matches url-pattern full-url)
    (throw (ex-info "Invalid URL format" {:url full-url :base-url base-url :endpoint endpoint}))))

(defn- construct-base-url ^String [{:keys [protocol host port]}]
  (if (and port (not= port 443))
    (str protocol "://" host ":" port)
    (str protocol "://" host)))

(defn- resolve-page-url
  "Stripe returns v2 `next_page_url` as a path relative to the API host."
  [base-url page-url]
  (if (re-matches url-pattern page-url)
    page-url
    (str base-url page-url)))

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
  (let [is-get (= method :get)
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
        is-v2 (= detected-version api-version/V2)
        is-get (= method :get)
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
                                                     (when (= method :post)
                                                       (generate-idempotency-key)))
                                :test-clock (:test-clock opts)
                                :stripe-beta (:stripe-beta opts)
                                :custom-headers (:custom-headers opts)
                                :content-type (encoding/content-type detected-version)})
        timeout (or (:timeout merged) default-timeout-ms)
        response-format (:as opts)
        ;; Map literals of up to eight keys are far cheaper to build, so :as
        ;; is set afterwards and the context below stays at eight keys.
        options (cond-> (build-http-options {:headers headers
                                             :timeout timeout
                                             :http-client (:http-client merged)
                                             :method method
                                             :params encoded-params
                                             :query-params v2-post-query-params
                                             :is-v2 is-v2
                                             :multipart multipart})
                  response-format (assoc :as response-format))]
    {:method method
     :url url
     :base-url base-url
     :full-url full-url
     :options options
     :detected-version detected-version
     :expand-params expand-params
     :merged merged}))

(defn- base-event
  "The fields shared by the request and response events of one request.
   The account and idempotency key are present only when they are sent."
  [method url {:keys [api-version stripe-account]} headers start-time]
  (let [idempotency-key (get headers "idempotency-key")]
    (cond-> {:method method
             :url url
             :api-version api-version
             :request-start-time start-time}
      stripe-account (assoc :account stripe-account)
      idempotency-key (assoc :idempotency-key idempotency-key))))

(defn- send-with-events
  "Calls `send-throttled`, emitting a request event before and a response
   event after."
  [send-throttled {:keys [method url options merged]}]
  (let [{:keys [listeners kebabify-keys?]} merged
        start-time (System/currentTimeMillis)
        event (base-event method url merged (:headers options) start-time)]
    (events/emit-event listeners :request event nil kebabify-keys?)
    (let [response (send-throttled)
          end-time (System/currentTimeMillis)]
      (events/emit-event listeners :response event
                         {:request-end-time end-time
                          :elapsed (- end-time start-time)
                          :status (:status response)
                          :request-id (get-in response [:headers "request-id"])}
                         kebabify-keys?)
      response)))

(defn- send-request
  "Sends one request the way the client is configured: throttled, retried on
   failure and reported to the client's listeners. Returns the raw HTTP
   response."
  [{:keys [method merged] :as context} request-url http-options]
  (let [{:keys [api-key listeners throttler max-network-retries]} merged
        send-once #(send-stripe-api-request method request-url http-options)
        send-with-retry (retry/with-retry send-once max-network-retries)
        send-throttled #(throttle/with-throttling throttler method request-url api-key send-with-retry)]
    ;; Event data is only built when someone is listening.
    (if (and listeners (seq @listeners))
      (send-with-events send-throttled context)
      (send-throttled))))

(defn- make-page-request-fn
  "Returns a function that fetches the page the paginator's params point at.
   Each page is sent like the first request: throttled, retried and reported
   to listeners."
  [{:keys [method detected-version expand-params base-url full-url options merged]
    :as context}]
  (let [{:keys [headers timeout http-client]} options
        is-v2 (= detected-version api-version/V2)
        is-get (= method :get)]
    (fn [page-params]
      (let [next-url (:_next-page-url page-params)
            clean-params (dissoc page-params :_next-page-url)
            request-url (if next-url (resolve-page-url base-url next-url) full-url)
            ;; When using next-page-url, don't re-add expand params (already in URL)
            page-expand (when-not next-url expand-params)
            page-encoded (encode-request-params {:params clean-params
                                                 :expand-params page-expand
                                                 :is-v2 is-v2
                                                 :is-get is-get
                                                 :detected-version detected-version})
            page-query (when (and is-v2 (not is-get) (seq page-expand)) page-expand)
            page-opts (build-http-options {:headers headers
                                           :timeout timeout
                                           :http-client http-client
                                           :method method
                                           :params page-encoded
                                           :query-params page-query
                                           :is-v2 is-v2})]
        (-> (send-request context request-url page-opts)
            (response/process-response false (:kebabify-keys? merged)))))))

(defn make-request
  "Makes an HTTP request to the Stripe API."
  [method url params opts config]
  (let [opts (or opts {})
        _ (validate-request-opts! opts)
        {:keys [full-url options merged] :as context}
        (prepare-request-context method url params opts config)
        paginate? (and (= method :get) (:auto-paginate? opts))
        ;; Auto-pagination yields the items of every page, so there is no
        ;; single response envelope to return.
        full-response? (and (:full-response? merged) (not paginate?))
        first-page (-> (send-request context full-url options)
                       (response/process-response full-response? (:kebabify-keys? merged)))]
    (if paginate?
      (pagination/paginate method url params opts (make-page-request-fn context) first-page)
      first-page)))
