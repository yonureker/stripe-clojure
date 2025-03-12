(ns stripe-clojure.mock.entitlements.active-entitlements-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.entitlements.active-entitlements :as active-entitlements]))

(deftest retrieve-active-entitlement-test
  (testing "Retrieve an active entitlement using a dummy id"
    (let [dummy-id "ae_mock"
          response (active-entitlements/retrieve-active-entitlement stripe-mock-client dummy-id)]
      (is (map? response))
      (is (= "entitlements.active_entitlement" (:object response)))
      (when (:id response)
        (is (string? (:id response)))))))

(deftest list-active-entitlements-test
  (testing "List active entitlements using stripe‑mock"
    (let [response (active-entitlements/list-active-entitlements stripe-mock-client {:customer "cus_mock"})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))))) 