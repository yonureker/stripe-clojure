(ns stripe-clojure.mock.products-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.products :as products]))

(deftest create-product-test
  (testing "Create a product with valid parameters"
    (let [params {:name "Test Product"}
          response (products/create-product stripe-client params)]
      (is (string? (:id response)) "Product should have an id")
      (is (= "product" (:object response))
          "Returned object should be 'product'"))))

(deftest retrieve-product-test
  (testing "Retrieve a product by id"
    (let [dummy-id "prod_mock_123"
          response (products/retrieve-product stripe-client dummy-id)]
      (is (= dummy-id (:id response)) "Retrieved product id should match")
      (is (= "product" (:object response))
          "Returned object should be 'product'"))))

(deftest update-product-test
  (testing "Update a product with valid parameters"
    (let [dummy-id "prod_mock_123"
          update-params {:metadata {:order "order123"}}
          response (products/update-product stripe-client dummy-id update-params)]
      (is (= dummy-id (:id response)) "Product id should remain unchanged")
      (is (= "product" (:object response))
          "Returned object should be 'product'"))))

(deftest list-products-test
  (testing "List all products with query parameters"
    (let [params {:limit 1}
          response (products/list-products stripe-client params)]
      (is (map? response) "Response should be a map")
      (is (vector? (:data response))
          "Response :data should be a vector of products"))))

(deftest delete-product-test
  (testing "Delete a product by id"
    (let [dummy-id "prod_mock_123"
          response (products/delete-product stripe-client dummy-id)]
      (is (= dummy-id (:id response)) "Deleted product id should match")
      (is (= "product" (:object response))
          "Returned object should be 'product'"))))

(deftest search-products-test
  (testing "Search for products with valid query parameters"
    (let [params {:query "name:'Test'"}
          response (products/search-products stripe-client params)]
      (is (map? response) "Response should be a map")
      (is (vector? (:data response))
          "Response :data should be a vector of products"))))

(deftest create-feature-test
  (testing "Create a product feature with valid parameters"
    (let [product-id "prod_mock_123"
          params {:entitlement_feature "feature_mock_123"}
          response (products/create-feature stripe-client product-id params)]
      (is (string? (:id response)) "Product feature should have an id")
      (is (= "product_feature" (:object response))
          "Returned object should be 'feature'"))))

(deftest retrieve-feature-test
  (testing "Retrieve a product feature by product id and feature id"
    (let [product-id "prod_mock_123"
          feature-id "feat_mock_123"
          response (products/retrieve-feature stripe-client product-id feature-id)]
      (is (= "product_feature" (:object response))
          "Returned object should be 'feature'"))))

(deftest list-features-test
  (testing "List features for a product with query parameters"
    (let [product-id "prod_mock_123"
          params {:limit 1}
          response (products/list-features stripe-client product-id params)]
      (is (map? response) "Response should be a map")
      (is (vector? (:data response))
          "Response :data should be a vector of features"))))

(deftest delete-feature-test
  (testing "Delete a product feature by product id and feature id"
    (let [product-id "prod_mock_123"
          feature-id "feat_mock_123"
          response (products/delete-feature stripe-client product-id feature-id)]
      (is (= "product_feature" (:object response))
          "Returned object should be 'feature'")))) 