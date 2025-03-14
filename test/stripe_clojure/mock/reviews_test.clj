(ns stripe-clojure.mock.reviews-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.reviews :as reviews]))

(deftest retrieve-review-test
  (testing "Retrieve review"
    (let [response (reviews/retrieve-review stripe-mock-client "rvw_mock")]
      (is (map? response))
      (is (= "review" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :open))
      (is (boolean? (:open response)))
      (is (contains? response :charge))
      (is (string? (:charge response))))))

(deftest list-reviews-test
  (testing "List reviews"
    (let [response (reviews/list-reviews stripe-mock-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [r (:data response)]
        (is (map? r))
        (is (= "review" (:object r)))
        (is (string? (:id r)))
        (is (contains? r :open))
        (is (boolean? (:open r)))
        (is (contains? r :charge))
        (is (string? (:charge r)))))))

(deftest approve-review-test
  (testing "Approve review"
    (let [response (reviews/approve-review stripe-mock-client "rvw_mock")]
      (is (map? response))
      (is (= "review" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :open))
      (is (contains? response :charge))
      (is (string? (:charge response)))))) 