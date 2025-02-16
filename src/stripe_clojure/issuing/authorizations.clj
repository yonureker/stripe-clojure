(ns stripe-clojure.issuing.authorizations
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-issuing-authorizations-endpoint (config/stripe-endpoints :issuing-authorizations))

(defn retrieve-authorization
  "Retrieves an issuing authorization.
   \nStripe API docs: https://stripe.com/docs/api/issuing/authorizations/retrieve"
  ([stripe-client authorization-id]
   (retrieve-authorization stripe-client authorization-id {}))
  ([stripe-client authorization-id opts]
   (request stripe-client :get
                (str stripe-issuing-authorizations-endpoint "/" authorization-id)
                {}
                opts)))

(defn update-authorization
  "Updates an issuing authorization.
   \nStripe API docs: https://stripe.com/docs/api/issuing/authorizations/update"
  ([stripe-client authorization-id params]
   (update-authorization stripe-client authorization-id params {}))
  ([stripe-client authorization-id params opts]
   (request stripe-client :post
                (str stripe-issuing-authorizations-endpoint "/" authorization-id)
                params
                opts)))

(defn list-authorizations
  "Lists all issuing authorizations.
   \nStripe API docs: https://stripe.com/docs/api/issuing/authorizations/list"
  ([stripe-client]
   (list-authorizations stripe-client {} {}))
  ([stripe-client params]
   (list-authorizations stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-issuing-authorizations-endpoint params opts)))

(defn approve-authorization
  "Approves an issuing authorization.
   \nStripe API docs: https://stripe.com/docs/api/issuing/authorizations/approve"
  ([stripe-client authorization-id]
   (approve-authorization stripe-client authorization-id {}))
  ([stripe-client authorization-id params]
   (approve-authorization stripe-client authorization-id params {}))
  ([stripe-client authorization-id params opts]
   (request stripe-client :post
            (str stripe-issuing-authorizations-endpoint "/" authorization-id "/approve")
            params
            opts)))

(defn decline-authorization
  "Declines an issuing authorization.
   \nStripe API docs: https://stripe.com/docs/api/issuing/authorizations/decline"
  ([stripe-client authorization-id]
   (decline-authorization stripe-client authorization-id {}))
  ([stripe-client authorization-id params]
   (decline-authorization stripe-client authorization-id params {}))
  ([stripe-client authorization-id params opts]
   (request stripe-client :post
            (str stripe-issuing-authorizations-endpoint "/" authorization-id "/decline")
            params
            opts))) 