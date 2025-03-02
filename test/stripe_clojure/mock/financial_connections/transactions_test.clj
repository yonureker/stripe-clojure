(ns stripe-clojure.mock.financial-connections.transactions-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.financial-connections.transactions :as transactions]))

(deftest retrieve-transaction-test
  (testing "Retrieve a financial connections transaction using stripe‑mock"
    (let [dummy-id "txn_mock"
          response (transactions/retrieve-transaction stripe-mock-client dummy-id)]
      (is (map? response))
      (is (= "financial_connections.transaction" (:object response)))
      (is (string? (:id response))))))

(deftest list-transactions-test
  (testing "List financial connections transactions using stripe‑mock"
    (let [response (transactions/list-transactions stripe-mock-client {:account "acct_mock"})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))))) 