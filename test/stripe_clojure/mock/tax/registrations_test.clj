(ns stripe-clojure.mock.tax.registrations-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.tax.registrations :as regs]))

(deftest create-registration-test
  (testing "Create tax registration"
    (let [params {:active_from "now"
                  :country "US"
                  :country_options {"us" {:state "CA" :type "state_sales_tax"}}}
          response (regs/create-registration stripe-mock-client params)]
      (is (map? response))
      (is (= "tax.registration" (:object response)))
      (is (string? (:id response)))
      (is (= "US" (:country response))))))

(deftest retrieve-registration-test
  (testing "Retrieve tax registration"
    (let [response (regs/retrieve-registration stripe-mock-client "reg_mock")]
      (is (map? response))
      (is (= "tax.registration" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :country)))))

(deftest update-registration-test
  (testing "Update tax registration"
    (let [response (regs/update-registration stripe-mock-client "reg_mock" {:expires_at "now"})]
      (is (map? response))
      (is (= "tax.registration" (:object response)))
      (is (string? (:id response))))))

(deftest list-registrations-test
  (testing "List tax registrations"
    (let [response (regs/list-registrations stripe-mock-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [reg (:data response)]
        (is (map? reg))
        (is (= "tax.registration" (:object reg)))
        (is (string? (:id reg))))))) 