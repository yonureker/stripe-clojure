(ns stripe-clojure.mock.sigma.scheduled-query-runs-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.sigma.scheduled-query-runs :as sqr]))

(deftest ^:integration retrieve-scheduled-query-run-test
  (testing "Retrieve scheduled query run"
    (let [response (sqr/retrieve-scheduled-query-run stripe-client "sqrun_mock")]
      (is (map? response))
      (is (= "scheduled_query_run" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :created))
      (is (number? (:created response))))))

(deftest ^:integration list-scheduled-query-runs-test
  (testing "List scheduled query runs"
    (let [response (sqr/list-scheduled-query-runs stripe-client {})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [item (:data response)]
        (is (map? item))
        (is (= "scheduled_query_run" (:object item)))
        (is (string? (:id item)))
        (is (contains? item :created))
        (is (number? (:created item))))))) 