(ns stripe-clojure.mock.payment-method-domains-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.payment-method-domains :as domains]))



(deftest create-payment-method-domain-test
  (testing "create-payment-method-domain returns a valid domain"
    (let [params {:domain_name "example.com"}
          response (domains/create-payment-method-domain stripe-mock-client params)]
      (is (string? (:id response)) "Payment method domain should have an id")
      (is (= "payment_method_domain" (:object response))
          "Returned object should be 'payment_method_domain'"))))

(deftest retrieve-payment-method-domain-test
  (testing "retrieve-payment-method-domain returns the correct domain"
    (let [dummy-id "pmd_mock_123"
          response (domains/retrieve-payment-method-domain stripe-mock-client dummy-id)]
      (is (= dummy-id (:id response)) "Retrieved domain id should match the passed id")
      (is (= "payment_method_domain" (:object response))
          "Returned object should be 'payment_method_domain'"))))

(deftest update-payment-method-domain-test
  (testing "update-payment-method-domain returns a valid response"
    (let [dummy-id "pmd_mock_123"
          params {:enabled false}
          response (domains/update-payment-method-domain stripe-mock-client dummy-id params)]
      (is (= dummy-id (:id response)) "Domain id should remain unchanged")
      (is (= "payment_method_domain" (:object response))
          "Returned object should be 'payment_method_domain'"))))

(deftest list-payment-method-domains-test
  (testing "list-payment-method-domains returns a list map"
    (let [params {:limit 1}
          response (domains/list-payment-method-domains stripe-mock-client params)]
      (is (map? response) "Response should be a map")
      (is (vector? (:data response)) "Response :data should be a vector of domains"))))

(deftest validate-payment-method-domain-test
  (testing "validate-payment-method-domain returns a valid response"
    (let [dummy-id "pmd_mock_123"
          response (domains/validate-payment-method-domain stripe-mock-client dummy-id)]
      (is (= "payment_method_domain" (:object response))
          "Returned object should be 'payment_method_domain'")))) 