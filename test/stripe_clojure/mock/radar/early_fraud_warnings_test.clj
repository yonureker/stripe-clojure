(ns stripe-clojure.mock.radar.early-fraud-warnings-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.radar.early-fraud-warnings :as efw]))

(deftest retrieve-early-fraud-warning-test
  (testing "Retrieve early fraud warning"
    (let [response (efw/retrieve-early-fraud-warning stripe-mock-client "efw_mock")]
      (is (map? response))
      (is (= "radar.early_fraud_warning" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :charge))
      (is (string? (:charge response)))
      (is (contains? response :created))
      (is (number? (:created response))))))

(deftest list-early-fraud-warnings-test
  (testing "List early fraud warnings"
    (let [response (efw/list-early-fraud-warnings stripe-mock-client {})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [warning (:data response)]
        (is (map? warning))
        (is (= "radar.early_fraud_warning" (:object warning)))
        (is (string? (:id warning)))
        (is (contains? warning :charge))
        (is (string? (:charge warning)))
        (is (contains? warning :created))
        (is (number? (:created warning))))))) 