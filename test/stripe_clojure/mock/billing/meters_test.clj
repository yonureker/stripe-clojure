(ns stripe-clojure.mock.billing.meters-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.billing.meters :as meters]))

(deftest ^:integration create-meter-test
  (testing "Create meter using stripe‑mock with required params"
    (let [params {:display_name "Search API Calls"
                  :event_name "ai_search_api"
                  :default_aggregation {:formula "sum"}}
          response (meters/create-meter stripe-client params)]
      (is (map? response))
      (is (= "billing.meter" (:object response)))
      (is (= "Search API Calls" (:display_name response)))
      (is (= "ai_search_api" (:event_name response)))
      (is (string? (:id response))))))

(deftest ^:integration retrieve-meter-test
  (testing "Retrieve meter using dummy id"
    (let [dummy-id "meter_mock"
          response (meters/retrieve-meter stripe-client dummy-id)]
      (is (map? response))
      (is (= "billing.meter" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration update-meter-test
  (testing "Update meter using stripe‑mock"
    (let [dummy-id "meter_mock"
          params {:display_name "Updated Meter"}
          response (meters/update-meter stripe-client dummy-id params)]
      (is (map? response))
      (is (= "billing.meter" (:object response)))
      (is (string? (:id response)))
      (is (= "Updated Meter" (:display_name response))))))

(deftest ^:integration list-meters-test
  (testing "List meters using stripe‑mock"
    (let [response (meters/list-meters stripe-client)]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response))))))

(deftest ^:integration deactivate-meter-test
  (testing "Deactivate meter using stripe‑mock"
    (let [dummy-id "meter_mock"
          response (meters/deactivate-meter stripe-client dummy-id)]
      (is (map? response))
      (is (= "billing.meter" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration reactivate-meter-test
  (testing "Reactivate meter using stripe‑mock"
    (let [dummy-id "meter_mock"
          response (meters/reactivate-meter stripe-client dummy-id)]
      (is (map? response))
      (is (= "billing.meter" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration list-event-summaries-test
  (testing "List event summaries for a meter using stripe‑mock"
    (let [dummy-id "meter_mock"
          response (meters/list-event-summaries stripe-client dummy-id {:customer "cus_mock"
                                                                        :end_time 1722666800
                                                                        :start_time 1722666800})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))))) 