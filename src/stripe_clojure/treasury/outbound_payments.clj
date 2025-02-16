(ns stripe-clojure.treasury.outbound-payments
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-treasury-outbound-payments-endpoint (config/stripe-endpoints :treasury-outbound-payments))

(defn create-outbound-payment
  "Creates a new treasury outbound payment.
   \nStripe API docs: https://stripe.com/docs/api/treasury/outbound_payments/create"
  ([stripe-client params]
   (create-outbound-payment stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-treasury-outbound-payments-endpoint params opts)))

(defn retrieve-outbound-payment
  "Retrieves a treasury outbound payment.
   \nStripe API docs: https://stripe.com/docs/api/treasury/outbound_payments/retrieve"
  ([stripe-client outbound-payment-id]
   (retrieve-outbound-payment stripe-client outbound-payment-id {}))
  ([stripe-client outbound-payment-id opts]
   (request stripe-client :get
                (str stripe-treasury-outbound-payments-endpoint "/" outbound-payment-id)
                {}
                opts)))

(defn list-outbound-payments
  "Lists all treasury outbound payments.
   \nStripe API docs: https://stripe.com/docs/api/treasury/outbound_payments/list"
  ([stripe-client params]
   (list-outbound-payments stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-treasury-outbound-payments-endpoint params opts)))

(defn cancel-outbound-payment
  "Cancels a treasury outbound payment.
   \nStripe API docs: https://stripe.com/docs/api/treasury/outbound_payments/cancel"
  ([stripe-client outbound-payment-id]
   (cancel-outbound-payment stripe-client outbound-payment-id {}))
  ([stripe-client outbound-payment-id opts]
   (request stripe-client :post
                (str stripe-treasury-outbound-payments-endpoint "/" outbound-payment-id "/cancel")
                {}
                opts))) 