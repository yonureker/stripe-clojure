(ns stripe-clojure.mock.terminal.configurations-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.terminal.configurations :as tc]))

(deftest create-configuration-test
  (testing "Create terminal configuration"
    (let [params {:name "Test Config"}
          response (tc/create-configuration stripe-mock-client params)]
      (is (map? response))
      (is (= "terminal.configuration" (:object response)))
      (is (string? (:id response)))
      (is (= "Test Config" (:name response))))))

(deftest retrieve-configuration-test
  (testing "Retrieve terminal configuration"
    (let [response (tc/retrieve-configuration stripe-mock-client "tc_mock")]
      (is (map? response))
      (is (= "terminal.configuration" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :name)))))

(deftest update-configuration-test
  (testing "Update terminal configuration"
    (let [params {:name "test"}
          response (tc/update-configuration stripe-mock-client "tc_mock" params)]
      (is (map? response))
      (is (= "terminal.configuration" (:object response)))
      (is (string? (:id response))))))

(deftest list-configurations-test
  (testing "List terminal configurations"
    (let [response (tc/list-configurations stripe-mock-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [config (:data response)]
        (is (map? config))
        (is (= "terminal.configuration" (:object config)))
        (is (string? (:id config)))
        (is (contains? config :name))))))

(deftest delete-configuration-test
  (testing "Delete terminal configuration"
    (let [response (tc/delete-configuration stripe-mock-client "tc_mock")]
      (is (map? response))
      (is (= "terminal.configuration" (:object response)))
      (is (string? (:id response)))
      (is (true? (:deleted response))))))