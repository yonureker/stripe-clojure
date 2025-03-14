(ns stripe-clojure.core
  (:require [stripe-clojure.http.client :as client]
            [stripe-clojure.config :as config]
            [stripe-clojure.http.events :as events]
            [stripe-clojure.schemas.client :as schema]
            [malli.core :as m]
            [malli.error :as me]))

;; Public API - keeping same docstrings and functionality
(defn init-stripe
  "Initializes a new Stripe client instance.
   
   Options:
   - :api-key : Stripe API key
   - :api-version : Stripe API version
   - :stripe-account : For Connect, the Stripe account to use
   - :max-network-retries : Maximum number of retries for failed network requests
   - :timeout : Request timeout in milliseconds
   - :protocol : The protocol to use for requests (default: https)
   - :host : Host to connect to (default: api.stripe.com)
   - :port : Port to connect to (default: 443)
   - :rate-limits : Custom rate limits to use
   - :use-connection-pool? : Whether to use connection pooling (default: false)
   - :pool-options : Options for connection pooling (if enabled)"
   [opts]
   (cond
     (contains? opts :mock)
     (client/create-instance config/mock-mode)
     
     (contains? opts :api-key)
     (if-let [errors (m/explain schema/StripeClient opts)]
       (let [humanized (me/humanize errors)]
         (throw (ex-info (str "Validation failed when initializing Stripe client: " (pr-str humanized)) 
                         {:errors humanized})))
       (client/create-instance opts))
  
     :else
     (throw (ex-info "API key required for initialization" {:opts opts}))))

(defn shutdown-stripe-client!
  "Shuts down the given Stripe client instance, releasing any internal and pooled resources."
  [stripe-client]
  (client/shutdown stripe-client))

(def get-config
  "Get the current Stripe configuration with sensitive data masked."
  client/get-client-config)

(defn on
  "Add an event listener to the Stripe client."
  [stripe-client event-type handler]
  (events/add-listener stripe-client event-type handler))

(defn off
  "Remove an event listener from the Stripe client."
  [stripe-client event-type handler]
  (events/remove-listener stripe-client event-type handler))
