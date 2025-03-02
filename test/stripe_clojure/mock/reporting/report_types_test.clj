(ns stripe-clojure.mock.reporting.report-types-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.reporting.report-types :as report-types]))

(deftest retrieve-report-type-test
  (testing "Retrieve a report type"
    (let [response (report-types/retrieve-report-type stripe-mock-client "rt_mock")]
      (is (map? response))
      (is (= "reporting.report_type" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :name))
      (is (string? (:name response))))))

(deftest list-report-types-test
  (testing "List report types"
    (let [response (report-types/list-report-types stripe-mock-client {})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [rt (:data response)]
        (is (map? rt))
        (is (= "reporting.report_type" (:object rt)))
        (is (string? (:id rt)))
        (is (contains? rt :name))
        (is (string? (:name rt))))))) 