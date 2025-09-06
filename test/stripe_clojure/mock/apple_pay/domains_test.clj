(ns stripe-clojure.mock.apple-pay.domains-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.apple-pay.domains :as domains]))

(deftest create-apple-pay-domain-test
  (testing "Create apple pay domain"
    (let [params {:domain_name "example.com"}
          response (domains/create-apple-pay-domain stripe-mock-client params)]
      (is (map? response))
      (is (= "apple_pay_domain" (:object response)))
      (is (string? (:id response)))
      (is (= "example.com" (:domain_name response))))))

(deftest retrieve-apple-pay-domain-test
  (testing "Retrieve apple pay domain"
    (let [response (domains/retrieve-apple-pay-domain stripe-mock-client "apwc_mock")]
      (is (map? response))
      (is (= "apple_pay_domain" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :domain_name))
      (is (contains? response :livemode)))))

(deftest delete-apple-pay-domain-test
  (testing "Delete apple pay domain"
    (let [response (domains/delete-apple-pay-domain stripe-mock-client "apwc_mock")]
      (is (map? response))
      (is (= "apple_pay_domain" (:object response)))
      (is (string? (:id response)))
      (is (true? (:deleted response))))))

(deftest list-apple-pay-domains-test
  (testing "List apple pay domains"
    (let [response (domains/list-apple-pay-domains stripe-mock-client)]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (is (boolean? (:has_more response))))))

(deftest list-apple-pay-domains-with-params-test
  (testing "List apple pay domains with params"
    (let [params {:limit 10}
          response (domains/list-apple-pay-domains stripe-mock-client params)]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (is (boolean? (:has_more response))))))