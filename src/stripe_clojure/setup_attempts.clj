(ns stripe-clojure.setup-attempts
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-setup-attempts-endpoint (:setup-attempts config/stripe-endpoints))

(defn list-setup-attempts
  "Returns a list of SetupAttempts associated with a SetupIntent.
   \nStripe API docs: https://stripe.com/docs/api/setup_attempts/list"
  ([stripe-client]
   (list-setup-attempts stripe-client {}))
  ([stripe-client params]
   (list-setup-attempts stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-setup-attempts-endpoint params opts)))
