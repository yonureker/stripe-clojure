(ns stripe-clojure.issuing.cardholders
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-issuing-cardholders-endpoint (config/stripe-endpoints :issuing-cardholders))

(defn create-cardholder
  "Creates a new issuing cardholder.
   \nStripe API docs: https://stripe.com/docs/api/issuing/cardholders/create"
  ([stripe-client params]
   (create-cardholder stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-issuing-cardholders-endpoint params opts)))

(defn retrieve-cardholder
  "Retrieves an issuing cardholder.
   \nStripe API docs: https://stripe.com/docs/api/issuing/cardholders/retrieve"
  ([stripe-client cardholder-id]
   (retrieve-cardholder stripe-client cardholder-id {}))
  ([stripe-client cardholder-id opts]
   (request stripe-client :get
                (str stripe-issuing-cardholders-endpoint "/" cardholder-id)
                {}
                opts)))

(defn update-cardholder
  "Updates an issuing cardholder.
   \nStripe API docs: https://stripe.com/docs/api/issuing/cardholders/update"
  ([stripe-client cardholder-id params]
   (update-cardholder stripe-client cardholder-id params {}))
  ([stripe-client cardholder-id params opts]
   (request stripe-client :post
            (str stripe-issuing-cardholders-endpoint "/" cardholder-id)
            params
            opts)))

(defn list-cardholders
  "Lists all issuing cardholders.
   \nStripe API docs: https://stripe.com/docs/api/issuing/cardholders/list"
  ([stripe-client]
   (list-cardholders stripe-client {} {}))
  ([stripe-client params]
   (list-cardholders stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-issuing-cardholders-endpoint params opts))) 