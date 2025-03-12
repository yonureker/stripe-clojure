(ns stripe-clojure.mock.subscriptions-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.subscriptions :as subscriptions]))

(deftest create-subscription-test
  (testing "Create subscription"
    (let [params {:customer "cus_mock" :items [{:plan "plan_mock"}]}
          response (subscriptions/create-subscription stripe-mock-client params)]
      (is (map? response))
      (is (= "subscription" (:object response)))
      (is (string? (:id response)))
      (is (= "cus_mock" (:customer response)))
      (is (vector? (get-in response [:items :data])))
      (is (not-empty (:items response))))))

(deftest retrieve-subscription-test
  (testing "Retrieve subscription"
    (let [response (subscriptions/retrieve-subscription stripe-mock-client "sub_mock")]
      (is (map? response))
      (is (= "subscription" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :customer)))))

(deftest update-subscription-test
  (testing "Update subscription"
    (let [params {:items [{:plan "new_plan"}]}
          response (subscriptions/update-subscription stripe-mock-client "sub_mock" params)]
      (is (map? response))
      (is (= "subscription" (:object response)))
      (is (string? (:id response))))))

(deftest cancel-subscription-test
  (testing "Cancel subscription"
    (let [response (subscriptions/cancel-subscription stripe-mock-client "sub_mock")]
      (is (map? response))
      (is (= "subscription" (:object response)))
      (is (string? (:id response))))))

(deftest list-subscriptions-test
  (testing "List subscriptions"
    (let [response (subscriptions/list-subscriptions stripe-mock-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [sub (:data response)]
        (is (map? sub))
        (is (= "subscription" (:object sub)))
        (is (string? (:id sub)))))))

(deftest search-subscriptions-test
  (testing "Search subscriptions"
    (let [params {:query "status:'active'"}
          response (subscriptions/search-subscriptions stripe-mock-client params)]
      (is (map? response))
      (is (= "search_result" (:object response)))
      (is (vector? (:data response)))
      (doseq [sub (:data response)]
        (is (map? sub))
        (is (= "subscription" (:object sub)))
        (is (string? (:id sub)))))))

(deftest resume-subscription-test
  (testing "Resume subscription"
    (let [params {:billing_cycle_anchor "now"}
          response (subscriptions/resume-subscription stripe-mock-client "sub_mock" params)]
      (is (map? response))
      (is (= "subscription" (:object response)))
      (is (string? (:id response))))))