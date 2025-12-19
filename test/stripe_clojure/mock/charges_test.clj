(ns stripe-clojure.mock.charges-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.charges :as charges]))

(deftest create-charge-test
  (testing "Create charge"
    (let [params {:amount 1000 :currency "usd" :source "tok_visa"}
          response (charges/create-charge stripe-mock-client params)]
      (is (map? response))
      (is (= "charge" (:object response)))
      (is (string? (:id response)))
      (is (= 1000 (:amount response)))
      (is (= "usd" (:currency response)))
      (is (boolean? (:captured response))))))

(deftest retrieve-charge-test
  (testing "Retrieve charge"
    (let [response (charges/retrieve-charge stripe-mock-client "ch_mock")]
      (is (map? response))
      (is (= "charge" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :amount))
      (is (number? (:amount response))))))

(deftest update-charge-test
  (testing "Update charge"
    (let [params {:description "Updated description"}
          response (charges/update-charge stripe-mock-client "ch_mock" params)]
      (is (map? response))
      (is (= "charge" (:object response)))
      (is (string? (:id response)))
      (is (= "Updated description" (:description response))))))

(deftest capture-charge-test
  (testing "Capture charge"
    (let [params {}
          response (charges/capture-charge stripe-mock-client "ch_mock" params)]
      (is (map? response))
      (is (= "charge" (:object response)))
      (is (string? (:id response))))))

(deftest list-charges-test
  (testing "List charges"
    (let [response (charges/list-charges stripe-mock-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [charge (:data response)]
        (is (map? charge))
        (is (= "charge" (:object charge)))
        (is (string? (:id charge)))
        (is (contains? charge :amount))
        (is (number? (:amount charge)))))))

(deftest search-charges-test
  (testing "Search charges"
    (let [response (charges/search-charges stripe-mock-client {:query "amount>500"})]
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

(deftest create-charge-with-expand-test
  (testing "Create charge with expand parameter (balance_transaction)"
    (let [params {:amount 1000
                  :currency "usd"
                  :source "tok_visa"
                  :expand ["balance_transaction"]}
          opts {:idempotency-key "abcd-efgh-ijkl-mnop"}
          response (charges/create-charge stripe-mock-client params opts)]
      (is (map? response))
      (is (= "charge" (:object response)))
      (is (string? (:id response)))
      ;; Check that the charge includes an expanded balance transaction.
      (is (map? (:balance_transaction response)))
      (is (= "balance_transaction" (:object (:balance_transaction response)))))))

(deftest retrieve-charge-with-expand-test
  (testing "Retrieve charge with expand parameter (balance_transaction)"
    (let [response (charges/retrieve-charge stripe-mock-client "ch_mock" {:expand ["balance_transaction"]})]
      (is (map? response))
      (is (= "charge" (:object response)))
      ;; Verify that the expanded field is returned as a map.
      (is (map? (:balance_transaction response)))
      (is (= "balance_transaction" (:object (:balance_transaction response)))))))

;; Dispute Tests
(deftest retrieve-dispute-test
  (testing "Retrieve dispute"
    (let [response (charges/retrieve-dispute stripe-mock-client "ch_mock")]
      (is (map? response))
      (is (= "dispute" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :status)))))

(deftest close-dispute-test
  (testing "Close dispute"
    (let [response (charges/close-dispute stripe-mock-client "ch_mock")]
      (is (map? response))
      (is (= "dispute" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :status)))))

;; Refund Tests
(deftest refund-charge-test
  (testing "Refund charge"
    (let [params {:amount 500}
          response (charges/refund-charge stripe-mock-client "ch_mock" params)]
      (is (map? response))
      (is (= "charge" (:object response)))
      (is (string? (:id response)))
      (is (boolean? (:refunded response))))))

(deftest create-refund-test
  (testing "Create refund"
    (let [params {:amount 500}
          response (charges/create-refund stripe-mock-client "ch_mock" params)]
      (is (map? response))
      (is (= "refund" (:object response)))
      (is (string? (:id response)))
      (is (= 500 (:amount response))))))

(deftest update-refund-test
  (testing "Update refund"
    (let [params {:metadata {:key "value"}}
          response (charges/update-refund stripe-mock-client "ch_mock" "re_mock" params)]
      (is (map? response))
      (is (= "refund" (:object response)))
      (is (string? (:id response))))))
