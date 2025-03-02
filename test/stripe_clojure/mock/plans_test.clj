(ns stripe-clojure.mock.plans-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.plans :as plans]))

(deftest create-plan-test
  (testing "Create a plan with valid parameters"
    (let [params {:amount 1000
                  :currency "usd"
                  :interval "month"
                  :product "prod_mock_123"
                  :nickname "Basic Plan"}
          response (plans/create-plan stripe-mock-client params)]
      (is (string? (:id response)) "Plan should have an id")
      (is (= "plan" (:object response)) "Returned object should be 'plan'"))))

(deftest retrieve-plan-test
  (testing "Retrieve a plan by id"
    (let [dummy-id "plan_mock_123"
          response (plans/retrieve-plan stripe-mock-client dummy-id)]
      (is (= dummy-id (:id response)) "Retrieved plan id should match")
      (is (= "plan" (:object response)) "Returned object should be 'plan'"))))

(deftest update-plan-test
  (testing "Update a plan with valid parameters"
    (let [dummy-id "plan_mock_123"
          update-params {:nickname "Updated Plan"}
          response (plans/update-plan stripe-mock-client dummy-id update-params)]
      (is (= dummy-id (:id response)) "Plan id should remain unchanged")
      (is (= "plan" (:object response)) "Returned object should be 'plan'"))))

(deftest list-plans-test
  (testing "List all plans with query parameters"
    (let [params {:limit 1}
          response (plans/list-plans stripe-mock-client params)]
      (is (map? response) "Response should be a map")
      (is (vector? (:data response)) "Response :data should be a vector of plans"))))

(deftest delete-plan-test
  (testing "Delete a plan by id"
    (let [dummy-id "plan_mock_123"
          response (plans/delete-plan stripe-mock-client dummy-id)]
      (is (= dummy-id (:id response)) "Deleted plan id should match")
      (is (= "plan" (:object response)) "Returned object should be 'plan'")))) 