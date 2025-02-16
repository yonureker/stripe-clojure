(ns stripe-clojure.treasury.debit-reversals
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-treasury-debit-reversals-endpoint (config/stripe-endpoints :treasury-debit-reversals))

(defn create-debit-reversal
  "Creates a new treasury debit reversal.
   \nStripe API docs: https://stripe.com/docs/api/treasury/debit_reversals/create"
  ([stripe-client params]
   (create-debit-reversal stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-treasury-debit-reversals-endpoint params opts)))

(defn retrieve-debit-reversal
  "Retrieves a treasury debit reversal.
   \nStripe API docs: https://stripe.com/docs/api/treasury/debit_reversals/retrieve"
  ([stripe-client debit-reversal-id]
   (retrieve-debit-reversal stripe-client debit-reversal-id {}))
  ([stripe-client debit-reversal-id opts]
   (request stripe-client :get
                (str stripe-treasury-debit-reversals-endpoint "/" debit-reversal-id)
                {}
                opts)))

(defn list-debit-reversals
  "Lists all treasury debit reversals.
   \nStripe API docs: https://stripe.com/docs/api/treasury/debit_reversals/list"
  ([stripe-client]
   (list-debit-reversals stripe-client {} {}))
  ([stripe-client params]
   (list-debit-reversals stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-treasury-debit-reversals-endpoint params opts))) 