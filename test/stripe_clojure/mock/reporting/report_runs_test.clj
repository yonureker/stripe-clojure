(ns stripe-clojure.mock.reporting.report-runs-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.reporting.report-runs :as rr]))

(deftest create-report-run-test
  (testing "Create report run"
    (let [params {:report_type "balance.summary.1"}
          response (rr/create-report-run stripe-mock-client params)]
      (is (map? response))
      (is (= "reporting.report_run" (:object response)))
      (is (string? (:id response)))
      (is (= "balance.summary.1" (:report_type response))))))

(deftest retrieve-report-run-test
  (testing "Retrieve report run"
    (let [response (rr/retrieve-report-run stripe-mock-client "rr_mock")]
      (is (map? response))
      (is (= "reporting.report_run" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :report_type)))))

(deftest list-report-runs-test
  (testing "List report runs"
    (let [response (rr/list-report-runs stripe-mock-client {})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [item (:data response)]
        (is (map? item))
        (is (= "reporting.report_run" (:object item)))
        (is (string? (:id item)))
        (is (contains? item :report_type)))))) 