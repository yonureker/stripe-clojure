(ns stripe-clojure.http.request
  (:require [clj-http.client :as http]
            [stripe-clojure.http.throttle :as throttle]
            [stripe-clojure.http.retry :as retry]
            [stripe-clojure.http.pagination :as pagination]
            [stripe-clojure.http.events :as events]
            [stripe-clojure.http.response :as response]
            [stripe-clojure.http.util :as util]
            [stripe-clojure.schemas.request :as schema]
            [malli.core :as m]
            [malli.error :as me]))

(defn send-stripe-api-request
  "Sends an HTTP request to the Stripe API."
  [method url options]
  (when-not (#{:get :post :delete} method)
    (throw (ex-info "Unsupported HTTP method. Must be one of: :get, :post, :delete" 
                    {:method method :supported-methods #{:get :post :delete}})))
  (try
    (case method
      :get    (http/get url options)
      :post   (http/post url options)
      :delete (http/delete url options))
    (catch Exception e
      (or (ex-data e)                    ;; ex-data might have :status
          {:status 500                    ;; ensure we have a status
           :error e
           :error-type "network-error"
           :message (.getMessage e)}))))

(defn- idempotent-method?
  "Checks if the HTTP method is naturally idempotent."
  [method]
  (#{:get :put :delete} method)) 

(defn- construct-base-url
  "Constructs the base URL from config"
  [{:keys [protocol host port]}]
  (str protocol "://" host (when (and port (not= port 443)) (str ":" port))))

(defn generate-idempotency-key []
  (str (java.util.UUID/randomUUID)))

(defn make-request
  "Makes an HTTP request to the Stripe API with retry capability for idempotent requests.
   
   Parameters:
   - method: The HTTP method as a keyword (:get, :post, or :delete)
   - url: The full URL for the API endpoint
   - params: A map of parameters to send with the request
   - opts: A map of additional options
   - config: The effective configuration (from closure)"
  [method url params opts config]
  ;; Always validate request options - prevents invalid API calls
  (when (m/explain schema/RequestOpts opts)
    (let [humanized (me/humanize (m/explain schema/RequestOpts opts))]
      (throw (ex-info (str "Invalid request options: " (pr-str humanized))
                              {:errors humanized
                               :provided-options opts}))))
  
  (let [{:keys [api-key
                api-version
                stripe-account
                connection-manager
                max-network-retries
                timeout
                full-response?
                listeners
                kebabify-keys?]} (merge config opts)
        request-start-time (System/currentTimeMillis)
        base-url (construct-base-url config)
        full-url (str base-url url)
        _ (when-not (re-matches #"^https?://.*" full-url)
            (throw (ex-info "Invalid URL format" {:url full-url :base-url base-url :endpoint url})))
        base-headers {:authorization (str "Bearer " api-key)
                      :content-type "application/x-www-form-urlencoded"
                      :stripe-version api-version}
        ;; Generate idempotency key if needed
        idempotency-key (or (:idempotency-key opts)
                            (when (idempotent-method? method)
                              (generate-idempotency-key)))
        {:keys [expand custom-headers test-clock auto-paginate?]} opts
        all-headers (cond-> base-headers
                      stripe-account (assoc :stripe-account stripe-account)
                      idempotency-key (assoc :idempotency-key idempotency-key)
                      test-clock (assoc :stripe-test-clock test-clock)
                      (seq custom-headers) (merge custom-headers))
        expand-params (util/format-expand expand)
        flattened-params (util/flatten-params params)
        request-params (merge flattened-params expand-params)
        timeout-value (or timeout 80000)
        options (cond-> {:headers all-headers
                         :as :json
                         :socket-timeout timeout-value
                         :conn-timeout timeout-value}
                  connection-manager (assoc :connection-manager connection-manager)
                  (seq request-params) (assoc (if (= method :get)
                                                :query-params
                                                :form-params)
                                              request-params))
        should-paginate? (and (= method :get)
                              auto-paginate?
                              (pagination/is-paginated-endpoint? full-url))
        request-with-retry (retry/with-retry #(send-stripe-api-request method full-url options) max-network-retries)
        ;; throttling specific
        effective-throttler (:throttler config)
        base-event-data {:method method
                         :url url
                         :api-version api-version
                         :request-start-time request-start-time
                         :account stripe-account
                         :idempotency-key idempotency-key}]

    ;; Request event
    (events/emit-event listeners :request base-event-data nil kebabify-keys?)

    (throttle/with-throttling effective-throttler method full-url api-key
      (fn []
        (let [raw-result (request-with-retry)
              request-id (get-in raw-result [:headers "request-id"])
              result (response/process-response raw-result full-response? kebabify-keys?)
              make-request-fn #(response/process-response (request-with-retry) full-response? kebabify-keys?)
              final-result (if should-paginate?
                             (pagination/paginate method full-url params opts make-request-fn)
                             result)
              request-end-time (System/currentTimeMillis)]
          ;; Response event
          (events/emit-event listeners :response base-event-data
                             {:request-end-time request-end-time
                              :elapsed (- request-end-time request-start-time)
                              :status (:status raw-result)
                              :request-id request-id}
                             kebabify-keys?)
          final-result)))))
