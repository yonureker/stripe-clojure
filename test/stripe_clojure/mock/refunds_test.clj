(ns stripe-clojure.mock.refunds-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.refunds :as refunds]))

(deftest create-refund-test
  (testing "Create refund"
    (let [params {:charge "ch_mock" :amount 500}
          response (refunds/create-refund stripe-mock-client params)]
      (is (map? response))
      (is (= "refund" (:object response)))
      (is (string? (:id response)))
      (is (= "ch_mock" (:charge response)))
      (is (= 500 (:amount response))))))

(deftest retrieve-refund-test
  (testing "Retrieve refund"
    (let [response (refunds/retrieve-refund stripe-mock-client "rf_mock")]
      (is (map? response))
      (is (= "refund" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :charge))
      (is (string? (:charge response))))))

(deftest update-refund-test
  (testing "Update refund"
    (let [params {}
          response (refunds/update-refund stripe-mock-client "rf_mock" params)]
      (is (map? response))
      (is (= "refund" (:object response)))
      (is (string? (:id response))))))

(deftest list-refunds-test
  (testing "List refunds"
    (let [response (refunds/list-refunds stripe-mock-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [r (:data response)]
        (is (map? r))
        (is (= "refund" (:object r)))
        (is (string? (:id r)))
        (is (contains? r :charge))
        (is (string? (:charge r)))
        (is (contains? r :amount))
        (is (number? (:amount r)))))))

(deftest cancel-refund-test
  (testing "Cancel refund"
    (let [response (refunds/cancel-refund stripe-mock-client "rf_mock")]
      (is (map? response))
      (is (= "refund" (:object response)))
      (is (string? (:id response)))))) 