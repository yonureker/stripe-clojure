(ns stripe-clojure.mock.reporting.report-types-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.reporting.report-types :as report-types]))

(deftest ^:integration retrieve-report-type-test
  (testing "Retrieve a report type"
    (let [response (report-types/retrieve-report-type stripe-client "rt_mock")]
      (is (map? response))
      (is (= "reporting.report_type" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :name))
      (is (string? (:name response))))))

(deftest ^:integration list-report-types-test
  (testing "List report types"
    (let [response (report-types/list-report-types stripe-client {})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [rt (:data response)]
        (is (map? rt))
        (is (= "reporting.report_type" (:object rt)))
        (is (string? (:id rt)))
        (is (contains? rt :name))
        (is (string? (:name rt))))))) 