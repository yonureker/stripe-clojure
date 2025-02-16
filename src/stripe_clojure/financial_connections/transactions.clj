(ns stripe-clojure.financial-connections.transactions
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-financial-connections-transactions-endpoint (config/stripe-endpoints :financial-connections-transactions))

(defn retrieve-transaction
  "Retrieves a financial connections transaction.
   \nStripe API docs: https://stripe.com/docs/api/financial_connections/transaction/retrieve"
  ([stripe-client transaction-id]
   (retrieve-transaction stripe-client transaction-id {}))
  ([stripe-client transaction-id opts]
   (request stripe-client :get
                (str stripe-financial-connections-transactions-endpoint "/" transaction-id)
                {}
                opts)))

(defn list-transactions
  "Lists all financial connections transactions.
   \nStripe API docs: https://stripe.com/docs/api/financial_connections/transactions/list"
  ([stripe-client params]
   (list-transactions stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-financial-connections-transactions-endpoint params opts))) 