(ns stripe-clojure.treasury.transactions
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-treasury-transactions-endpoint (config/stripe-endpoints :treasury-transactions))

(defn retrieve-transaction
  "Retrieves a treasury transaction.
   \nStripe API docs: https://stripe.com/docs/api/treasury/transactions/retrieve"
  ([stripe-client transaction-id]
   (retrieve-transaction stripe-client transaction-id {}))
  ([stripe-client transaction-id opts]
   (request stripe-client :get
                (str stripe-treasury-transactions-endpoint "/" transaction-id)
                {}
                opts)))

(defn list-transactions
  "Lists all treasury transactions.
   \nStripe API docs: https://stripe.com/docs/api/treasury/transactions/list"
  ([stripe-client]
   (list-transactions stripe-client {} {}))
  ([stripe-client params]
   (list-transactions stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-treasury-transactions-endpoint params opts))) 