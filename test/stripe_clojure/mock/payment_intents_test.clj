(ns stripe-clojure.mock.payment-intents-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.payment-intents :as pi]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Payment Intents – Valid Cases (Error Tests Removed)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest create-payment-intent-test
  (testing "create-payment-intent returns a valid payment intent"
    (let [params {:amount 5000
                  :currency "usd"
                  :payment_method_types ["card"]}
          response (pi/create-payment-intent stripe-mock-client params)]
      (is (string? (:id response)) "Payment intent should have a string id")
      (is (= "payment_intent" (:object response))
          "Returned object should be 'payment_intent'"))))

(deftest retrieve-payment-intent-test
  (testing "retrieve-payment-intent returns the correct payment intent"
    (let [dummy-id "pi_mock_123"
          response (pi/retrieve-payment-intent stripe-mock-client dummy-id)]
      (is (string? (:id response)) "Payment intent should have an id")
      (is (= "payment_intent" (:object response)))
      (is (contains? response :id) "Expected response to contain the :id key"))))

(deftest update-payment-intent-test
  (testing "update-payment-intent successfully updates a payment intent"
    (let [dummy-id "pi_mock_123"
          update-params {:metadata {:order_id "12345"}}
          response (pi/update-payment-intent stripe-mock-client dummy-id update-params)]
      (is (contains? response :id) "Expected response to contain the :id key")
      (is (= "payment_intent" (:object response))
          "Object should be 'payment_intent'"))))

(deftest confirm-payment-intent-test
  (testing "confirm-payment-intent confirms a payment intent"
    (let [dummy-id "pi_mock_123"
          params {:payment_method "pm_card_visa"}
          response (pi/confirm-payment-intent stripe-mock-client dummy-id params)]
      (is (= "payment_intent" (:object response))
          "Object should be 'payment_intent'")
      (is (contains? response :id) "Expected response to contain the :id key"))))

(deftest capture-payment-intent-test
  (testing "capture-payment-intent captures a payment intent"
    (let [dummy-id "pi_mock_123"
          params {:amount_to_capture 5000}
          response (pi/capture-payment-intent stripe-mock-client dummy-id params)]
      (is (= "payment_intent" (:object response))
          "Returned object should be 'payment_intent'")
      (is (contains? response :id) "Expected response to contain the :id key"))))

(deftest cancel-payment-intent-test
  (testing "cancel-payment-intent cancels a payment intent"
    (let [dummy-id "pi_mock_123"
          params {:cancellation_reason "requested_by_customer"}
          response (pi/cancel-payment-intent stripe-mock-client dummy-id params)]
      (is (= "payment_intent" (:object response))
          "Returned object should be 'payment_intent'")
      (is (contains? response :id) "Expected response to contain the :id key"))))

(deftest list-payment-intents-test
  (testing "list-payment-intents returns a list response"
    (let [params {:limit 1}
          response (pi/list-payment-intents stripe-mock-client params)]
      (is (map? response) "Response should be a map")
      (is (vector? (:data response))
          "The :data key should contain a vector of payment intents"))))

(deftest increment-authorization-test
  (testing "increment-authorization successfully increments the authorization amount"
    (let [dummy-id "pi_mock_123"
          params {:amount 1000}
          response (pi/increment-authorization stripe-mock-client dummy-id params)]
      (is (= 1000 (:amount response))))))

(deftest search-payment-intents-test
  (testing "search-payment-intents returns matching payment intents"
    (let [params {:query "status:'requires_payment_method'"}
          response (pi/search-payment-intents stripe-mock-client params)]
      (is (map? response) "Response should be a map")
      (is (vector? (:data response))
          "The :data key should contain a vector of matching payment intents"))))

(deftest verify-microdeposits-test
  (testing "verify-microdeposits verifies microdeposits on a payment intent"
    (let [dummy-id "pi_mock_123"
          params {:amounts [32 45]}
          response (pi/verify-microdeposits stripe-mock-client dummy-id params)]
      (is (= "payment_intent" (:object response))
          "Object should be 'payment_intent'")
      (is (contains? response :id) "Expected response to contain the :id key"))))

(deftest apply-customer-balance-test
  (testing "apply-customer-balance applies customer balance to a payment intent"
    (let [dummy-id "pi_mock_123"
          params {:amount 2000}
          response (pi/apply-customer-balance stripe-mock-client dummy-id params)]
      (is (= "payment_intent" (:object response))
          "Returned object should be 'payment_intent'")
      (is (contains? response :id) "Expected response to contain the :id key"))))
