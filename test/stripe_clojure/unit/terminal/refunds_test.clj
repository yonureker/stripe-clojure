(ns stripe-clojure.unit.terminal.refunds-test
  (:require [clojure.test :refer [deftest is testing]]
            [stripe-clojure.http.client :as client]
            [stripe-clojure.terminal.refunds :as refunds]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (testing "endpoint is correctly configured"
    (is (= "/v1/terminal/refunds"
           (:terminal-refunds config/stripe-endpoints)))))

(deftest create-refund-test
  (testing "create-refund calls request with correct params"
    (let [captured (atom nil)
          params {:payment_intent "pi_123" :amount 500}]
      (with-redefs [client/request
                    (fn [_client m e p o]
                      (reset! captured {:method m :endpoint e :params p :opts o})
                      {:id "mock_result"})]
        (refunds/create-refund :mock-client params))
      (is (= :post (:method @captured)))
      (is (= "/v1/terminal/refunds" (:endpoint @captured)))
      (is (= params (:params @captured)))
      (is (= {} (:opts @captured))))))

(deftest create-refund-with-opts-test
  (testing "create-refund forwards opts"
    (let [captured (atom nil)
          params {:payment_intent "pi_123" :amount 500}]
      (with-redefs [client/request
                    (fn [_client m e p o]
                      (reset! captured {:method m :endpoint e :params p :opts o})
                      {:id "mock_result"})]
        (refunds/create-refund :mock-client params {:idempotency-key "key_1"}))
      (is (= :post (:method @captured)))
      (is (= "/v1/terminal/refunds" (:endpoint @captured)))
      (is (= params (:params @captured)))
      (is (= {:idempotency-key "key_1"} (:opts @captured))))))
