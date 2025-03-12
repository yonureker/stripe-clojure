(ns stripe-clojure.mock.subscription-schedules-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.subscription-schedules :as schedules]))

(deftest create-subscription-schedule-test
  (testing "Create subscription schedule"
    (let [params {:phases [{:items [{:price "plan_mock"
                                     :quantity 100}]
                            :iterations 1}]}
          response (schedules/create-subscription-schedule stripe-mock-client params)]
      (is (map? response))
      (is (= "subscription_schedule" (:object response)))
      (is (string? (:id response))))))

(deftest retrieve-subscription-schedule-test
  (testing "Retrieve subscription schedule"
    (let [response (schedules/retrieve-subscription-schedule stripe-mock-client "ss_mock")]
      (is (map? response))
      (is (= "subscription_schedule" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :subscription)))))

(deftest update-subscription-schedule-test
  (testing "Update subscription schedule"
    (let [params {:end_behavior "cancel"}
          response (schedules/update-subscription-schedule stripe-mock-client "ss_mock" params)]
      (is (map? response))
      (is (= "subscription_schedule" (:object response)))
      (is (string? (:id response)))
      (is (= "cancel" (:end_behavior response))))))

(deftest list-subscription-schedules-test
  (testing "List subscription schedules"
    (let [response (schedules/list-subscription-schedules stripe-mock-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [sched (:data response)]
        (is (map? sched))
        (is (= "subscription_schedule" (:object sched)))
        (is (string? (:id sched)))))))

(deftest cancel-subscription-schedule-test
  (testing "Cancel subscription schedule"
    (let [response (schedules/cancel-subscription-schedule stripe-mock-client "ss_mock")]
      (is (map? response))
      (is (= "subscription_schedule" (:object response)))
      (is (string? (:id response))))))

(deftest release-subscription-schedule-test
  (testing "Release subscription schedule"
    (let [response (schedules/release-subscription-schedule stripe-mock-client "ss_mock")]
      (is (map? response))
      (is (= "subscription_schedule" (:object response)))
      (is (string? (:id response)))))) 