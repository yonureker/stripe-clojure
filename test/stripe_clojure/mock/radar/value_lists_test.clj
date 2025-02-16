(ns stripe-clojure.mock.radar.value-lists-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.radar.value-lists :as vl]))

(deftest ^:integration create-value-list-test
  (testing "Create a radar value list"
    (let [params   {:name "Test Value List" :alias "test"}
          response (vl/create-value-list stripe-client params)]
      (is (map? response))
      (is (= "radar.value_list" (:object response)))
      (is (string? (:id response)))
      (is (= "Test Value List" (:name response))))))

(deftest ^:integration retrieve-value-list-test
  (testing "Retrieve a radar value list"
    (let [response (vl/retrieve-value-list stripe-client "vl_mock")]
      (is (map? response))
      (is (= "radar.value_list" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :name))
      (is (string? (:name response))))))

(deftest ^:integration update-value-list-test
  (testing "Update a radar value list"
    (let [params   {:metadata {:updated "true"}}
          response (vl/update-value-list stripe-client "vl_mock" params)]
      (is (map? response))
      (is (= "radar.value_list" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration list-value-lists-test
  (testing "List radar value lists"
    (let [response (vl/list-value-lists stripe-client {})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [list-item (:data response)]
        (is (map? list-item))
        (is (= "radar.value_list" (:object list-item)))
        (is (string? (:id list-item)))
        (is (contains? list-item :name))
        (is (string? (:name list-item)))))))

(deftest ^:integration delete-value-list-test
  (testing "Delete a radar value list"
    (let [response (vl/delete-value-list stripe-client "vl_mock")]
      (is (map? response))
      (is (= "radar.value_list" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :deleted))
      (is (true? (:deleted response)))))) 