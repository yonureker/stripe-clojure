(ns stripe-clojure.http.request
  (:require [clj-http.client :as http]
            [stripe-clojure.http.helpers :as helpers]
            [stripe-clojure.http.throttle :as throttle]
            [stripe-clojure.http.retry :as retry]
            [cheshire.core :as json]
            [stripe-clojure.http.pagination :as pagination]))

(defn send-stripe-api-request
  "Sends an HTTP request to the Stripe API."
  [method url options]
  (try
    (case method
      :get    (http/get url options)
      :post   (http/post url options)
      :delete (http/delete url options)
      (throw (ex-info "Unsupported HTTP method" {:method method})))
    (catch Exception e
      (or (ex-data e)                    ;; ex-data might have :status
          {:status 500                    ;; ensure we have a status
           :error e}))))

(defn- idempotent-method?
  "Checks if the HTTP method is naturally idempotent."
  [method]
  (#{:get :put :delete} method)) 

(defn- determine-max-retries
  "Determines the maximum number of retries based on the provided options and request type.
   
   Follows Stripe's retry recommendations:
   - Idempotent methods (GET, PUT, DELETE) can always retry
   - POST requests need an idempotency key for retries
   - Default max retries is 3
   - Can be overridden by max-network-retries option
   - POST without idempotency key never retries (even with max-network-retries)
   
   Parameters:
   - method: HTTP method (:get, :post, :put, :delete)
   - idempotency-key: Key for ensuring POST request idempotency
   - max-network-retries: Optional override for max retries
   - default-max-retries: Default number of retries (usually 3)"
  [method idempotency-key max-network-retries default-max-retries]
  (let [default-retries (or default-max-retries 3)]
    (cond
      ;; POST without idempotency key - never retry
      (and (= method :post)
           (not idempotency-key))
      0

      ;; Idempotent methods or POST with idempotency key
      (or (idempotent-method? method)
          idempotency-key)
      (or max-network-retries default-retries)

      ;; Any other case (shouldn't occur)
      :else 0)))

(defn- construct-base-url
  "Constructs the base URL from config"
  [{:keys [protocol host port]}]
  (str protocol "://" host (when (and port (not= port 443)) (str ":" port))))

(defn make-request
  "Makes an HTTP request to the Stripe API with retry capability for idempotent requests.
   
   Parameters:
   - method: The HTTP method as a keyword (:get, :post, or :delete)
   - url: The full URL for the API endpoint
   - params: A map of parameters to send with the request
   - opts: A map of additional options
   - config: The effective configuration (from closure)"
  [method url params opts config]
  (let [{:keys [api-key
                api-version
                stripe-account
                connection-manager
                max-network-retries
                timeout
                full-response?]} (merge config opts)
        base-url (construct-base-url config)
        full-url (str base-url url)
        base-headers {:authorization (str "Bearer " api-key)
                      :content-type "application/x-www-form-urlencoded"
                      :stripe-version api-version}
        {:keys [idempotency-key expand custom-headers test-clock auto-paginate?]} opts
        all-headers (cond-> base-headers
                      stripe-account (assoc :stripe-account stripe-account)
                      idempotency-key (assoc :idempotency-key idempotency-key)
                      test-clock (assoc :stripe-test-clock test-clock)
                      (seq custom-headers) (merge custom-headers))
        formatted-headers (helpers/format-headers all-headers)
        expand-params (helpers/format-expand expand)
        flattened-params (helpers/flatten-params params)
        request-params (merge flattened-params expand-params)
        timeout-value (or timeout 80000)
        options (cond-> {:headers formatted-headers
                         :as :json
                         :socket-timeout timeout-value
                         :conn-timeout timeout-value}
                  connection-manager (assoc :connection-manager connection-manager)
                  (seq request-params) (assoc (if (= method :get) 
                                                :query-params 
                                                :form-params) 
                                              request-params))
        max-retries (determine-max-retries method 
                                           idempotency-key 
                                           (:max-network-retries opts) 
                                           max-network-retries)
        should-paginate? (and (= method :get)
                              auto-paginate?
                              (pagination/is-paginated-endpoint? full-url))
        request-with-retry (retry/with-retry #(send-stripe-api-request method full-url options) max-retries)
        ;; throttling specific
        effective-throttler (:throttler config)]
    
    (throttle/with-throttling effective-throttler method full-url api-key
      (fn []
        (let [make-request-fn #(process-response (request-with-retry) full-response?)]
          (if should-paginate?
            (pagination/paginate method full-url params opts make-request-fn)
            (make-request-fn)))))))
