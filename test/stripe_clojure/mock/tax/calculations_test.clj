(ns stripe-clojure.mock.tax.calculations-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.tax.calculations :as calc]))

(deftest create-calculation-test
  (testing "Create tax calculation"
    (let [params {:currency "usd" :line_items [{:amount 1000 :reference "abc"}]
                  :customer "cus_mock"}
          response (calc/create-calculation stripe-mock-client params)]
      (is (map? response))
      (is (= "tax.calculation" (:object response)))
      (is (string? (:id response)))
      (is (= "cus_mock" (:customer response))))))

(deftest retrieve-calculation-test
  (testing "Retrieve tax calculation"
    (let [response (calc/retrieve-calculation stripe-mock-client "calc_mock")]
      (is (map? response))
      (is (= "tax.calculation" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :customer)))))

(deftest list-line-items-test
  (testing "List line items for tax calculation"
    (let [response (calc/list-line-items stripe-mock-client "calc_mock" {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [item (:data response)]
        (is (map? item))
        (is (= "tax.calculation_line_item" (:object item)))
        (is (string? (:id item))))))) 