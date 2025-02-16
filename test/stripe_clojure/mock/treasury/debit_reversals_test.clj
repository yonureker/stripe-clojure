(ns stripe-clojure.mock.treasury.debit-reversals-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.treasury.debit-reversals :as dr]))

(deftest ^:integration create-debit-reversal-test
  (testing "Create treasury debit reversal"
    (let [params {:received_debit "txn_mock"}
          response (dr/create-debit-reversal stripe-client params)]
      (is (map? response))
      (is (= "treasury.debit_reversal" (:object response)))
      (is (string? (:id response)))
      (is (string?  (:transaction response))))))

(deftest ^:integration retrieve-debit-reversal-test
  (testing "Retrieve treasury debit reversal"
    (let [response (dr/retrieve-debit-reversal stripe-client "dr_mock")]
      (is (map? response))
      (is (= "treasury.debit_reversal" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :amount)))))

(deftest ^:integration list-debit-reversals-test
  (testing "List treasury debit reversals"
    (let [response (dr/list-debit-reversals stripe-client {:limit 2 :financial_account "fa_mock"})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [item (:data response)]
        (is (map? item))
        (is (= "treasury.debit_reversal" (:object item)))
        (is (string? (:id item))))))) 