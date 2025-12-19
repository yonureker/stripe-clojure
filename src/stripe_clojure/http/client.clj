(ns stripe-clojure.http.client
  (:require [hato.client :as hato]
            [stripe-clojure.http.throttle :as throttle]
            [stripe-clojure.http.request :as request]
            [stripe-clojure.config :as config]))

;; ================= HELPER FUNCTIONS =================
(defn mask-client-config
  "Returns the client configuration with sensitive data (like the API key) masked."
  [client-config]
  (-> client-config
      (dissoc :http-client :throttler)
      (assoc :api-key (config/mask-api-key (:api-key client-config)))))

(defn merge-client-config
  "Merges per-request options with the instance's configuration."
  [base-config request-opts]
  (merge base-config
         (select-keys request-opts
                      [:api-key
                       :api-version
                       :stripe-account
                       :max-network-retries
                       :timeout
                       :full-response?])))

(defn shutdown-http-client
  "Shuts down the HTTP client if needed."
  [_client-config]
  nil)

(defn- create-http-client
  "Creates a Hato HTTP client with HTTP/2 support."
  [opts]
  (hato/build-http-client
   {:version :http-2
    :connect-timeout (or (:timeout opts) (:timeout config/default-client-config))}))

(defn- prepare-config
  "Merges user-provided options with defaults."
  [opts http-client]
  (let [selected-opts (select-keys opts [:api-key
                                         :api-version
                                         :stripe-account
                                         :max-network-retries
                                         :timeout
                                         :protocol
                                         :host
                                         :port
                                         :full-response?
                                         :rate-limits
                                         :throttler
                                         :kebabify-keys?])
        config (merge config/default-client-config
                      selected-opts
                      {:http-client http-client
                       :listeners (atom [])})]
    config))

;; ================= PROTOCOL & RECORD =================
(defprotocol StripeClient
  "Defines the operations for Stripe API client instances."
  (get-client-config [this]
    "Returns the Stripe configuration with sensitive data masked.")
  (execute-request [this method endpoint params opts]
    "Executes an API request using the instance configuration.")
  (shutdown [this]
    "Shuts down internal resources (e.g., connection pools)."))

(defrecord StripeClientInstance [config]
  StripeClient
  (get-client-config [_]
    (mask-client-config config))
  (execute-request [_ method endpoint params opts]
    (let [effective-config (merge-client-config config opts)]
      (request/make-request method endpoint params opts effective-config)))
  (shutdown [_]
    (shutdown-http-client config)))

(defn- needs-throttling?
  "Determines if throttling is needed based on user-provided rate limits.
   
   Stripe's actual server-side limits are quite low (25-100 req/s), so any
   user-provided rate limits likely indicate a need for client-side throttling."
  [rate-limits]
  (and (some? rate-limits)
       (seq rate-limits)
       true))

(defn create-instance
  "Creates and returns a new Stripe client instance without using any global state.
   
   Options include:
   - :api-key (required),
   - :api-version, :stripe-account, :max-network-retries, :timeout,
   - :protocol (default: \"https\"),
   - :host (default: \"api.stripe.com\"), :port (default: 443),
   - :rate-limits (only creates throttler if limits are restrictive).
   
   Returns a StripeClientInstance record that implements the StripeClient protocol."
  [opts]
  (when-not (:api-key opts)
    (throw (ex-info "API key is required" {:opts opts})))

  (let [throttler (when (needs-throttling? (:rate-limits opts))
                    (throttle/create-throttler (:rate-limits opts)))
        opts-with-throttler (if throttler
                              (assoc opts :throttler throttler)
                              opts)
        http-client (create-http-client opts)
        config (prepare-config opts-with-throttler http-client)]
    (->StripeClientInstance config)))

(defn request
  "Delegates to client/execute-request for backwards compatibility."
  [instance method endpoint params opts]
  (execute-request instance method endpoint params opts))
