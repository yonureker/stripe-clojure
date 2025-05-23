(ns stripe-clojure.mock.billing.credit-grants-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.billing.credit-grants :as credit-grants]))

(deftest create-credit-grant-test
  (testing "Create credit grant using stripe‑mock with required params"
    (let [params {:name "Purchased Credits"
                  :customer "cus_mock"
                  :amount {:monetary {:currency "usd" :value 1000}
                           :type "monetary"}
                  :applicability_config {:scope {:price_type "metered"}}
                  :category "paid"}
          response (credit-grants/create-credit-grant stripe-mock-client params)]
      (is (map? response))
      (is (= "billing.credit_grant" (:object response)))
      ;; Optionally, verify that the response echoes back the passed values
      (is (= "Purchased Credits" (:name response)))
      (is (= "cus_mock" (:customer response))))))

(deftest retrieve-credit-grant-test
  (testing "Retrieve credit grant using dummy id"
    (let [dummy-id "cg_mock"
          response (credit-grants/retrieve-credit-grant stripe-mock-client dummy-id)]
      (is (map? response))
      (is (= "billing.credit_grant" (:object response)))
      (is (= dummy-id (:id response))))))

(deftest update-credit-grant-test
  (testing "Update credit grant using stripe‑mock"
    (let [dummy-id "cg_mock"
          params {:metadata {:cost_basis "0.9"}
                  :expires_at 1759302000}
          response (credit-grants/update-credit-grant stripe-mock-client dummy-id params)]
      (is (map? response))
      (is (= "billing.credit_grant" (:object response)))
      (is (= dummy-id (:id response))))))

(deftest list-credit-grants-test
  (testing "List credit grants using dummy customer id"
    (let [response (credit-grants/list-credit-grants stripe-mock-client {:customer "cus_mock"})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response))))))

(deftest expire-credit-grant-test
  (testing "Expire credit grant using stripe‑mock"
    (let [dummy-id "cg_mock"
          response (credit-grants/expire-credit-grant stripe-mock-client dummy-id)]
      (is (map? response))
      (is (= "billing.credit_grant" (:object response))))))

(deftest void-credit-grant-test
  (testing "Void credit grant using stripe‑mock"
    (let [dummy-id "cg_mock"
          response (credit-grants/void-credit-grant stripe-mock-client dummy-id)]
      (is (map? response))
      (is (= "billing.credit_grant" (:object response)))
      (is (= dummy-id (:id response))))))