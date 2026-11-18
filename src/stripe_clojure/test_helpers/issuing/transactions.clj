(ns stripe-clojure.test-helpers.issuing.transactions
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-test-helpers-issuing-transactions-endpoint (:test-helpers-issuing-transactions config/stripe-endpoints))

(defn create-force-capture
  "Creates a test mode forced capture for an arbitrary amount.
   \nStripe API docs: https://stripe.com/docs/api/issuing/transactions/test_mode_create_force_capture"
  ([stripe-client params]
   (create-force-capture stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post
            (str stripe-test-helpers-issuing-transactions-endpoint "/create_force_capture")
            params
            opts)))

(defn create-unlinked-refund
  "Creates a test mode unlinked refund for an arbitrary amount.
   \nStripe API docs: https://stripe.com/docs/api/issuing/transactions/test_mode_create_unlinked_refund"
  ([stripe-client params]
   (create-unlinked-refund stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post
            (str stripe-test-helpers-issuing-transactions-endpoint "/create_unlinked_refund")
            params
            opts)))

(defn refund-transaction
  "Refunds a test mode transaction.
   \nStripe API docs: https://stripe.com/docs/api/issuing/transactions/test_mode_refund"
  ([stripe-client transaction-id]
   (refund-transaction stripe-client transaction-id {} {}))
  ([stripe-client transaction-id params]
   (refund-transaction stripe-client transaction-id params {}))
  ([stripe-client transaction-id params opts]
   (request stripe-client :post
            (str stripe-test-helpers-issuing-transactions-endpoint "/" transaction-id "/refund")
            params
            opts)))
