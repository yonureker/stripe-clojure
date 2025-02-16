(ns stripe-clojure.treasury.credit-reversals
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-treasury-credit-reversals-endpoint (config/stripe-endpoints :treasury-credit-reversals))

(defn create-credit-reversal
  "Creates a new treasury credit reversal.
   \nStripe API docs: https://stripe.com/docs/api/treasury/credit_reversals/create"
  ([stripe-client params]
   (create-credit-reversal stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-treasury-credit-reversals-endpoint params opts)))

(defn retrieve-credit-reversal
  "Retrieves a treasury credit reversal.
   \nStripe API docs: https://stripe.com/docs/api/treasury/credit_reversals/retrieve"
  ([stripe-client credit-reversal-id]
   (retrieve-credit-reversal stripe-client credit-reversal-id {}))
  ([stripe-client credit-reversal-id opts]
   (request stripe-client :get
                (str stripe-treasury-credit-reversals-endpoint "/" credit-reversal-id)
                {}
                opts)))

(defn list-credit-reversals
  "Lists all treasury credit reversals.
   \nStripe API docs: https://stripe.com/docs/api/treasury/credit_reversals/list"
  ([stripe-client]
   (list-credit-reversals stripe-client {} {}))
  ([stripe-client params]
   (list-credit-reversals stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-treasury-credit-reversals-endpoint params opts))) 