(ns stripe-clojure.mock.test-helpers.treasury.outbound-payments-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.test-helpers.treasury.outbound-payments :as test-outbound-payments]))

(deftest update-outbound-payment-test
  (testing "Updates a test mode outbound payment with tracking details"
    (let [response (test-outbound-payments/update-outbound-payment stripe-mock-client "obp_mock" {:tracking_details {:type "ach" :ach {:trace_id "trace_123"}}})]
      (is (map? response))
      (is (= "treasury.outbound_payment" (:object response)))
      (is (string? (:id response))))))

(deftest fail-outbound-payment-test
  (testing "Transitions a test mode outbound payment to the failed status"
    (let [response (test-outbound-payments/fail-outbound-payment stripe-mock-client "obp_mock")]
      (is (map? response))
      (is (= "treasury.outbound_payment" (:object response)))
      (is (string? (:id response))))))

(deftest post-outbound-payment-test
  (testing "Transitions a test mode outbound payment to the posted status"
    (let [response (test-outbound-payments/post-outbound-payment stripe-mock-client "obp_mock")]
      (is (map? response))
      (is (= "treasury.outbound_payment" (:object response)))
      (is (string? (:id response))))))

(deftest return-outbound-payment-test
  (testing "Transitions a test mode outbound payment to the returned status"
    (let [response (test-outbound-payments/return-outbound-payment stripe-mock-client "obp_mock")]
      (is (map? response))
      (is (= "treasury.outbound_payment" (:object response)))
      (is (string? (:id response))))))
