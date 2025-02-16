(ns stripe-clojure.mock.treasury.received-debits-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.treasury.received-debits :as rd]))

(deftest ^:integration retrieve-received-debit-test
  (testing "Retrieve treasury received debit"
    (let [response (rd/retrieve-received-debit stripe-client "rd_mock")]
      (is (map? response))
      (is (= "treasury.received_debit" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :amount)))))

(deftest ^:integration list-received-debits-test
  (testing "List treasury received debits"
    (let [response (rd/list-received-debits stripe-client {:limit 2 :financial_account "fa_mock"})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [debit (:data response)]
        (is (map? debit))
        (is (= "treasury.received_debit" (:object debit)))
        (is (string? (:id debit)))))))