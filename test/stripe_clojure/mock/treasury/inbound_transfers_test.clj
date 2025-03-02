(ns stripe-clojure.mock.treasury.inbound-transfers-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.treasury.inbound-transfers :as it]))

(deftest create-inbound-transfer-test
  (testing "Create treasury inbound transfer"
    (let [params {:amount 1000 :currency "usd" :financial_account "fa_mock"
                  :origin_payment_method "pm_mock"}
          response (it/create-inbound-transfer stripe-mock-client params)]
      (is (map? response))
      (is (= "treasury.inbound_transfer" (:object response)))
      (is (string? (:id response)))
      (is (= 1000 (:amount response)))
      (is (= "usd" (:currency response)))
      (is (= "fa_mock" (:financial_account response))))))

(deftest retrieve-inbound-transfer-test
  (testing "Retrieve treasury inbound transfer"
    (let [response (it/retrieve-inbound-transfer stripe-mock-client "it_mock")]
      (is (map? response))
      (is (= "treasury.inbound_transfer" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :amount)))))

(deftest list-inbound-transfers-test
  (testing "List treasury inbound transfers"
    (let [response (it/list-inbound-transfers stripe-mock-client {:limit 2 :financial_account "fa_mock"})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [transfer (:data response)]
        (is (map? transfer))
        (is (= "treasury.inbound_transfer" (:object transfer)))
        (is (string? (:id transfer)))))))

(deftest cancel-inbound-transfer-test
  (testing "Cancel treasury inbound transfer"
    (let [response (it/cancel-inbound-transfer stripe-mock-client "it_cancel_mock")]
      (is (map? response))
      (is (= "treasury.inbound_transfer" (:object response)))
      (is (string? (:id response)))))) 