(ns stripe-clojure.mock.test-helpers.treasury.inbound-transfers-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.test-helpers.treasury.inbound-transfers :as test-inbound-transfers]))

(deftest fail-inbound-transfer-test
  (testing "Transitions a test mode inbound transfer to the failed status"
    (let [response (test-inbound-transfers/fail-inbound-transfer stripe-mock-client "ibt_mock")]
      (is (map? response))
      (is (= "treasury.inbound_transfer" (:object response)))
      (is (string? (:id response))))))

(deftest return-inbound-transfer-test
  (testing "Marks a test mode inbound transfer as returned"
    (let [response (test-inbound-transfers/return-inbound-transfer stripe-mock-client "ibt_mock")]
      (is (map? response))
      (is (= "treasury.inbound_transfer" (:object response)))
      (is (string? (:id response))))))

(deftest succeed-inbound-transfer-test
  (testing "Transitions a test mode inbound transfer to the succeeded status"
    (let [response (test-inbound-transfers/succeed-inbound-transfer stripe-mock-client "ibt_mock")]
      (is (map? response))
      (is (= "treasury.inbound_transfer" (:object response)))
      (is (string? (:id response))))))
