(ns stripe-clojure.mock.linked-accounts-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.linked-accounts :as linked-accounts]))

(deftest retrieve-linked-account-test
  (testing "Retrieve linked account"
    (let [response (linked-accounts/retrieve-linked-account stripe-mock-client "fca_mock")]
      (is (map? response))
      ;; stripe-mock returns financial_connections.account instead of linked_account
      (is (= "financial_connections.account" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :status)))))

(deftest list-linked-accounts-test
  (testing "List linked accounts"
    (let [response (linked-accounts/list-linked-accounts stripe-mock-client)]
      (is (map? response))
      ;; stripe-mock maps this to financial_connections accounts
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (is (boolean? (:has_more response))))))

(deftest list-linked-accounts-with-params-test
  (testing "List linked accounts with params"
    (let [params {:limit 10}
          response (linked-accounts/list-linked-accounts stripe-mock-client params)]
      (is (map? response))
      ;; stripe-mock maps this to financial_connections accounts
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (is (boolean? (:has_more response))))))

(deftest disconnect-linked-account-test
  (testing "Disconnect linked account"
    (let [response (linked-accounts/disconnect-linked-account stripe-mock-client "fca_mock")]
      (is (map? response))
      ;; stripe-mock returns financial_connections.account instead of linked_account
      (is (= "financial_connections.account" (:object response)))
      (is (string? (:id response))))))

(deftest refresh-linked-account-test
  (testing "Refresh linked account"
    (let [params {:features ["balances"]}
          response (linked-accounts/refresh-linked-account stripe-mock-client "fca_mock" params)]
      (is (map? response))
      ;; stripe-mock doesn't support this endpoint
      (is (= "invalid_request_error" (:type response)))
      (is (string? (:message response))))))

(deftest list-linked-account-owners-test
  (testing "List linked account owners"
    (let [response (linked-accounts/list-linked-account-owners stripe-mock-client "fca_mock")]
      (is (map? response))
      ;; stripe-mock doesn't support this endpoint
      (is (= "invalid_request_error" (:type response)))
      (is (string? (:message response))))))