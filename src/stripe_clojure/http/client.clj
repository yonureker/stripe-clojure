(ns stripe-clojure.http.client
  (:require [clj-http.conn-mgr :as conn-mgr]
            [stripe-clojure.http.throttle :as throttle]
            [stripe-clojure.http.request :as request]
            [stripe-clojure.config :as config]))

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
    (-> config
        (dissoc :connection-manager)
        (dissoc :throttler)
        (assoc :api-key (config/mask-api-key (:api-key config)))))
  (execute-request [_ method endpoint params opts]
    ;; Merge the instance defaults (config) with any per-request overrides.
    (let [effective-config (merge config
                                  (select-keys opts
                                               [:api-key
                                                :api-version
                                                :stripe-account
                                                :max-network-retries
                                                :timeout
                                                :full-response?]))]
      ;; Delegate the actual HTTP call to 'make-request'.
      (request/make-request method endpoint params opts effective-config)))
  (shutdown [_]
    (when-let [cm (:connection-manager config)]
      (.shutdown cm))
    nil))

;; ================= EVENT LISTENER =================
(def valid-event-types
  "Set of supported event types"
  #{:request      ; Emitted when a request is made to Stripe's API
    :response})   ; Emitted when a response is received
    
(defn on
  "Add an event listener to the Stripe client."
  [stripe-client event-type handler]
  (when-not (valid-event-types event-type)
    (throw (ex-info "Unsupported event type"
                    {:event-type event-type
                     :valid-types valid-event-types})))
  (swap! (-> stripe-client :config :listeners)
         conj {:type event-type :handler handler}))

(defn off
  "Remove an event listener from the Stripe client."
  [stripe-client event-type handler]
  (when-not (valid-event-types event-type)
    (throw (ex-info "Unsupported event type"
                    {:event-type event-type
                     :valid-types valid-event-types})))
  (swap! (-> stripe-client :config :listeners)
         (fn [listeners]
           (vec (remove #(and (= (:type %) event-type)
                              (= (:handler %) handler))
                        listeners)))))

;; ================= HELPER FUNCTIONS (MOVED UP) =================
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