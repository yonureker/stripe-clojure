(ns stripe-clojure.issuing.transactions
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-issuing-transactions-endpoint (config/stripe-endpoints :issuing-transactions))

(defn retrieve-transaction
  "Retrieves an issuing transaction.
   \nStripe API docs: https://stripe.com/docs/api/issuing/transactions/retrieve"
  ([stripe-client transaction-id]
   (retrieve-transaction stripe-client transaction-id {}))
  ([stripe-client transaction-id opts]
   (request stripe-client :get
                (str stripe-issuing-transactions-endpoint "/" transaction-id)
                {}
                opts)))

(defn update-transaction
  "Updates an issuing transaction.
   \nStripe API docs: https://stripe.com/docs/api/issuing/transactions/update"
  ([stripe-client transaction-id params]
   (update-transaction stripe-client transaction-id params {}))
  ([stripe-client transaction-id params opts]
   (request stripe-client :post
                (str stripe-issuing-transactions-endpoint "/" transaction-id)
                params
                opts)))

(defn list-transactions
  "Lists all issuing transactions.
   \nStripe API docs: https://stripe.com/docs/api/issuing/transactions/list"
  ([stripe-client]
   (list-transactions stripe-client {} {}))
  ([stripe-client params]
   (list-transactions stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-issuing-transactions-endpoint params opts))) 