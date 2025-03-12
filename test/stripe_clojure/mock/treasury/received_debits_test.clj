(ns stripe-clojure.mock.treasury.received-debits-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.treasury.received-debits :as rd]))

(deftest retrieve-received-debit-test
  (testing "Retrieve treasury received debit"
    (let [response (rd/retrieve-received-debit stripe-mock-client "rd_mock")]
      (is (map? response))
      (is (= "treasury.received_debit" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :amount)))))

(deftest list-received-debits-test
  (testing "List treasury received debits"
    (let [response (rd/list-received-debits stripe-mock-client {:limit 2 :financial_account "fa_mock"})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [debit (:data response)]
        (is (map? debit))
        (is (= "treasury.received_debit" (:object debit)))
        (is (string? (:id debit)))))))