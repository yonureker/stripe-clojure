(ns stripe-clojure.mock.sources-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.sources :as sources]))

(deftest create-source-test
  (testing "Create source"
    (let [params {:type "card" :currency "usd" :owner {:email "test@example.com"}}
          response (sources/create-source stripe-mock-client params)]
      (is (map? response))
      (is (= "source" (:object response)))
      (is (string? (:id response)))
      (is (= "card" (:type response)))
      (is (= "usd" (:currency response))))))

(deftest retrieve-source-test
  (testing "Retrieve source"
    (let [response (sources/retrieve-source stripe-mock-client "src_mock")]
      (is (map? response))
      (is (= "source" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :type))
      (is (string? (:type response))))))

(deftest update-source-test
  (testing "Update source"
    (let [params {:owner {:email "updated@example.com"}}
          response (sources/update-source stripe-mock-client "src_mock" params)]
      (is (map? response))
      (is (= "source" (:object response)))
      (is (string? (:id response))))))

(deftest verify-source-test
  (testing "Verify source"
    (let [params {:amounts [32 45]}
          response (sources/verify-source stripe-mock-client "src_mock" params)]
      (is (map? response))
      ;; stripe-mock may return nil for this endpoint
      (is (map? response)))))

(deftest list-source-transactions-test
  (testing "List source transactions"
    (let [response (sources/list-source-transactions stripe-mock-client "src_mock")]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (is (boolean? (:has_more response))))))

(deftest retrieve-source-transaction-test
  (testing "Retrieve source transaction"
    (let [response (sources/retrieve-source-transaction stripe-mock-client "src_mock" "srctxn_mock")]
      (is (map? response))
      (is (= "source_transaction" (:object response)))
      (is (string? (:id response))))))

(deftest retrieve-mandate-notification-test
  (testing "Retrieve mandate notification"
    (let [response (sources/retrieve-mandate-notification stripe-mock-client "src_mock" "srcmnd_mock")]
      (is (map? response))
      (is (= "source_mandate_notification" (:object response)))
      (is (string? (:id response))))))