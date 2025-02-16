(ns stripe-clojure.mock.climate.orders-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.climate.orders :as orders]))

(deftest ^:integration create-order-test
  (testing "Create climate order using stripe‑mock with required parameters"
    (let [params {:product "prod_mock"}
          response (orders/create-order stripe-client params)]
      (is (map? response))
      (is (= "climate.order" (:object response)))
      (when (:id response)
        (is (string? (:id response)))))))

(deftest ^:integration retrieve-order-test
  (testing "Retrieve climate order using a dummy id"
    (let [dummy-id "order_mock"
          response (orders/retrieve-order stripe-client dummy-id)]
      (is (map? response))
      (is (= "climate.order" (:object response)))
      (when (:id response)
        (is (string? (:id response)))))))

(deftest ^:integration update-order-test
  (testing "Update climate order using stripe‑mock"
    (let [dummy-id "order_mock"
          params {:metadata {:description "Updated Order"}}
          response (orders/update-order stripe-client dummy-id params)]
      (is (map? response))
      (is (= "climate.order" (:object response)))
      (when (:id response)
        (is (string? (:id response)))))))

(deftest ^:integration list-orders-test
  (testing "List climate orders using stripe‑mock"
    (let [response (orders/list-orders stripe-client)]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response))))))

(deftest ^:integration cancel-order-test
  (testing "Cancel climate order using stripe‑mock"
    (let [dummy-id "order_mock"
          response (orders/cancel-order stripe-client dummy-id)]
      (is (map? response))
      (is (= "climate.order" (:object response)))
      (when (:id response)
        (is (string? (:id response)))))))