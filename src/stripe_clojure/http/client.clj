(ns stripe-clojure.http.client
  (:require [clj-http.conn-mgr :as conn-mgr]
            [stripe-clojure.http.throttle :as throttle]
            [stripe-clojure.http.request :as request]
            [stripe-clojure.config :as config]))

;; ================= HELPER FUNCTIONS =================
(defn mask-client-config
  "Returns the client configuration with sensitive data (like the API key) masked."
  [client-config]
  (-> client-config
      (dissoc :connection-manager :throttler)
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

(defn shutdown-connection-manager
  "Shuts down the client connection manager if it exists."
  [client-config]
  (when-let [cm (:connection-manager client-config)]
    (.shutdown cm))
  nil)

(defn- create-http-client
  "Creates an HTTP client, optionally with a connection pool."
  [use-connection-pool? pool-options]
  (if use-connection-pool?
    (let [cm (conn-mgr/make-reusable-conn-manager
              (merge config/default-connection-pool-options pool-options))]
      {:connection-manager cm})
    {}))

(defn- prepare-config
  "Merges user-provided options with defaults."
  [opts connection-manager]
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
        config    (merge config/default-client-config
                             selected-opts
                             {:connection-manager connection-manager
                              :use-connection-pool? (boolean (:use-connection-pool? opts))
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

(defrecord StripeClientInstance [config request-fn]
  StripeClient
  (get-client-config [_]
    (mask-client-config config))
  (execute-request [_ method endpoint params opts]
    (let [effective-config (merge-client-config config opts)]
      (request/make-request method endpoint params opts effective-config)))
  (shutdown [_]
    (shutdown-connection-manager config)))

(defn create-instance
  "Creates and returns a new Stripe client instance without using any global state.
   
   Options include:
   - :api-key (required),
   - :api-version, :stripe-account, :max-network-retries, :timeout,
   - :protocol (default: \"https\"),
   - :host (default: \"api.stripe.com\"), :port (default: 443),
   - :rate-limits,
   - :use-connection-pool? (default: false),
   - :pool-options (if pooling is enabled).
   
   Returns a StripeClientInstance record that implements the StripeClient protocol."
  [opts]
  (when-not (:api-key opts)
    (throw (ex-info "API key is required" {:opts opts})))

  (let [throttler (throttle/create-throttler (or (:rate-limits opts) {}))
        opts-with-throttler (assoc opts :throttler throttler)
        {:keys [connection-manager]} (create-http-client (:use-connection-pool? opts)
                                                         (:pool-options opts))
        config (prepare-config opts-with-throttler connection-manager)
        client-request-fn (fn [method endpoint params opts effective-config]
                            (request/make-request method endpoint params opts effective-config))]
    (->StripeClientInstance config client-request-fn)))

(defn request
  "Delegates to client/execute-request for backwards compatibility."
  [instance method endpoint params opts]
  (execute-request instance method endpoint params opts))