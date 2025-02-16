(ns stripe-clojure.mock.treasury.transaction-entries-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.treasury.transaction-entries :as te]))

(deftest ^:integration retrieve-transaction-entry-test
  (testing "Retrieve treasury transaction entry"
    (let [response (te/retrieve-transaction-entry stripe-client "te_mock")]
      (is (map? response))
      (is (= "treasury.transaction_entry" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :transaction)))))

(deftest ^:integration list-transaction-entries-test
  (testing "List treasury transaction entries"
    (let [response (te/list-transaction-entries stripe-client {:financial_account "fa_mock" :limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [entry (:data response)]
        (is (map? entry))
        (is (= "treasury.transaction_entry" (:object entry)))
        (is (string? (:id entry)))))))