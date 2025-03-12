(ns stripe-clojure.mock.billing.alerts-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.billing.alerts :as alerts]))

(deftest create-alert-test
  (testing "Create a new billing alert"
    (let [params {:title "API Request usage alert"
                  :alert_type "usage_threshold"}
          response (alerts/create-alert stripe-mock-client params)]
      (is (map? response))
      (is (= "billing.alert" (:object response)))
      (is (= (:name params) (:name response))))))

(deftest retrieve-alert-test
  (testing "Retrieve an existing billing alert"
    (let [params {:name "test_alert_"
                  :threshold 1500
                  :status "active"}
          created (alerts/create-alert stripe-mock-client params)
          alert-id (:id created)
          retrieved (alerts/retrieve-alert stripe-mock-client alert-id)]
      (is (= alert-id (:id retrieved))))))

(deftest list-alerts-test
  (testing "List billing alerts"
    (let [response (alerts/list-alerts stripe-mock-client {:limit 5})]
      (is (= "list" (:object response)))
      (is (vector? (:data response))))))

(deftest alert-status-change-test
  (testing "Activate, archive, then deactivate a billing alert"
    (let [params {:title "API Request usage alert"
                  :alert_type "usage_threshold"}
          created (alerts/create-alert stripe-mock-client params)
          activated (alerts/activate-alert stripe-mock-client "mock_123")
          archived (alerts/archive-alert stripe-mock-client "mock_123")
          deactivated (alerts/deactivate-alert stripe-mock-client "mock_123")]
      (is (:id created) "Expected created alert to have an id")
      (is (= "billing.alert" (:object created)) "Expected created alert's object to be 'billing.alert'")
      (is (:id activated) "Expected activated alert to have an id")
      (is (= "billing.alert" (:object activated)) "Expected activated alert's object to be 'billing.alert'")
      (is (:id archived) "Expected archived alert to have an id")
      (is (= "billing.alert" (:object archived)) "Expected archived alert's object to be 'billing.alert'")
      (is (:id deactivated) "Expected deactivated alert to have an id")
      (is (= "billing.alert" (:object deactivated)) "Expected deactivated alert's object to be 'billing.alert'"))))