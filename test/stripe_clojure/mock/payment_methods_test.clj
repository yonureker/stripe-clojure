(ns stripe-clojure.mock.payment-methods-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.payment-methods :as payment-methods]))

(deftest create-payment-method-test
  (testing "Create a payment method with valid parameters"
    (let [params {:type "card"
                  :card {:number "4242424242424242"
                         :exp_month 12
                         :exp_year 2026
                         :cvc "123"}}
          response (payment-methods/create-payment-method stripe-client params)]
      (is (string? (:id response)) "Payment method should have an id")
      (is (= "payment_method" (:object response))
          "Returned object should be 'payment_method'"))))

(deftest retrieve-payment-method-test
  (testing "Retrieve a payment method by id"
    (let [dummy-id "pm_mock_123"
          response (payment-methods/retrieve-payment-method stripe-client dummy-id)]
      (is (= dummy-id (:id response)) "Retrieved payment method id should match")
      (is (= "payment_method" (:object response))
          "Returned object should be 'payment_method'"))))

(deftest update-payment-method-test
  (testing "Update a payment method"
    (let [dummy-id "pm_mock_123"
          update-params {:metadata {:order "order123"}}
          response (payment-methods/update-payment-method stripe-client dummy-id update-params)]
      (is (= dummy-id (:id response)) "Payment method id should remain unchanged")
      (is (= "payment_method" (:object response))
          "Returned object should be 'payment_method'"))))

(deftest list-payment-methods-test
  (testing "List payment methods with query parameters"
    (let [params {:customer "cus_mock_123"}
          response (payment-methods/list-payment-methods stripe-client params)]
      (is (map? response) "Response should be a map")
      (is (vector? (:data response))
          "Response :data should be a vector of payment methods"))))

(deftest attach-payment-method-test
  (testing "Attach a payment method to a customer"
    (let [dummy-id "pm_mock_123"
          params {:customer "cus_mock_123"}
          response (payment-methods/attach-payment-method stripe-client dummy-id params)]
      (is (= "payment_method" (:object response))
          "Returned object should be 'payment_method'")
      (is (= dummy-id (:id response))
          "Payment method id should remain unchanged"))))

(deftest detach-payment-method-test
  (testing "Detach a payment method from a customer"
    (let [dummy-id "pm_mock_123"
          response (payment-methods/detach-payment-method stripe-client dummy-id)]
      (is (= "payment_method" (:object response))
          "Returned object should be 'payment_method'")
      (is (= dummy-id (:id response))
          "Payment method id should match")))) 