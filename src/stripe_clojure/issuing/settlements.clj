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

(defn update-settlement
  "Updates an issuing settlement.
   \nStripe API docs: https://stripe.com/docs/api/issuing/settlements/update"
  ([stripe-client settlement-id params]
   (update-settlement stripe-client settlement-id params {}))
  ([stripe-client settlement-id params opts]
   (request stripe-client :post
                (str stripe-issuing-settlements-endpoint "/" settlement-id)
                params
                opts)))
