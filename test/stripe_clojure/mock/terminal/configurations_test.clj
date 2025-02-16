(ns stripe-clojure.mock.terminal.configurations-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.terminal.configurations :as tc]))

(deftest ^:integration create-configuration-test
  (testing "Create terminal configuration"
    (let [params {:name "Test Config"}
          response (tc/create-configuration stripe-client params)]
      (is (map? response))
      (is (= "terminal.configuration" (:object response)))
      (is (string? (:id response)))
      (is (= "Test Config" (:name response))))))

(deftest ^:integration retrieve-configuration-test
  (testing "Retrieve terminal configuration"
    (let [response (tc/retrieve-configuration stripe-client "tc_mock")]
      (is (map? response))
      (is (= "terminal.configuration" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :name)))))

(deftest ^:integration update-configuration-test
  (testing "Update terminal configuration"
    (let [params {:name "test"}
          response (tc/update-configuration stripe-client "tc_mock" params)]
      (is (map? response))
      (is (= "terminal.configuration" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration list-configurations-test
  (testing "List terminal configurations"
    (let [response (tc/list-configurations stripe-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [config (:data response)]
        (is (map? config))
        (is (= "terminal.configuration" (:object config)))
        (is (string? (:id config)))
        (is (contains? config :name))))))

(deftest ^:integration delete-configuration-test
  (testing "Delete terminal configuration"
    (let [response (tc/delete-configuration stripe-client "tc_mock")]
      (is (map? response))
      (is (= "terminal.configuration" (:object response)))
      (is (string? (:id response)))
      (is (true? (:deleted response))))))