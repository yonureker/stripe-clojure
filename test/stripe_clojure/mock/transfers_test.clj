(ns stripe-clojure.mock.transfers-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.transfers :as transfers]))

(deftest create-transfer-test
  (testing "Create transfer"
    (let [params {:amount 5000 :currency "usd" :destination "acct_mock"}
          response (transfers/create-transfer stripe-mock-client params)]
      (is (map? response))
      (is (= "transfer" (:object response)))
      (is (string? (:id response)))
      (is (= 5000 (:amount response)))
      (is (= "usd" (:currency response)))
      (is (= "acct_mock" (:destination response))))))

(deftest retrieve-transfer-test
  (testing "Retrieve transfer"
    (let [response (transfers/retrieve-transfer stripe-mock-client "tr_mock")]
      (is (map? response))
      (is (= "transfer" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :destination))
      (is (string? (:destination response))))))

(deftest update-transfer-test
  (testing "Update transfer"
    (let [params {:description "Updated transfer"}
          response (transfers/update-transfer stripe-mock-client "tr_mock" params)]
      (is (map? response))
      (is (= "transfer" (:object response)))
      (is (string? (:id response)))
      (is (= "Updated transfer" (:description response))))))

(deftest list-transfers-test
  (testing "List transfers"
    (let [response (transfers/list-transfers stripe-mock-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [t (:data response)]
        (is (map? t))
        (is (= "transfer" (:object t)))
        (is (string? (:id t)))
        (is (contains? t :amount))
        (is (number? (:amount t)))))))

(deftest create-reversal-test
  (testing "Create reversal"
    (let [params {:amount 1000}
          response (transfers/create-reversal stripe-mock-client "tr_mock" params)]
      (is (map? response))
      (is (= "transfer_reversal" (:object response)))
      (is (string? (:id response)))
      (is (= 1000 (:amount response))))))

(deftest retrieve-reversal-test
  (testing "Retrieve reversal"
    (let [response (transfers/retrieve-reversal stripe-mock-client "tr_mock" "trr_mock")]
      (is (map? response))
      (is (= "transfer_reversal" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :amount))
      (is (number? (:amount response))))))

(deftest update-reversal-test
  (testing "Update reversal"
    (let [params {}
          response (transfers/update-reversal stripe-mock-client "tr_mock" "trr_mock" params)]
      (is (map? response))
      (is (= "transfer_reversal" (:object response)))
      (is (string? (:id response))))))

(deftest list-reversals-test
  (testing "List reversals"
    (let [response (transfers/list-reversals stripe-mock-client "tr_mock" {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [rev (:data response)]
        (is (map? rev))
        (is (= "transfer_reversal" (:object rev)))
        (is (string? (:id rev)))
        (is (contains? rev :amount))
        (is (number? (:amount rev))))))) 