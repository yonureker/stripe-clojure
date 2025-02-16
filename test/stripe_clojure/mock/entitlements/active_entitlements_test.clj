(ns stripe-clojure.mock.entitlements.active-entitlements-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.entitlements.active-entitlements :as active-entitlements]))

(deftest ^:integration retrieve-active-entitlement-test
  (testing "Retrieve an active entitlement using a dummy id"
    (let [dummy-id "ae_mock"
          response (active-entitlements/retrieve-active-entitlement stripe-client dummy-id)]
      (is (map? response))
      (is (= "entitlements.active_entitlement" (:object response)))
      (when (:id response)
        (is (string? (:id response)))))))

(deftest ^:integration list-active-entitlements-test
  (testing "List active entitlements using stripe‑mock"
    (let [response (active-entitlements/list-active-entitlements stripe-client {:customer "cus_mock"})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))))) 