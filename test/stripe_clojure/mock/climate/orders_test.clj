(ns stripe-clojure.mock.climate.orders-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.climate.orders :as orders]))

(deftest create-order-test
  (testing "Create climate order using stripe‑mock with required parameters"
    (let [params {:product "prod_mock"}
          response (orders/create-order stripe-mock-client params)]
      (is (map? response))
      (is (= "climate.order" (:object response)))
      (when (:id response)
        (is (string? (:id response)))))))

(deftest retrieve-order-test
  (testing "Retrieve climate order using a dummy id"
    (let [dummy-id "order_mock"
          response (orders/retrieve-order stripe-mock-client dummy-id)]
      (is (map? response))
      (is (= "climate.order" (:object response)))
      (when (:id response)
        (is (string? (:id response)))))))

(deftest update-order-test
  (testing "Update climate order using stripe‑mock"
    (let [dummy-id "order_mock"
          params {:metadata {:description "Updated Order"}}
          response (orders/update-order stripe-mock-client dummy-id params)]
      (is (map? response))
      (is (= "climate.order" (:object response)))
      (when (:id response)
        (is (string? (:id response)))))))

(deftest list-orders-test
  (testing "List climate orders using stripe‑mock"
    (let [response (orders/list-orders stripe-mock-client)]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response))))))

(deftest cancel-order-test
  (testing "Cancel climate order using stripe‑mock"
    (let [dummy-id "order_mock"
          response (orders/cancel-order stripe-mock-client dummy-id)]
      (is (map? response))
      (is (= "climate.order" (:object response)))
      (when (:id response)
        (is (string? (:id response)))))))