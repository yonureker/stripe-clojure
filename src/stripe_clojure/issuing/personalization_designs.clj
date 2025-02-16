(ns stripe-clojure.issuing.personalization-designs
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-issuing-personalization-designs-endpoint (config/stripe-endpoints :issuing-personalization-designs))

(defn create-personalization-design
  "Creates a new issuing personalization design.
   \nStripe API docs: https://stripe.com/docs/api/issuing/personalization_designs/create"
  ([stripe-client params]
   (create-personalization-design stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-issuing-personalization-designs-endpoint params opts)))

(defn retrieve-personalization-design
  "Retrieves an issuing personalization design.
   \nStripe API docs: https://stripe.com/docs/api/issuing/personalization_designs/retrieve"
  ([stripe-client design-id]
   (retrieve-personalization-design stripe-client design-id {}))
  ([stripe-client design-id opts]
   (request stripe-client :get
                (str stripe-issuing-personalization-designs-endpoint "/" design-id)
                {}
                opts)))

(defn update-personalization-design
  "Updates an issuing personalization design.
   \nStripe API docs: https://stripe.com/docs/api/issuing/personalization_designs/update"
  ([stripe-client design-id params]
   (update-personalization-design stripe-client design-id params {}))
  ([stripe-client design-id params opts]
   (request stripe-client :post
                (str stripe-issuing-personalization-designs-endpoint "/" design-id)
                params
                opts)))

(defn list-personalization-designs
  "Lists all issuing personalization designs.
   \nStripe API docs: https://stripe.com/docs/api/issuing/personalization_designs/list"
  ([stripe-client]
   (list-personalization-designs stripe-client {} {}))
  ([stripe-client params]
   (list-personalization-designs stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-issuing-personalization-designs-endpoint params opts))) 