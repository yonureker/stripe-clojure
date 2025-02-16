(ns stripe-clojure.mock.treasury.transactions-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.treasury.transactions :as tt]))

(deftest ^:integration retrieve-transaction-test
  (testing "Retrieve treasury transaction"
    (let [response (tt/retrieve-transaction stripe-client "txn_mock")]
      (is (map? response))
      (is (= "treasury.transaction" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :amount))
      (is (number? (:amount response)))
      (is (contains? response :currency))
      (is (string? (:currency response)))
      (is (contains? response :created))
      (is (number? (:created response))))))

(deftest ^:integration list-transactions-test
  (testing "List treasury transactions"
    (let [response (tt/list-transactions stripe-client {:limit 2 :financial_account "fa_mock"})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [tx (:data response)]
        (is (map? tx))
        (is (= "treasury.transaction" (:object tx)))
        (is (string? (:id tx)))
        (is (contains? tx :amount))
        (is (number? (:amount tx)))
        (is (contains? tx :currency))
        (is (string? (:currency tx))))))) 