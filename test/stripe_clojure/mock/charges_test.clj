(ns stripe-clojure.mock.charges-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.charges :as charges]))

(deftest ^:integration create-charge-test
  (testing "Create charge"
    (let [params {:amount 1000 :currency "usd" :source "tok_visa"}
          response (charges/create-charge stripe-client params)]
      (is (map? response))
      (is (= "charge" (:object response)))
      (is (string? (:id response)))
      (is (= 1000 (:amount response)))
      (is (= "usd" (:currency response)))
      (is (boolean? (:captured response))))))

(deftest ^:integration retrieve-charge-test
  (testing "Retrieve charge"
    (let [response (charges/retrieve-charge stripe-client "ch_mock")]
      (is (map? response))
      (is (= "charge" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :amount))
      (is (number? (:amount response))))))

(deftest ^:integration update-charge-test
  (testing "Update charge"
    (let [params {:description "Updated description"}
          response (charges/update-charge stripe-client "ch_mock" params)]
      (is (map? response))
      (is (= "charge" (:object response)))
      (is (string? (:id response)))
      (is (= "Updated description" (:description response))))))

(deftest ^:integration capture-charge-test
  (testing "Capture charge"
    (let [params {}
          response (charges/capture-charge stripe-client "ch_mock" params)]
      (is (map? response))
      (is (= "charge" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration list-charges-test
  (testing "List charges"
    (let [response (charges/list-charges stripe-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [charge (:data response)]
        (is (map? charge))
        (is (= "charge" (:object charge)))
        (is (string? (:id charge)))
        (is (contains? charge :amount))
        (is (number? (:amount charge)))))))

(deftest ^:integration search-charges-test
  (testing "Search charges"
    (let [response (charges/search-charges stripe-client {:query "amount>500"})]
      (is (map? response))
      (is (= "search_result" (:object response)))
      (is (vector? (:data response)))
      (doseq [charge (:data response)]
        (is (map? charge))
        (is (= "charge" (:object charge)))
        (is (string? (:id charge)))
        (is (contains? charge :amount))
        (is (number? (:amount charge)))))))

;; Expand examples

(deftest ^:integration create-charge-with-expand-test
  (testing "Create charge with expand parameter (balance_transaction)"
    (let [params {:amount 1000
                  :currency "usd"
                  :source "tok_visa"
                  :expand ["balance_transaction"]}
          response (charges/create-charge stripe-client params)]
      (is (map? response))
      (is (= "charge" (:object response)))
      (is (string? (:id response)))
      ;; Check that the charge includes an expanded balance transaction.
      (is (map? (:balance_transaction response)))
      (is (= "balance_transaction" (:object (:balance_transaction response)))))))

(deftest ^:integration retrieve-charge-with-expand-test
  (testing "Retrieve charge with expand parameter (balance_transaction)"
    (let [response (charges/retrieve-charge stripe-client "ch_mock" {:expand ["balance_transaction"]})]
      (is (map? response))
      (is (= "charge" (:object response)))
      ;; Verify that the expanded field is returned as a map.
      (is (map? (:balance_transaction response)))
      (is (= "balance_transaction" (:object (:balance_transaction response)))))))
