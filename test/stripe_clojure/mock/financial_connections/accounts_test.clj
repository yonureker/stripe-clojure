(ns stripe-clojure.mock.financial-connections.accounts-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.financial-connections.accounts :as accounts]))

(deftest ^:integration retrieve-account-test
  (testing "Retrieve a financial connections account using a dummy id"
    (let [dummy-id "acct_mock"
          response (accounts/retrieve-account stripe-client dummy-id)]
      (is (map? response))
      (is (= "financial_connections.account" (:object response)))
      (when (:id response)
        (is (string? (:id response)))))))

(deftest ^:integration list-accounts-test
  (testing "List financial connections accounts using stripe‑mock"
    (let [response (accounts/list-accounts stripe-client)]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response))))))

(deftest ^:integration disconnect-account-test
  (testing "Disconnect a financial connections account using stripe‑mock"
    (let [dummy-id "acct_mock"
          response (accounts/disconnect-account stripe-client dummy-id)]
      (is (map? response))
      (is (= "financial_connections.account" (:object response)))
      (when (:id response)
        (is (string? (:id response)))))))

(deftest ^:integration list-owners-test
  (testing "List owners for a financial connections account"
    (let [dummy-id "acct_mock"
          ;; For listing owners, no required params are necessary.
          response (accounts/list-owners stripe-client dummy-id {:ownership "test"})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response))))))

(deftest ^:integration refresh-account-test
  (testing "Refresh a financial connections account with required features parameter"
    (let [dummy-id "acct_mock"
          ;; Required param: for example, refreshing the "balance" data.
          params {:features ["balance"]}
          response (accounts/refresh-account stripe-client dummy-id params)]
      (is (map? response))
      (is (= "financial_connections.account" (:object response)))
      (when (:id response)
        (is (string? (:id response)))))))

(deftest ^:integration subscribe-account-test
  (testing "Subscribe to data refreshes for a financial connections account with required features parameter"
    (let [dummy-id "acct_mock"
          ;; Required param: for example, subscribing to "transactions" data.
          params {:features ["transactions"]}
          response (accounts/subscribe-account stripe-client dummy-id params)]
      (is (map? response))
      (is (= "financial_connections.account" (:object response)))
      (when (:id response)
        (is (string? (:id response)))))))

(deftest ^:integration unsubscribe-account-test
  (testing "Unsubscribe from data refreshes for a financial connections account with required features parameter"
    (let [dummy-id "acct_mock"
          ;; Required param: for example, unsubscribing from "transactions" data.
          params {:features ["transactions"]}
          response (accounts/unsubscribe-account stripe-client dummy-id params)]
      (is (map? response))
      (is (= "financial_connections.account" (:object response)))
      (when (:id response)
        (is (string? (:id response))))))) 