(ns stripe-clojure.mock.topups-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.topups :as topups]))

(deftest create-topup-test
  (testing "Create topup"
    (let [params {:amount 1000 :currency "usd" :description "Test topup"}
          response (topups/create-topup stripe-mock-client params)]
      (is (map? response))
      (is (= "topup" (:object response)))
      (is (string? (:id response)))
      (is (= 1000 (:amount response)))
      (is (= "usd" (:currency response)))
      (is (= "Test topup" (:description response))))))

(deftest retrieve-topup-test
  (testing "Retrieve topup"
    (let [response (topups/retrieve-topup stripe-mock-client "tu_mock")]
      (is (map? response))
      (is (= "topup" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :amount)))))

(deftest update-topup-test
  (testing "Update topup"
    (let [params {:description "Updated topup"}
          response (topups/update-topup stripe-mock-client "tu_mock" params)]
      (is (map? response))
      (is (= "topup" (:object response)))
      (is (string? (:id response)))
      (is (= "Updated topup" (:description response))))))

(deftest list-topups-test
  (testing "List topups"
    (let [response (topups/list-topups stripe-mock-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [t (:data response)]
        (is (map? t))
        (is (= "topup" (:object t)))
        (is (string? (:id t)))))))

(deftest cancel-topup-test
  (testing "Cancel topup"
    (let [response (topups/cancel-topup stripe-mock-client "tu_mock")]
      (is (map? response))
      (is (= "topup" (:object response)))
      (is (string? (:id response)))))) 