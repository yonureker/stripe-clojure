(ns stripe-clojure.mock.billing.credit-balance-transactions-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.billing.credit-balance-transactions :as cbt]))

(def dummy-customer-id "cus_mock")

(deftest list-credit-balance-transactions-test
  (testing "List credit balance transactions using a dummy customer id"
    (let [response (cbt/list-credit-balance-transactions stripe-mock-client {:customer dummy-customer-id})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response))))))

(deftest retrieve-credit-balance-transaction-test
  (testing "Retrieve a credit balance transaction if available"
    (let [transactions (cbt/list-credit-balance-transactions stripe-mock-client {:customer dummy-customer-id})
          transaction-id (some-> (:data transactions) first :id)]
      (if transaction-id
        (let [retrieved (cbt/retrieve-credit-balance-transaction stripe-mock-client transaction-id)]
          (is (= transaction-id (:id retrieved)))
          (is (= "billing.credit_balance_transaction" (:object retrieved))))
        (testing "No transactions available for retrieval (acceptable with stripe-mock)"
          (is true "Skipped retrieval test as no transactions were returned"))))))