(ns stripe-clojure.issuing.settlements
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-issuing-settlements-endpoint (config/stripe-endpoints :issuing-settlements))

(defn retrieve-settlement
  "Retrieves an issuing settlement.
   \nStripe API docs: https://stripe.com/docs/api/issuing/settlements/retrieve"
  ([stripe-client settlement-id]
   (retrieve-settlement stripe-client settlement-id {}))
  ([stripe-client settlement-id opts]
   (request stripe-client :get
                (str stripe-issuing-settlements-endpoint "/" settlement-id)
                {}
                opts)))

(defn list-settlements
  "Returns a list of issuing settlements.
   \nStripe API docs: https://stripe.com/docs/api/issuing/settlements/list"
  ([stripe-client]
   (list-settlements stripe-client {} {}))
  ([stripe-client params]
   (list-settlements stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-issuing-settlements-endpoint params opts)))