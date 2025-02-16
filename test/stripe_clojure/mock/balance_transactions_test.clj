(ns stripe-clojure.mock.balance-transactions-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.balance-transactions :as bt]))

(deftest ^:integration retrieve-balance-transaction-test
  (testing "Retrieve balance transaction"
    (let [response (bt/retrieve-balance-transaction stripe-client "bt_mock")]
      (is (map? response))
      (is (= "balance_transaction" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :amount))
      (is (number? (:amount response)))
      (is (contains? response :currency))
      (is (string? (:currency response)))
      (is (contains? response :created))
      (is (number? (:created response)))
      (is (contains? response :net))
      (is (number? (:net response))))))

(deftest ^:integration list-balance-transactions-test
  (testing "List balance transactions"
    (let [response (bt/list-balance-transactions stripe-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [tx (:data response)]
        (is (map? tx))
        (is (= "balance_transaction" (:object tx)))
        (is (string? (:id tx)))
        (is (contains? tx :amount))
        (is (number? (:amount tx)))
        (is (contains? tx :currency))
        (is (string? (:currency tx)))
        (is (contains? tx :created))
        (is (number? (:created tx)))
        (is (contains? tx :net))
        (is (number? (:net tx)))))))
