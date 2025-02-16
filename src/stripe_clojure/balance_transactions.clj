(ns stripe-clojure.balance-transactions
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-balance-transactions-endpoint (:balance-transactions config/stripe-endpoints))

(defn retrieve-balance-transaction
  "Retrieves the balance transaction with the given ID.
   \nStripe API docs: https://stripe.com/docs/api/balance_transactions/retrieve"
  ([stripe-client transaction-id]
   (retrieve-balance-transaction stripe-client transaction-id {}))
  ([stripe-client transaction-id opts]
   (request stripe-client :get (str stripe-balance-transactions-endpoint "/" transaction-id) {} opts)))

(defn list-balance-transactions
  "Returns a list of balance transactions.
   \nStripe API docs: https://stripe.com/docs/api/balance_transactions/list"
  ([stripe-client]
   (list-balance-transactions stripe-client {}))
  ([stripe-client params]
   (list-balance-transactions stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-balance-transactions-endpoint params opts)))
