(ns stripe-clojure.mock.billing-portal.configurations-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.billing-portal.configurations :as configurations]))

(deftest create-configuration-test
  (testing "Create configuration using stripe‑mock with required params"
    (let [params {:default_return_url "https://example.com"
                  :features {:customer_update {:enabled true}
                             :invoice_history {:enabled true}}}
          response (configurations/create-configuration stripe-mock-client params)]
      (is (map? response))
      (is (= "billing_portal.configuration" (:object response)))
      (is (= "https://example.com" (:default_return_url response)))
      ;; Since stripe‑mock may not echo back our input id, we simply check if an id is present and is a string when provided.
      (when (:id response)
        (is (string? (:id response)))))))

(deftest retrieve-configuration-test
  (testing "Retrieve configuration using dummy id"
    (let [dummy-id "pc_mock"
          response (configurations/retrieve-configuration stripe-mock-client dummy-id)]
      (is (map? response))
      (is (= "billing_portal.configuration" (:object response)))
      (when (:id response)
        (is (string? (:id response)))))))

(deftest update-configuration-test
  (testing "Update configuration using stripe‑mock"
    (let [dummy-id "pc_mock"
          params {:default_return_url "https://updated.com"}
          response (configurations/update-configuration stripe-mock-client dummy-id params)]
      (is (map? response))
      (is (= "billing_portal.configuration" (:object response)))
      (is (= "https://updated.com" (:default_return_url response)))
      (when (:id response)
        (is (string? (:id response)))))))

(deftest list-configurations-test
  (testing "List configurations using stripe‑mock"
    (let [response (configurations/list-configurations stripe-mock-client)]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))))) 