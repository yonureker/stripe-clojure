(ns stripe-clojure.mock.test-helpers.issuing.cards-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.test-helpers.issuing.cards :as test-cards]))

(deftest deliver-card-test
  (testing "Updates the shipping status of a card to delivered"
    (let [response (test-cards/deliver-card stripe-mock-client "ic_mock")]
      (is (map? response))
      (is (= "issuing.card" (:object response)))
      (is (string? (:id response))))))

(deftest fail-card-test
  (testing "Updates the shipping status of a card to failure"
    (let [response (test-cards/fail-card stripe-mock-client "ic_mock")]
      (is (map? response))
      (is (= "issuing.card" (:object response)))
      (is (string? (:id response))))))

(deftest return-card-test
  (testing "Updates the shipping status of a card to returned"
    (let [response (test-cards/return-card stripe-mock-client "ic_mock")]
      (is (map? response))
      (is (= "issuing.card" (:object response)))
      (is (string? (:id response))))))

(deftest ship-card-test
  (testing "Updates the shipping status of a card to shipped"
    (let [response (test-cards/ship-card stripe-mock-client "ic_mock")]
      (is (map? response))
      (is (= "issuing.card" (:object response)))
      (is (string? (:id response))))))

(deftest submit-card-test
  (testing "Updates the shipping status of a card to submitted"
    (let [response (test-cards/submit-card stripe-mock-client "ic_mock")]
      (is (map? response))
      (is (= "issuing.card" (:object response)))
      (is (string? (:id response))))))
