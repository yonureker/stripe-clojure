(ns stripe-clojure.mock.treasury.outbound-transfers-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.treasury.outbound-transfers :as ot]))

(deftest create-outbound-transfer-test
  (testing "Create treasury outbound transfer"
    (let [params {:amount 700 :currency "usd" :financial_account "fa_mock"}
          response (ot/create-outbound-transfer stripe-mock-client params)]
      (is (map? response))
      (is (= "treasury.outbound_transfer" (:object response)))
      (is (string? (:id response)))
      (is (= 700 (:amount response)))
      (is (= "usd" (:currency response))))))

(deftest retrieve-outbound-transfer-test
  (testing "Retrieve treasury outbound transfer"
    (let [response (ot/retrieve-outbound-transfer stripe-mock-client "ot_mock")]
      (is (map? response))
      (is (= "treasury.outbound_transfer" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :amount)))))

(deftest list-outbound-transfers-test
  (testing "List treasury outbound transfers"
    (let [response (ot/list-outbound-transfers stripe-mock-client {:limit 2 :financial_account "fa_mock"})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [transfer (:data response)]
        (is (map? transfer))
        (is (= "treasury.outbound_transfer" (:object transfer)))
        (is (string? (:id transfer)))))))

(deftest cancel-outbound-transfer-test
  (testing "Cancel treasury outbound transfer"
    (let [response (ot/cancel-outbound-transfer stripe-mock-client "ot_cancel_mock")]
      (is (map? response))
      (is (= "treasury.outbound_transfer" (:object response)))
      (is (string? (:id response)))))) 