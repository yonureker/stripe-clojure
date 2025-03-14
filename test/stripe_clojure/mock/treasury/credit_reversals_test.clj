(ns stripe-clojure.mock.treasury.credit-reversals-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.treasury.credit-reversals :as cr]))

(deftest create-credit-reversal-test
  (testing "Create treasury credit reversal"
    (let [params {:received_credit "rd_mock"}
          response (cr/create-credit-reversal stripe-mock-client params)]
      (is (map? response))
      (is (= "treasury.credit_reversal" (:object response)))
      (is (string? (:id response)))
      (is (= 1000 (:amount response)))
      (is (= "usd" (:currency response))))))

(deftest retrieve-credit-reversal-test
  (testing "Retrieve treasury credit reversal"
    (let [response (cr/retrieve-credit-reversal stripe-mock-client "cr_mock")]
      (is (map? response))
      (is (= "treasury.credit_reversal" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :amount)))))

(deftest list-credit-reversals-test
  (testing "List treasury credit reversals"
    (let [response (cr/list-credit-reversals stripe-mock-client {:limit 2 :financial_account "fa_mock"})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [item (:data response)]
        (is (map? item))
        (is (= "treasury.credit_reversal" (:object item)))
        (is (string? (:id item))))))) 