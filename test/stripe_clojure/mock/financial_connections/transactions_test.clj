(ns stripe-clojure.mock.financial-connections.transactions-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.financial-connections.transactions :as transactions]))

(deftest ^:integration retrieve-transaction-test
  (testing "Retrieve a financial connections transaction using stripe‑mock"
    (let [dummy-id "txn_mock"
          response (transactions/retrieve-transaction stripe-client dummy-id)]
      (is (map? response))
      (is (= "financial_connections.transaction" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration list-transactions-test
  (testing "List financial connections transactions using stripe‑mock"
    (let [response (transactions/list-transactions stripe-client {:account "acct_mock"})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))))) 