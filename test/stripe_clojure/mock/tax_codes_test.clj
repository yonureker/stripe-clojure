(ns stripe-clojure.mock.tax-codes-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.tax-codes :as tax-codes]))

(deftest ^:integration retrieve-tax-code-test
  (testing "Retrieve tax code"
    (let [response (tax-codes/retrieve-tax-code stripe-client "txc_mock")]
      (is (map? response))
      (is (= "tax_code" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :description))
      (is (string? (:description response))))))

(deftest ^:integration list-tax-codes-test
  (testing "List tax codes"
    (let [response (tax-codes/list-tax-codes stripe-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [tc (:data response)]
        (is (map? tc))
        (is (= "tax_code" (:object tc)))
        (is (string? (:id tc)))
        (is (contains? tc :description))
        (is (string? (:description tc))))))) 