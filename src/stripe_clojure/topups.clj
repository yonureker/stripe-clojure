(ns stripe-clojure.topups
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-topups-endpoint (config/stripe-endpoints :topups))

(defn create-topup
  "Creates a new topup.
   \nStripe API docs: https://stripe.com/docs/api/topups/create"
  ([stripe-client params]
   (create-topup stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-topups-endpoint params opts)))

(defn retrieve-topup
  "Retrieves the details of a topup.
   \nStripe API docs: https://stripe.com/docs/api/topups/retrieve"
  ([stripe-client topup-id]
   (retrieve-topup stripe-client topup-id {}))
  ([stripe-client topup-id opts]
   (request stripe-client :get
            (str stripe-topups-endpoint "/" topup-id)
            {}
            opts)))

(defn update-topup
  "Updates the specified topup.
   \nStripe API docs: https://stripe.com/docs/api/topups/update"
  ([stripe-client topup-id params]
   (update-topup stripe-client topup-id params {}))
  ([stripe-client topup-id params opts]
   (request stripe-client :post
            (str stripe-topups-endpoint "/" topup-id)
            params
            opts)))

(defn list-topups
  "Lists all topups.
   \nStripe API docs: https://stripe.com/docs/api/topups/list"
  ([stripe-client]
   (list-topups stripe-client {}))
  ([stripe-client params]
   (list-topups stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-topups-endpoint params opts)))

(defn cancel-topup
  "Cancels a topup.
   \nStripe API docs: https://stripe.com/docs/api/topups/cancel"
  ([stripe-client topup-id]
   (cancel-topup stripe-client topup-id {}))
  ([stripe-client topup-id opts]
   (request stripe-client :post
            (str stripe-topups-endpoint "/" topup-id "/cancel")
            {}
            opts)))