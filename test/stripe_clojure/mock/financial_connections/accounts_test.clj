(ns stripe-clojure.mock.financial-connections.accounts-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.financial-connections.accounts :as accounts]))

(deftest retrieve-account-test
  (testing "Retrieve a financial connections account using a dummy id"
    (let [dummy-id "acct_mock"
          response (accounts/retrieve-account stripe-mock-client dummy-id)]
      (is (map? response))
      (is (= "financial_connections.account" (:object response)))
      (when (:id response)
        (is (string? (:id response)))))))

(deftest list-accounts-test
  (testing "List financial connections accounts using stripe‑mock"
    (let [response (accounts/list-accounts stripe-mock-client)]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response))))))

(deftest disconnect-account-test
  (testing "Disconnect a financial connections account using stripe‑mock"
    (let [dummy-id "acct_mock"
          response (accounts/disconnect-account stripe-mock-client dummy-id)]
      (is (map? response))
      (is (= "financial_connections.account" (:object response)))
      (when (:id response)
        (is (string? (:id response)))))))

(deftest list-owners-test
  (testing "List owners for a financial connections account"
    (let [dummy-id "acct_mock"
          ;; For listing owners, no required params are necessary.
          response (accounts/list-owners stripe-mock-client dummy-id {:ownership "test"})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response))))))

(deftest refresh-account-test
  (testing "Refresh a financial connections account with required features parameter"
    (let [dummy-id "acct_mock"
          ;; Required param: for example, refreshing the "balance" data.
          params {:features ["balance"]}
          response (accounts/refresh-account stripe-mock-client dummy-id params)]
      (is (map? response))
      (is (= "financial_connections.account" (:object response)))
      (when (:id response)
        (is (string? (:id response)))))))

(deftest subscribe-account-test
  (testing "Subscribe to data refreshes for a financial connections account with required features parameter"
    (let [dummy-id "acct_mock"
          ;; Required param: for example, subscribing to "transactions" data.
          params {:features ["transactions"]}
          response (accounts/subscribe-account stripe-mock-client dummy-id params)]
      (is (map? response))
      (is (= "financial_connections.account" (:object response)))
      (when (:id response)
        (is (string? (:id response)))))))

(deftest unsubscribe-account-test
  (testing "Unsubscribe from data refreshes for a financial connections account with required features parameter"
    (let [dummy-id "acct_mock"
          ;; Required param: for example, unsubscribing from "transactions" data.
          params {:features ["transactions"]}
          response (accounts/unsubscribe-account stripe-mock-client dummy-id params)]
      (is (map? response))
      (is (= "financial_connections.account" (:object response)))
      (when (:id response)
        (is (string? (:id response))))))) 