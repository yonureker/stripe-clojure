(ns stripe-clojure.mock.climate.suppliers-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.climate.suppliers :as suppliers]))

(deftest ^:integration retrieve-supplier-test
  (testing "Retrieve a climate supplier using a dummy supplier id"
    (let [dummy-id "sup_mock"
          response (suppliers/retrieve-supplier stripe-client dummy-id)]
      (is (map? response))
      (is (= "climate.supplier" (:object response)))
      (when (:id response)
        (is (string? (:id response)))))))

(deftest ^:integration list-suppliers-test
  (testing "List climate suppliers using stripe‑mock"
    (let [response (suppliers/list-suppliers stripe-client)]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response))))))