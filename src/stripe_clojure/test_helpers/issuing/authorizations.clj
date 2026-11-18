(ns stripe-clojure.test-helpers.issuing.authorizations
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-test-helpers-issuing-authorizations-endpoint (:test-helpers-issuing-authorizations config/stripe-endpoints))

(defn create-authorization
  "Creates a test mode authorization.
   \nStripe API docs: https://stripe.com/docs/api/issuing/authorizations/test_mode_create"
  ([stripe-client params]
   (create-authorization stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-test-helpers-issuing-authorizations-endpoint params opts)))

(defn capture-authorization
  "Captures a test mode authorization.
   \nStripe API docs: https://stripe.com/docs/api/issuing/authorizations/test_mode_capture"
  ([stripe-client authorization-id]
   (capture-authorization stripe-client authorization-id {} {}))
  ([stripe-client authorization-id params]
   (capture-authorization stripe-client authorization-id params {}))
  ([stripe-client authorization-id params opts]
   (request stripe-client :post
            (str stripe-test-helpers-issuing-authorizations-endpoint "/" authorization-id "/capture")
            params
            opts)))

(defn expire-authorization
  "Expires a test mode authorization.
   \nStripe API docs: https://stripe.com/docs/api/issuing/authorizations/test_mode_expire"
  ([stripe-client authorization-id]
   (expire-authorization stripe-client authorization-id {}))
  ([stripe-client authorization-id opts]
   (request stripe-client :post
            (str stripe-test-helpers-issuing-authorizations-endpoint "/" authorization-id "/expire")
            {}
            opts)))

(defn finalize-amount
  "Finalizes the amount on an authorization prior to capture.
   \nStripe API docs: https://stripe.com/docs/api/issuing/authorizations/test_mode_finalize_amount"
  ([stripe-client authorization-id params]
   (finalize-amount stripe-client authorization-id params {}))
  ([stripe-client authorization-id params opts]
   (request stripe-client :post
            (str stripe-test-helpers-issuing-authorizations-endpoint "/" authorization-id "/finalize_amount")
            params
            opts)))

(defn respond-to-fraud-challenge
  "Responds to a fraud challenge on a test mode authorization.
   \nStripe API docs: https://stripe.com/docs/api/issuing/authorizations/respond_to_fraud_challenges"
  ([stripe-client authorization-id params]
   (respond-to-fraud-challenge stripe-client authorization-id params {}))
  ([stripe-client authorization-id params opts]
   (request stripe-client :post
            (str stripe-test-helpers-issuing-authorizations-endpoint "/" authorization-id "/fraud_challenges/respond")
            params
            opts)))

(defn increment-authorization
  "Increments a test mode authorization.
   \nStripe API docs: https://stripe.com/docs/api/issuing/authorizations/test_mode_increment"
  ([stripe-client authorization-id params]
   (increment-authorization stripe-client authorization-id params {}))
  ([stripe-client authorization-id params opts]
   (request stripe-client :post
            (str stripe-test-helpers-issuing-authorizations-endpoint "/" authorization-id "/increment")
            params
            opts)))

(defn reverse-authorization
  "Reverses a test mode authorization.
   \nStripe API docs: https://stripe.com/docs/api/issuing/authorizations/test_mode_reverse"
  ([stripe-client authorization-id]
   (reverse-authorization stripe-client authorization-id {} {}))
  ([stripe-client authorization-id params]
   (reverse-authorization stripe-client authorization-id params {}))
  ([stripe-client authorization-id params opts]
   (request stripe-client :post
            (str stripe-test-helpers-issuing-authorizations-endpoint "/" authorization-id "/reverse")
            params
            opts)))
