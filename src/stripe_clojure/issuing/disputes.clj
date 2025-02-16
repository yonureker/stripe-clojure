(ns stripe-clojure.issuing.disputes
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-issuing-disputes-endpoint (config/stripe-endpoints :issuing-disputes))

(defn create-dispute
  "Creates a new issuing dispute.
   \nStripe API docs: https://stripe.com/docs/api/issuing/disputes/create"
  ([stripe-client params]
   (create-dispute stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-issuing-disputes-endpoint params opts)))

(defn retrieve-dispute
  "Retrieves an issuing dispute.
   \nStripe API docs: https://stripe.com/docs/api/issuing/disputes/retrieve"
  ([stripe-client dispute-id]
   (retrieve-dispute stripe-client dispute-id {}))
  ([stripe-client dispute-id opts]
   (request stripe-client :get
                (str stripe-issuing-disputes-endpoint "/" dispute-id)
                {}
                opts)))

(defn update-dispute
  "Updates an issuing dispute.
   \nStripe API docs: https://stripe.com/docs/api/issuing/disputes/update"
  ([stripe-client dispute-id params]
   (update-dispute stripe-client dispute-id params {}))
  ([stripe-client dispute-id params opts]
   (request stripe-client :post
                (str stripe-issuing-disputes-endpoint "/" dispute-id)
                params
                opts)))

(defn list-disputes
  "Lists all issuing disputes.
   \nStripe API docs: https://stripe.com/docs/api/issuing/disputes/list"
  ([stripe-client]
   (list-disputes stripe-client {} {}))
  ([stripe-client params]
   (list-disputes stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-issuing-disputes-endpoint params opts)))

(defn submit-dispute
  "Submits an issuing dispute.
   \nStripe API docs: https://stripe.com/docs/api/issuing/dispute/submit"
  ([stripe-client dispute-id]
   (submit-dispute stripe-client dispute-id {}))
  ([stripe-client dispute-id params]
   (submit-dispute stripe-client dispute-id params {}))
   ([stripe-client dispute-id params opts]
    (request stripe-client :post
             (str stripe-issuing-disputes-endpoint "/" dispute-id "/submit")
             params
             opts))) 