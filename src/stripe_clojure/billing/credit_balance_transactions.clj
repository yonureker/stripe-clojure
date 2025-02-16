(ns stripe-clojure.billing.credit-balance-transactions
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-billing-credit-balance-transactions-endpoint (config/stripe-endpoints :billing-credit-balance-transactions))

(defn list-credit-balance-transactions
  "Lists all credit balance transactions.
   \nStripe API docs: https://stripe.com/docs/api/billing/credit-balance-transaction/list"
  ([stripe-client]
   (list-credit-balance-transactions stripe-client {} {}))
  ([stripe-client params]
   (list-credit-balance-transactions stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-billing-credit-balance-transactions-endpoint params opts)))

(defn retrieve-credit-balance-transaction
  "Retrieves a credit balance transaction.
   \nStripe API docs: https://stripe.com/docs/api/billing/credit-balance-transaction/retrieve"
  ([stripe-client transaction-id]
   (retrieve-credit-balance-transaction stripe-client transaction-id {}))
  ([stripe-client transaction-id opts]
   (request stripe-client :get
            (str stripe-billing-credit-balance-transactions-endpoint "/" transaction-id)
            {}
            opts))) 