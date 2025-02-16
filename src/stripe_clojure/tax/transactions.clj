(ns stripe-clojure.tax.transactions
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-tax-transactions-endpoint (config/stripe-endpoints :tax-transactions))

(defn retrieve-transaction
  "Retrieves a tax transaction.
   \nStripe API docs: https://stripe.com/docs/api/tax/transactions/retrieve"
  ([stripe-client transaction-id]
   (retrieve-transaction stripe-client transaction-id {}))
  ([stripe-client transaction-id opts]
   (request stripe-client :get
                (str stripe-tax-transactions-endpoint "/" transaction-id)
                {}
                opts)))

(defn create-from-calculation
  "Creates a new tax transaction from a calculation.
   \nStripe API docs: https://stripe.com/docs/api/tax/transactions/create_from_calculation"
  ([stripe-client params]
   (create-from-calculation stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post
                (str stripe-tax-transactions-endpoint "/create_from_calculation")
                params
                opts)))

(defn create-reversal
  "Creates a new tax transaction reversal.
   \nStripe API docs: https://stripe.com/docs/api/tax/transactions/create_reversal"
  ([stripe-client params]
   (create-reversal stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post
                (str stripe-tax-transactions-endpoint "/create_reversal")
                params
                opts)))

(defn list-line-items
  "Lists all line items for a tax transaction.
   \nStripe API docs: https://stripe.com/docs/api/tax/transactions/line_items"
  ([stripe-client transaction-id]
   (list-line-items stripe-client transaction-id {} {}))
  ([stripe-client transaction-id params]
   (list-line-items stripe-client transaction-id params {}))
  ([stripe-client transaction-id params opts]
   (request stripe-client :get
                (str stripe-tax-transactions-endpoint "/" transaction-id "/line_items")
                params
                opts))) 