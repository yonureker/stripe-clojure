(ns stripe-clojure.mock.subscription-items-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.subscription-items :as subitems]))

(deftest create-subscription-item-test
  (testing "Create subscription item"
    (let [params {:subscription "sub_mock"}
          response (subitems/create-subscription-item stripe-mock-client params)]
      (is (map? response))
      (is (= "subscription_item" (:object response)))
      (is (string? (:id response)))
      (is (= "sub_mock" (:subscription response))))))

(deftest retrieve-subscription-item-test
  (testing "Retrieve subscription item"
    (let [response (subitems/retrieve-subscription-item stripe-mock-client "si_mock")]
      (is (map? response))
      (is (= "subscription_item" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :subscription))
      (is (string? (:subscription response))))))

(deftest update-subscription-item-test
  (testing "Update subscription item"
    (let [params {:quantity 100}
          response (subitems/update-subscription-item stripe-mock-client "si_mock" params)]
      (is (map? response))
      (is (= "subscription_item" (:object response)))
      (is (string? (:id response)))
      (is (= 100 (:quantity response))))))

(deftest list-subscription-items-test
  (testing "List subscription items"
    (let [response (subitems/list-subscription-items stripe-mock-client {:limit 2 :subscription "sub_mock"})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [item (:data response)]
        (is (map? item))
        (is (= "subscription_item" (:object item)))
        (is (string? (:id item)))
        (is (contains? item :subscription))
        (is (string? (:subscription item)))))))

(deftest delete-subscription-item-test
  (testing "Delete subscription item"
    (let [response (subitems/delete-subscription-item stripe-mock-client "si_mock")]
      (is (map? response))
      (is (= "subscription_item" (:object response)))
      (is (string? (:id response)))
      (is (true? (:deleted response))))))

(deftest create-usage-record-test
  (testing "Create usage record"
    (let [params {:quantity 100}
          response (subitems/create-usage-record stripe-mock-client "si_mock" params)]
      (is (map? response))
      (is (= "usage_record" (:object response)))
      (is (string? (:id response)))
      (is (= 100 (:quantity response))))))

(deftest list-usage-record-summaries-test
  (testing "List usage record summaries"
    (let [response (subitems/list-usage-record-summaries stripe-mock-client "si_mock" {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [summary (:data response)]
        (is (map? summary))
        (is (= "usage_record_summary" (:object summary)))
        (is (string? (:id summary))))))) 