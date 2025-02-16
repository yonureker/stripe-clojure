(ns stripe-clojure.mock.issuing.personalization-designs-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.issuing.personalization-designs :as pd]))

(deftest ^:integration create-personalization-design-test
  (testing "Create a new issuing personalization design with required parameters"
    (let [params   {:physical_bundle "mock"}
          response (pd/create-personalization-design stripe-client params)]
      (is (map? response))
      (is (= "issuing.personalization_design" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration retrieve-personalization-design-test
  (testing "Retrieve an issuing personalization design using a dummy design id"
    (let [dummy-id "pd_mock"
          response (pd/retrieve-personalization-design stripe-client dummy-id)]
      (is (map? response))
      (is (= "issuing.personalization_design" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration update-personalization-design-test
  (testing "Update an issuing personalization design with update parameters"
    (let [dummy-id "pd_mock"
          params   {:name "Updated name"}
          response (pd/update-personalization-design stripe-client dummy-id params)]
      (is (map? response))
      (is (= "issuing.personalization_design" (:object response)))
      (is (string? (:id response)))
      (is (= "Updated name" (:name response))))))

(deftest ^:integration list-personalization-designs-test
  (testing "List all issuing personalization designs"
    (let [response (pd/list-personalization-designs stripe-client {})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [item (:data response)]
        (is (map? item))
        (is (= "issuing.personalization_design" (:object item)))
        (is (string? (:id item))))))) 