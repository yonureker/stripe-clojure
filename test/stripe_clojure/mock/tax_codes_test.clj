(ns stripe-clojure.mock.tax-codes-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.tax-codes :as tax-codes]))

(deftest retrieve-tax-code-test
  (testing "Retrieve tax code"
    (let [response (tax-codes/retrieve-tax-code stripe-mock-client "txc_mock")]
      (is (map? response))
      (is (= "tax_code" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :description))
      (is (string? (:description response))))))

(deftest list-tax-codes-test
  (testing "List tax codes"
    (let [response (tax-codes/list-tax-codes stripe-mock-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [tc (:data response)]
        (is (map? tc))
        (is (= "tax_code" (:object tc)))
        (is (string? (:id tc)))
        (is (contains? tc :description))
        (is (string? (:description tc))))))) 