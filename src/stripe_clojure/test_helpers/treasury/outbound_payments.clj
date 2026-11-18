(ns stripe-clojure.test-helpers.treasury.outbound-payments
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-test-helpers-treasury-outbound-payments-endpoint (:test-helpers-treasury-outbound-payments config/stripe-endpoints))

(defn update-outbound-payment
  "Updates a test mode outbound payment with tracking details.
   \nStripe API docs: https://stripe.com/docs/api/treasury/outbound_payments/test_mode_update"
  ([stripe-client outbound-payment-id params]
   (update-outbound-payment stripe-client outbound-payment-id params {}))
  ([stripe-client outbound-payment-id params opts]
   (request stripe-client :post
            (str stripe-test-helpers-treasury-outbound-payments-endpoint "/" outbound-payment-id)
            params
            opts)))

(defn fail-outbound-payment
  "Transitions a test mode outbound payment to the failed status.
   \nStripe API docs: https://stripe.com/docs/api/treasury/outbound_payments/test_mode_fail"
  ([stripe-client outbound-payment-id]
   (fail-outbound-payment stripe-client outbound-payment-id {}))
  ([stripe-client outbound-payment-id opts]
   (request stripe-client :post
            (str stripe-test-helpers-treasury-outbound-payments-endpoint "/" outbound-payment-id "/fail")
            {}
            opts)))

(defn post-outbound-payment
  "Transitions a test mode outbound payment to the posted status.
   \nStripe API docs: https://stripe.com/docs/api/treasury/outbound_payments/test_mode_post"
  ([stripe-client outbound-payment-id]
   (post-outbound-payment stripe-client outbound-payment-id {}))
  ([stripe-client outbound-payment-id opts]
   (request stripe-client :post
            (str stripe-test-helpers-treasury-outbound-payments-endpoint "/" outbound-payment-id "/post")
            {}
            opts)))

(defn return-outbound-payment
  "Transitions a test mode outbound payment to the returned status.
   \nStripe API docs: https://stripe.com/docs/api/treasury/outbound_payments/test_mode_return"
  ([stripe-client outbound-payment-id]
   (return-outbound-payment stripe-client outbound-payment-id {} {}))
  ([stripe-client outbound-payment-id params]
   (return-outbound-payment stripe-client outbound-payment-id params {}))
  ([stripe-client outbound-payment-id params opts]
   (request stripe-client :post
            (str stripe-test-helpers-treasury-outbound-payments-endpoint "/" outbound-payment-id "/return")
            params
            opts)))
