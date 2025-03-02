(ns stripe-clojure.mock.payouts-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.payouts :as payouts]))

(deftest create-payout-test
  (testing "Create a payout with valid parameters"
    (let [params {:amount 1000
                  :currency "usd"
                  :destination "acct_mock_123"}
          response (payouts/create-payout stripe-mock-client params)]
      (is (string? (:id response)) "Payout should have an id")
      (is (= "payout" (:object response))
          "Returned object should be 'payout'"))))

(deftest retrieve-payout-test
  (testing "Retrieve a payout by id"
    (let [dummy-id "po_mock_123"
          response (payouts/retrieve-payout stripe-mock-client dummy-id)]
      (is (= dummy-id (:id response)) "Retrieved payout id should match")
      (is (= "payout" (:object response))
          "Returned object should be 'payout'"))))

(deftest update-payout-test
  (testing "Update a payout with valid parameters"
    (let [dummy-id "po_mock_123"
          update-params {:metadata {:order "order123"}}
          response (payouts/update-payout stripe-mock-client dummy-id update-params)]
      (is (= dummy-id (:id response)) "Payout id should remain unchanged")
      (is (= "payout" (:object response))
          "Returned object should be 'payout'"))))

(deftest list-payouts-test
  (testing "List payouts with query parameters"
    (let [params {:limit 1}
          response (payouts/list-payouts stripe-mock-client params)]
      (is (map? response) "Response should be a map")
      (is (vector? (:data response))
          "Response :data should be a vector of payouts"))))

(deftest cancel-payout-test
  (testing "Cancel a pending payout"
    (let [dummy-id "po_mock_123"
          response (payouts/cancel-payout stripe-mock-client dummy-id)]
      (is (= dummy-id (:id response)) "Payout id should match")
      (is (= "payout" (:object response))
          "Returned object should be 'payout'"))))

(deftest reverse-payout-test
  (testing "Reverse a payout that has been sent"
    (let [dummy-id "po_mock_123"
          response (payouts/reverse-payout stripe-mock-client dummy-id)]
      (is (string? (:id response)) "Payout should have an id")
      (is (= "payout" (:object response))
          "Returned object should be 'payout'")))) 