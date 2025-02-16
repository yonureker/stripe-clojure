(ns stripe-clojure.mock.issuing.cardholders-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.issuing.cardholders :as cardholders]))

(deftest ^:integration create-cardholder-test
  (testing "Create a new issuing cardholder using stripe‑mock with required parameters"
    (let [params {:name  "Test Cardholder"
                  :billing {:address {:country "US"
                                      :city "San Francisco"
                                      :line1 "123 Main St"
                                      :postal_code "94101"
                                      :state "CA"}}}
          response (cardholders/create-cardholder stripe-client params)]
      (is (map? response))
      (is (= "issuing.cardholder" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration retrieve-cardholder-test
  (testing "Retrieve an issuing cardholder using a dummy id"
    (let [dummy-id "cardholder_mock"
          response (cardholders/retrieve-cardholder stripe-client dummy-id)]
      (is (map? response))
      (is (= "issuing.cardholder" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration update-cardholder-test
  (testing "Update an issuing cardholder using stripe‑mock with update parameters"
    (let [dummy-id "cardholder_mock"
          params {:metadata {:updated "true"}}
          response (cardholders/update-cardholder stripe-client dummy-id params)]
      (is (map? response))
      (is (= "issuing.cardholder" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration list-cardholders-test
  (testing "List issuing cardholders using stripe‑mock"
    (let [response (cardholders/list-cardholders stripe-client {})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [ch (:data response)]
        (is (map? ch))
        (is (string? (:id ch))))))) 