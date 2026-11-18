(ns stripe-clojure.mock.test-helpers.issuing.authorizations-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.test-helpers.issuing.authorizations :as test-authorizations]))

(deftest create-authorization-test
  (testing "Creates a test mode authorization"
    (let [response (test-authorizations/create-authorization stripe-mock-client {:card "ic_mock" :amount 1000})]
      (is (map? response))
      (is (= "issuing.authorization" (:object response)))
      (is (string? (:id response))))))

(deftest capture-authorization-test
  (testing "Captures a test mode authorization"
    (let [response (test-authorizations/capture-authorization stripe-mock-client "iauth_mock")]
      (is (map? response))
      (is (= "issuing.authorization" (:object response)))
      (is (string? (:id response))))))

(deftest expire-authorization-test
  (testing "Expires a test mode authorization"
    (let [response (test-authorizations/expire-authorization stripe-mock-client "iauth_mock")]
      (is (map? response))
      (is (= "issuing.authorization" (:object response)))
      (is (string? (:id response))))))

(deftest finalize-amount-test
  (testing "Finalizes the amount on an authorization prior to capture"
    (let [response (test-authorizations/finalize-amount stripe-mock-client "iauth_mock" {:final_amount 1000})]
      (is (map? response))
      (is (= "issuing.authorization" (:object response)))
      (is (string? (:id response))))))

(deftest respond-to-fraud-challenge-test
  (testing "Responds to a fraud challenge on a test mode authorization"
    (let [response (test-authorizations/respond-to-fraud-challenge stripe-mock-client "iauth_mock" {:confirmed true})]
      (is (map? response))
      (is (= "issuing.authorization" (:object response)))
      (is (string? (:id response))))))

(deftest increment-authorization-test
  (testing "Increments a test mode authorization"
    (let [response (test-authorizations/increment-authorization stripe-mock-client "iauth_mock" {:increment_amount 500})]
      (is (map? response))
      (is (= "issuing.authorization" (:object response)))
      (is (string? (:id response))))))

(deftest reverse-authorization-test
  (testing "Reverses a test mode authorization"
    (let [response (test-authorizations/reverse-authorization stripe-mock-client "iauth_mock")]
      (is (map? response))
      (is (= "issuing.authorization" (:object response)))
      (is (string? (:id response))))))
