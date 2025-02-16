(ns stripe-clojure.issuing.cards
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-issuing-cards-endpoint (config/stripe-endpoints :issuing-cards))

(defn create-card
  "Creates a new issuing card.
   \nStripe API docs: https://stripe.com/docs/api/issuing/cards/create"
  ([stripe-client params]
   (create-card stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-issuing-cards-endpoint params opts)))

(defn retrieve-card
  "Retrieves an issuing card.
   \nStripe API docs: https://stripe.com/docs/api/issuing/cards/retrieve"
  ([stripe-client card-id]
   (retrieve-card stripe-client card-id {}))
  ([stripe-client card-id opts]
   (request stripe-client :get
                (str stripe-issuing-cards-endpoint "/" card-id)
                {}
                opts)))

(defn update-card
  "Updates an issuing card.
   \nStripe API docs: https://stripe.com/docs/api/issuing/cards/update"
  ([stripe-client card-id params]
   (update-card stripe-client card-id params {}))
  ([stripe-client card-id params opts]
   (request stripe-client :post
                (str stripe-issuing-cards-endpoint "/" card-id)
                params
                opts)))

(defn list-cards
  "Lists all issuing cards.
   \nStripe API docs: https://stripe.com/docs/api/issuing/cards/list"
  ([stripe-client]
   (list-cards stripe-client {} {}))
  ([stripe-client params]
   (list-cards stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-issuing-cards-endpoint params opts))) 