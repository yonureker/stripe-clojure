(ns stripe-clojure.mock.issuing.transactions-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.issuing.transactions :as transactions]))

(deftest retrieve-transaction-test
  (testing "Retrieve an issuing transaction"
    (let [response (transactions/retrieve-transaction stripe-mock-client "txn_mock")]
      (is (map? response))
      (is (= "issuing.transaction" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :amount))
      (is (number? (:amount response))))))

(deftest update-transaction-test
  (testing "Update an issuing transaction"
    (let [params   {:metadata {:updated "true"}}
          response (transactions/update-transaction stripe-mock-client "txn_mock" params)]
      (is (map? response))
      (is (= "issuing.transaction" (:object response)))
      (is (string? (:id response))))))

(deftest list-transactions-test
  (testing "List issuing transactions"
    (let [response (transactions/list-transactions stripe-mock-client {})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [txn (:data response)]
        (is (map? txn))
        (is (= "issuing.transaction" (:object txn)))
        (is (string? (:id txn)))
        (is (contains? txn :amount))
        (is (number? (:amount txn))))))) 