(ns stripe-clojure.mock.test-helpers.issuing.transactions-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.test-helpers.issuing.transactions :as test-transactions]))

(deftest create-force-capture-test
  (testing "Creates a test mode forced capture for an arbitrary amount"
    (let [response (test-transactions/create-force-capture stripe-mock-client {:amount 1000 :card "ic_mock"})]
      (is (map? response))
      (is (= "issuing.transaction" (:object response)))
      (is (string? (:id response))))))

(deftest create-unlinked-refund-test
  (testing "Creates a test mode unlinked refund for an arbitrary amount"
    (let [response (test-transactions/create-unlinked-refund stripe-mock-client {:amount 1000 :card "ic_mock"})]
      (is (map? response))
      (is (= "issuing.transaction" (:object response)))
      (is (string? (:id response))))))

(deftest refund-transaction-test
  (testing "Refunds a test mode transaction"
    (let [response (test-transactions/refund-transaction stripe-mock-client "ipi_mock")]
      (is (map? response))
      (is (= "issuing.transaction" (:object response)))
      (is (string? (:id response))))))
