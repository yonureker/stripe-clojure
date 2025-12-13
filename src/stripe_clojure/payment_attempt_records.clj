(ns stripe-clojure.payment-attempt-records
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-payment-attempt-records-endpoint (config/stripe-endpoints :payment-attempt-records))

(defn list-payment-attempt-records
  "Lists all payment attempt records attached to the specified payment record.
   \nStripe API docs: https://docs.stripe.com/api/payment-attempt-record/list"
  ([stripe-client params]
   (list-payment-attempt-records stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-payment-attempt-records-endpoint params opts)))

(defn retrieve-payment-attempt-record
  "Retrieves a payment attempt record with the given ID.
   \nStripe API docs: https://docs.stripe.com/api/payment-attempt-record/retrieve"
  ([stripe-client payment-attempt-record-id]
   (retrieve-payment-attempt-record stripe-client payment-attempt-record-id {}))
  ([stripe-client payment-attempt-record-id opts]
   (request stripe-client :get
            (str stripe-payment-attempt-records-endpoint "/" payment-attempt-record-id)
            {}
            opts)))
