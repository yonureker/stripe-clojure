(ns stripe-clojure.mock.setup-intents-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.setup-intents :as setup-intents]))

(deftest create-setup-intent-test
  (testing "Create setup intent"
    (let [params {:customer "cus_mock" :payment_method "pm_mock"}
          response (setup-intents/create-setup-intent stripe-mock-client params)]
      (is (map? response))
      (is (= "setup_intent" (:object response)))
      (is (string? (:id response)))
      (is (= "cus_mock" (:customer response)))
      (is (= "pm_mock" (:payment_method response))))))

(deftest retrieve-setup-intent-test
  (testing "Retrieve setup intent"
    (let [response (setup-intents/retrieve-setup-intent stripe-mock-client "si_mock")]
      (is (map? response))
      (is (= "setup_intent" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :customer)))))

(deftest update-setup-intent-test
  (testing "Update setup intent"
    (let [params {:description "Updated setup intent"}
          response (setup-intents/update-setup-intent stripe-mock-client "si_mock" params)]
      (is (map? response))
      (is (= "setup_intent" (:object response)))
      (is (string? (:id response)))
      (is (= "Updated setup intent" (:description response))))))

(deftest confirm-setup-intent-test
  (testing "Confirm setup intent"
    (let [params {:payment_method "pm_confirm"}
          response (setup-intents/confirm-setup-intent stripe-mock-client "si_mock" params)]
      (is (map? response))
      (is (= "setup_intent" (:object response)))
      (is (string? (:id response)))
      (is (= "pm_confirm" (:payment_method response))))))

(deftest cancel-setup-intent-test
  (testing "Cancel setup intent"
    (let [response (setup-intents/cancel-setup-intent stripe-mock-client "si_mock")]
      (is (map? response))
      (is (= "setup_intent" (:object response)))
      (is (string? (:id response))))))

(deftest list-setup-intents-test
  (testing "List setup intents"
    (let [response (setup-intents/list-setup-intents stripe-mock-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [si (:data response)]
        (is (map? si))
        (is (= "setup_intent" (:object si)))
        (is (string? (:id si)))))))

(deftest verify-microdeposits-test
  (testing "Verify microdeposits on setup intent"
    (let [params {:amounts [32 45]}
          response (setup-intents/verify-microdeposits stripe-mock-client "si_mock" params)]
      (is (map? response))
      (is (= "setup_intent" (:object response)))
      (is (string? (:id response)))))) 