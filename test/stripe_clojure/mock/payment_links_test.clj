(ns stripe-clojure.mock.payment-links-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.payment-links :as payment-links]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Payment Links – Valid Cases
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest create-payment-link-test
  (testing "Create a payment link with valid parameters"
    (let [params {:line_items [{:price "price_mock_123" :quantity 1}]}
          response (payment-links/create-payment-link stripe-client params)]
      (is (string? (:id response)) "Payment link should have an id")
      (is (= "payment_link" (:object response))
          "Returned object should be 'payment_link'"))))

(deftest retrieve-payment-link-test
  (testing "Retrieve a payment link with a given id"
    (let [plink-id "plink_mock_123"
          response (payment-links/retrieve-payment-link stripe-client plink-id)]
      (is (= plink-id (:id response))
          "Retrieved payment link id should match the passed id")
      (is (= "payment_link" (:object response))
          "Returned object should be 'payment_link'"))))

(deftest update-payment-link-test
  (testing "Update a payment link"
    (let [plink-id "plink_mock_123"
          update-params {:metadata {"order" "order123"}}
          response (payment-links/update-payment-link stripe-client plink-id update-params)]
      (is (= plink-id (:id response))
          "Payment link id should remain unchanged"))))

(deftest list-payment-links-test
  (testing "List all payment links with query parameters"
    (let [params {:limit 1}
          response (payment-links/list-payment-links stripe-client params)]
      (is (map? response)
          "Response should be a map")
      (is (vector? (:data response))
          "Response :data should be a vector of payment links"))))

(deftest list-line-items-test
  (testing "List line items for a specific payment link"
    (let [plink-id "plink_mock_123"
          params {:limit 1}
          response (payment-links/list-line-items stripe-client plink-id params)]
      (is (map? response)
          "Response should be a map")
      (is (vector? (:data response))
          "Response :data should be a vector of line items"))))