(ns stripe-clojure.test-helpers.treasury.inbound-transfers
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-test-helpers-treasury-inbound-transfers-endpoint (:test-helpers-treasury-inbound-transfers config/stripe-endpoints))

(defn fail-inbound-transfer
  "Transitions a test mode inbound transfer to the failed status.
   \nStripe API docs: https://stripe.com/docs/api/treasury/inbound_transfers/test_mode_fail"
  ([stripe-client inbound-transfer-id]
   (fail-inbound-transfer stripe-client inbound-transfer-id {} {}))
  ([stripe-client inbound-transfer-id params]
   (fail-inbound-transfer stripe-client inbound-transfer-id params {}))
  ([stripe-client inbound-transfer-id params opts]
   (request stripe-client :post
            (str stripe-test-helpers-treasury-inbound-transfers-endpoint "/" inbound-transfer-id "/fail")
            params
            opts)))

(defn return-inbound-transfer
  "Marks a test mode inbound transfer as returned.
   \nStripe API docs: https://stripe.com/docs/api/treasury/inbound_transfers/test_mode_return"
  ([stripe-client inbound-transfer-id]
   (return-inbound-transfer stripe-client inbound-transfer-id {}))
  ([stripe-client inbound-transfer-id opts]
   (request stripe-client :post
            (str stripe-test-helpers-treasury-inbound-transfers-endpoint "/" inbound-transfer-id "/return")
            {}
            opts)))

(defn succeed-inbound-transfer
  "Transitions a test mode inbound transfer to the succeeded status.
   \nStripe API docs: https://stripe.com/docs/api/treasury/inbound_transfers/test_mode_succeed"
  ([stripe-client inbound-transfer-id]
   (succeed-inbound-transfer stripe-client inbound-transfer-id {}))
  ([stripe-client inbound-transfer-id opts]
   (request stripe-client :post
            (str stripe-test-helpers-treasury-inbound-transfers-endpoint "/" inbound-transfer-id "/succeed")
            {}
            opts)))
