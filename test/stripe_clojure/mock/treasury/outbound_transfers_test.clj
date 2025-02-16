(ns stripe-clojure.mock.treasury.outbound-transfers-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.treasury.outbound-transfers :as ot]))

(deftest ^:integration create-outbound-transfer-test
  (testing "Create treasury outbound transfer"
    (let [params {:amount 700 :currency "usd" :financial_account "fa_mock"}
          response (ot/create-outbound-transfer stripe-client params)]
      (is (map? response))
      (is (= "treasury.outbound_transfer" (:object response)))
      (is (string? (:id response)))
      (is (= 700 (:amount response)))
      (is (= "usd" (:currency response))))))

(deftest ^:integration retrieve-outbound-transfer-test
  (testing "Retrieve treasury outbound transfer"
    (let [response (ot/retrieve-outbound-transfer stripe-client "ot_mock")]
      (is (map? response))
      (is (= "treasury.outbound_transfer" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :amount)))))

(deftest ^:integration list-outbound-transfers-test
  (testing "List treasury outbound transfers"
    (let [response (ot/list-outbound-transfers stripe-client {:limit 2 :financial_account "fa_mock"})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [transfer (:data response)]
        (is (map? transfer))
        (is (= "treasury.outbound_transfer" (:object transfer)))
        (is (string? (:id transfer)))))))

(deftest ^:integration cancel-outbound-transfer-test
  (testing "Cancel treasury outbound transfer"
    (let [response (ot/cancel-outbound-transfer stripe-client "ot_cancel_mock")]
      (is (map? response))
      (is (= "treasury.outbound_transfer" (:object response)))
      (is (string? (:id response)))))) 