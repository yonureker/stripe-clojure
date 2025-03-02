(ns stripe-clojure.mock.climate.suppliers-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.climate.suppliers :as suppliers]))

(deftest retrieve-supplier-test
  (testing "Retrieve a climate supplier using a dummy supplier id"
    (let [dummy-id "sup_mock"
          response (suppliers/retrieve-supplier stripe-mock-client dummy-id)]
      (is (map? response))
      (is (= "climate.supplier" (:object response)))
      (when (:id response)
        (is (string? (:id response)))))))

(deftest list-suppliers-test
  (testing "List climate suppliers using stripe‑mock"
    (let [response (suppliers/list-suppliers stripe-mock-client)]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response))))))