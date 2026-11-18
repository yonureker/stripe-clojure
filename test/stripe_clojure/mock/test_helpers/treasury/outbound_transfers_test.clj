(ns stripe-clojure.mock.test-helpers.treasury.outbound-transfers-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.test-helpers.treasury.outbound-transfers :as test-outbound-transfers]))

(deftest update-outbound-transfer-test
  (testing "Updates a test mode outbound transfer with tracking details"
    (let [response (test-outbound-transfers/update-outbound-transfer stripe-mock-client "obt_mock" {:tracking_details {:type "ach" :ach {:trace_id "trace_123"}}})]
      (is (map? response))
      (is (= "treasury.outbound_transfer" (:object response)))
      (is (string? (:id response))))))

(deftest fail-outbound-transfer-test
  (testing "Transitions a test mode outbound transfer to the failed status"
    (let [response (test-outbound-transfers/fail-outbound-transfer stripe-mock-client "obt_mock")]
      (is (map? response))
      (is (= "treasury.outbound_transfer" (:object response)))
      (is (string? (:id response))))))

(deftest post-outbound-transfer-test
  (testing "Transitions a test mode outbound transfer to the posted status"
    (let [response (test-outbound-transfers/post-outbound-transfer stripe-mock-client "obt_mock")]
      (is (map? response))
      (is (= "treasury.outbound_transfer" (:object response)))
      (is (string? (:id response))))))

(deftest return-outbound-transfer-test
  (testing "Transitions a test mode outbound transfer to the returned status"
    (let [response (test-outbound-transfers/return-outbound-transfer stripe-mock-client "obt_mock")]
      (is (map? response))
      (is (= "treasury.outbound_transfer" (:object response)))
      (is (string? (:id response))))))
