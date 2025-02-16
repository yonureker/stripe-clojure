(ns stripe-clojure.mock.radar.value-list-items-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.radar.value-list-items :as vli]))

(deftest ^:integration create-value-list-item-test
  (testing "Create a radar value list item"
    (let [params {:value "test_value" :value_list "vl_mock"}
          response (vli/create-value-list-item stripe-client params)]
      (is (map? response))
      (is (= "radar.value_list_item" (:object response)))
      (is (string? (:id response)))
      (is (= "test_value" (:value response)))
      (is (= "vl_mock" (:value_list response))))))

(deftest ^:integration retrieve-value-list-item-test
  (testing "Retrieve a radar value list item"
    (let [response (vli/retrieve-value-list-item stripe-client "vli_mock")]
      (is (map? response))
      (is (= "radar.value_list_item" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :value))
      (is (string? (:value response))))))

(deftest ^:integration list-value-list-items-test
  (testing "List radar value list items"
    (let [response (vli/list-value-list-items stripe-client {:limit 3 :value_list "vl_mock"})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [item (:data response)]
        (is (map? item))
        (is (= "radar.value_list_item" (:object item)))
        (is (string? (:id item)))
        (is (contains? item :value))
        (is (string? (:value item)))))))

(deftest ^:integration delete-value-list-item-test
  (testing "Delete a radar value list item"
    (let [response (vli/delete-value-list-item stripe-client "vli_mock")]
      (is (map? response))
      (is (= "radar.value_list_item" (:object response)))
      (is (string? (:id response)))
      (is (true? (:deleted response)))))) 