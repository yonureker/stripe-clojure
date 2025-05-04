(ns stripe-clojure.invoice-payments
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-invoice-payments-endpoint (:invoice-payments config/stripe-endpoints))

(defn retrieve-invoice-payment
  "Retrieves an invoice payment.
   \nStripe API docs: https://docs.stripe.com/api/invoice-payment/retrieve"
  ([stripe-client invoice-payment-id]
   (retrieve-invoice-payment stripe-client invoice-payment-id {}))
  ([stripe-client invoice-payment-id opts]
   (request stripe-client :get (str stripe-invoice-payments-endpoint "/" invoice-payment-id) {} opts)))

(defn list-invoice-payments
  "List all invoice payments.
   \nStripe API docs: https://docs.stripe.com/api/invoice-payment/list"
  ([stripe-client]
   (list-invoice-payments stripe-client {}))
  ([stripe-client opts]
   (request stripe-client :get stripe-invoice-payments-endpoint {} opts)))