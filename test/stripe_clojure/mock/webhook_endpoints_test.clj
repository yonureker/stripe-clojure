(ns stripe-clojure.mock.webhook-endpoints-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.webhook-endpoints :as webhook-endpoints]))

(deftest create-webhook-endpoint-test
  (testing "Create webhook endpoint"
    (let [params {:url "https://example.com/webhook"
                  :enabled_events ["charge.succeeded"]}
          response (webhook-endpoints/create-webhook-endpoint stripe-mock-client params)]
      (is (map? response))
      (is (= "webhook_endpoint" (:object response)))
      (is (string? (:id response)))
      (is (= "https://example.com/webhook" (:url response)))
      (is (vector? (:enabled_events response)))
      (is (= ["charge.succeeded"] (:enabled_events response))))))

(deftest retrieve-webhook-endpoint-test
  (testing "Retrieve webhook endpoint"
    (let [response (webhook-endpoints/retrieve-webhook-endpoint stripe-mock-client "we_mock")]
      (is (map? response))
      (is (= "webhook_endpoint" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :url))
      (is (string? (:url response))))))

(deftest update-webhook-endpoint-test
  (testing "Update webhook endpoint"
    (let [params {:url "https://example.com/new_webhook"}
          response (webhook-endpoints/update-webhook-endpoint stripe-mock-client "we_mock" params)]
      (is (map? response))
      (is (= "webhook_endpoint" (:object response)))
      (is (string? (:id response)))
      (is (= "https://example.com/new_webhook" (:url response))))))

(deftest list-webhook-endpoints-test
  (testing "List webhook endpoints"
    (let [response (webhook-endpoints/list-webhook-endpoints stripe-mock-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [we (:data response)]
        (is (map? we))
        (is (= "webhook_endpoint" (:object we)))
        (is (string? (:id we)))
        (is (contains? we :url))
        (is (string? (:url we)))))))

(deftest delete-webhook-endpoint-test
  (testing "Delete webhook endpoint"
    (let [response (webhook-endpoints/delete-webhook-endpoint stripe-mock-client "we_mock")]
      (is (map? response))
      (is (= "webhook_endpoint" (:object response)))
      (is (string? (:id response)))
      (is (true? (:deleted response)))))) 