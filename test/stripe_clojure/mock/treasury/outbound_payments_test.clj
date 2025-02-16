(ns stripe-clojure.mock.treasury.outbound-payments-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.treasury.outbound-payments :as op]))

(deftest ^:integration create-outbound-payment-test
  (testing "Create treasury outbound payment"
    (let [params {:amount 500 :currency "usd" :financial_account "fa_mock"}
          response (op/create-outbound-payment stripe-client params)]
      (is (map? response))
      (is (= "treasury.outbound_payment" (:object response)))
      (is (string? (:id response)))
      (is (= 500 (:amount response)))
      (is (= "usd" (:currency response)))
      (is (= "fa_mock" (:financial_account response))))))

(deftest ^:integration retrieve-outbound-payment-test
  (testing "Retrieve treasury outbound payment"
    (let [response (op/retrieve-outbound-payment stripe-client "op_mock")]
      (is (map? response))
      (is (= "treasury.outbound_payment" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :amount)))))

(deftest ^:integration list-outbound-payments-test
  (testing "List treasury outbound payments"
    (let [response (op/list-outbound-payments stripe-client {:limit 2 :financial_account "fa_mock"})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [payment (:data response)]
        (is (map? payment))
        (is (= "treasury.outbound_payment" (:object payment)))
        (is (string? (:id payment)))))))

(deftest ^:integration cancel-outbound-payment-test
  (testing "Cancel treasury outbound payment"
    (let [response (op/cancel-outbound-payment stripe-client "op_cancel_mock")]
      (is (map? response))
      (is (= "treasury.outbound_payment" (:object response)))
      (is (string? (:id response)))))) 