(ns stripe-clojure.mock.tax-rates-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.tax-rates :as tax-rates]))

(deftest ^:integration create-tax-rate-test
  (testing "Create tax rate"
    (let [params {:display_name "VAT"
                  :percentage 20.0
                  :inclusive false
                  :description "German VAT"
                  :jurisdiction "DE"}
          response (tax-rates/create-tax-rate stripe-client params)]
      (is (map? response))
      (is (= "tax_rate" (:object response)))
      (is (string? (:id response)))
      (is (= "VAT" (:display_name response)))
      (is (= 20 (:percentage response)))
      (is (= false (:inclusive response)))
      (is (= "German VAT" (:description response)))
      (is (= "DE" (:jurisdiction response))))))

(deftest ^:integration retrieve-tax-rate-test
  (testing "Retrieve tax rate"
    (let [response (tax-rates/retrieve-tax-rate stripe-client "txr_mock")]
      (is (map? response))
      (is (= "tax_rate" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :display_name))
      (is (string? (:display_name response))))))

(deftest ^:integration update-tax-rate-test
  (testing "Update tax rate"
    (let [params {:description "Updated description"}
          response (tax-rates/update-tax-rate stripe-client "txr_mock" params)]
      (is (map? response))
      (is (= "tax_rate" (:object response)))
      (is (string? (:id response)))
      (is (= "Updated description" (:description response))))))

(deftest ^:integration list-tax-rates-test
  (testing "List tax rates"
    (let [response (tax-rates/list-tax-rates stripe-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [tr (:data response)]
        (is (map? tr))
        (is (= "tax_rate" (:object tr)))
        (is (string? (:id tr))))))) 