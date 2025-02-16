(ns stripe-clojure.mock.prices-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.prices :as prices]))

(deftest create-price-test
  (testing "Create a price with valid parameters"
    (let [params {:unit_amount 1500
                  :currency "usd"
                  :product "prod_mock_123"
                  :recurring {:interval "month"}}
          response (prices/create-price stripe-client params)]
      (is (string? (:id response)) "Price should have an id")
      (is (= "price" (:object response))
          "Returned object should be 'price'"))))

(deftest retrieve-price-test
  (testing "Retrieve a price by id"
    (let [dummy-id "price_mock_123"
          response (prices/retrieve-price stripe-client dummy-id)]
      (is (= dummy-id (:id response)) "Retrieved price id should match")
      (is (= "price" (:object response))
          "Returned object should be 'price'"))))

(deftest update-price-test
  (testing "Update a price with valid parameters"
    (let [dummy-id "price_mock_123"
          update-params {:metadata {:order "order123"}}
          response (prices/update-price stripe-client dummy-id update-params)]
      (is (= dummy-id (:id response)) "Price id should remain unchanged")
      (is (= "price" (:object response))
          "Returned object should be 'price'"))))

(deftest list-prices-test
  (testing "List all prices with query parameters"
    (let [params {:limit 1}
          response (prices/list-prices stripe-client params)]
      (is (map? response) "Response should be a map")
      (is (vector? (:data response))
          "Response :data should be a vector of prices"))))

(deftest search-prices-test
  (testing "Search for prices with valid query parameters"
    (let [params {:query "currency:'usd'"}
          response (prices/search-prices stripe-client params)]
      (is (map? response) "Response should be a map")
      (is (vector? (:data response))
          "Response :data should be a vector of prices")))) 