(ns stripe-clojure.mock.payment-method-configurations-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.payment-method-configurations :as payment-configs]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Payment Method Configurations – Valid Cases
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest create-payment-method-configuration-test
  (testing "Create a payment method configuration with valid parameters"
    (let [params {:name "Test Payment Method Configuration"}
          response (payment-configs/create-payment-method-configuration stripe-mock-client params)]
      (is (string? (:id response)) "Payment method configuration should have an id")
      (is (= "payment_method_configuration" (:object response))
          "Returned object should be 'payment_method_configuration'"))))

(deftest retrieve-payment-method-configuration-test
  (testing "Retrieve a payment method configuration by id"
    (let [dummy-id "pmc_mock_123"
          response (payment-configs/retrieve-payment-method-configuration stripe-mock-client dummy-id)]
      (is (= dummy-id (:id response))
          "Retrieved configuration id should match the provided id")
      (is (= "payment_method_configuration" (:object response))
          "Returned object should be 'payment_method_configuration'"))))

(deftest update-payment-method-configuration-test
  (testing "Update a payment method configuration"
    (let [dummy-id "pmc_mock_123"
          update-params {:name "Updated Payment Method Configuration"}
          response (payment-configs/update-payment-method-configuration stripe-mock-client dummy-id update-params)]
      (is (contains? response :id) "Expected response to contain the :id key")
      (is (= "payment_method_configuration" (:object response))
          "Returned object should be 'payment_method_configuration'"))))

(deftest list-payment-method-configurations-test
  (testing "List payment method configurations with query parameters"
    (let [params {:limit 1}
          response (payment-configs/list-payment-method-configurations stripe-mock-client params)]
      (is (map? response) "Response should be a map")
      (is (vector? (:data response))
          "Response :data should be a vector of payment method configurations"))))
