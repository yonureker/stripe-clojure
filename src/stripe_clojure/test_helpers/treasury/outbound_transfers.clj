(ns stripe-clojure.test-helpers.treasury.outbound-transfers
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-test-helpers-treasury-outbound-transfers-endpoint (:test-helpers-treasury-outbound-transfers config/stripe-endpoints))

(defn update-outbound-transfer
  "Updates a test mode outbound transfer with tracking details.
   \nStripe API docs: https://stripe.com/docs/api/treasury/outbound_transfers/test_mode_update"
  ([stripe-client outbound-transfer-id params]
   (update-outbound-transfer stripe-client outbound-transfer-id params {}))
  ([stripe-client outbound-transfer-id params opts]
   (request stripe-client :post
            (str stripe-test-helpers-treasury-outbound-transfers-endpoint "/" outbound-transfer-id)
            params
            opts)))

(defn fail-outbound-transfer
  "Transitions a test mode outbound transfer to the failed status.
   \nStripe API docs: https://stripe.com/docs/api/treasury/outbound_transfers/test_mode_fail"
  ([stripe-client outbound-transfer-id]
   (fail-outbound-transfer stripe-client outbound-transfer-id {}))
  ([stripe-client outbound-transfer-id opts]
   (request stripe-client :post
            (str stripe-test-helpers-treasury-outbound-transfers-endpoint "/" outbound-transfer-id "/fail")
            {}
            opts)))

(defn post-outbound-transfer
  "Transitions a test mode outbound transfer to the posted status.
   \nStripe API docs: https://stripe.com/docs/api/treasury/outbound_transfers/test_mode_post"
  ([stripe-client outbound-transfer-id]
   (post-outbound-transfer stripe-client outbound-transfer-id {}))
  ([stripe-client outbound-transfer-id opts]
   (request stripe-client :post
            (str stripe-test-helpers-treasury-outbound-transfers-endpoint "/" outbound-transfer-id "/post")
            {}
            opts)))

(defn return-outbound-transfer
  "Transitions a test mode outbound transfer to the returned status.
   \nStripe API docs: https://stripe.com/docs/api/treasury/outbound_transfers/test_mode_return"
  ([stripe-client outbound-transfer-id]
   (return-outbound-transfer stripe-client outbound-transfer-id {} {}))
  ([stripe-client outbound-transfer-id params]
   (return-outbound-transfer stripe-client outbound-transfer-id params {}))
  ([stripe-client outbound-transfer-id params opts]
   (request stripe-client :post
            (str stripe-test-helpers-treasury-outbound-transfers-endpoint "/" outbound-transfer-id "/return")
            params
            opts)))
