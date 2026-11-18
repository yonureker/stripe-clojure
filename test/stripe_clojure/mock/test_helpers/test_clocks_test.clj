(ns stripe-clojure.mock.test-helpers.test-clocks-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.test-helpers.test-clocks :as test-test-clocks]))

(deftest create-test-clock-test
  (testing "Creates a new test clock"
    (let [response (test-test-clocks/create-test-clock stripe-mock-client {:frozen_time 1700000000})]
      (is (map? response))
      (is (= "test_helpers.test_clock" (:object response)))
      (is (string? (:id response))))))

(deftest retrieve-test-clock-test
  (testing "Retrieves a test clock"
    (let [response (test-test-clocks/retrieve-test-clock stripe-mock-client "clock_mock")]
      (is (map? response))
      (is (= "test_helpers.test_clock" (:object response)))
      (is (string? (:id response))))))

(deftest list-test-clocks-test
  (testing "Lists all test clocks"
    (let [response (test-test-clocks/list-test-clocks stripe-mock-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response))))))

(deftest delete-test-clock-test
  (testing "Deletes a test clock"
    (let [response (test-test-clocks/delete-test-clock stripe-mock-client "clock_mock")]
      (is (map? response))
      (is (= "test_helpers.test_clock" (:object response)))
      (is (string? (:id response))))))

(deftest advance-test-clock-test
  (testing "Starts advancing a test clock to a specified time in the future"
    (let [response (test-test-clocks/advance-test-clock stripe-mock-client "clock_mock" {:frozen_time 1700003600})]
      (is (map? response))
      (is (= "test_helpers.test_clock" (:object response)))
      (is (string? (:id response))))))
