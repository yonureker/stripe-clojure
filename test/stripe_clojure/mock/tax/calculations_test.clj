(ns stripe-clojure.mock.tax.calculations-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.tax.calculations :as calc]))

(deftest ^:integration create-calculation-test
  (testing "Create tax calculation"
    (let [params {:currency "usd" :line_items [{:amount 1000 :reference "abc"}]
                  :customer "cus_mock"}
          response (calc/create-calculation stripe-client params)]
      (is (map? response))
      (is (= "tax.calculation" (:object response)))
      (is (string? (:id response)))
      (is (= "cus_mock" (:customer response))))))

(deftest ^:integration retrieve-calculation-test
  (testing "Retrieve tax calculation"
    (let [response (calc/retrieve-calculation stripe-client "calc_mock")]
      (is (map? response))
      (is (= "tax.calculation" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :customer)))))

(deftest ^:integration list-line-items-test
  (testing "List line items for tax calculation"
    (let [response (calc/list-line-items stripe-client "calc_mock" {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [item (:data response)]
        (is (map? item))
        (is (= "tax.calculation_line_item" (:object item)))
        (is (string? (:id item))))))) 