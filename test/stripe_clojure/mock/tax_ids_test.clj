(ns stripe-clojure.mock.tax-ids-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.tax-ids :as tax-ids]))

(deftest ^:integration create-tax-id-test
  (testing "Create tax id"
    (let [params {:type "eu_vat" :value "DE123456789"}
          response (tax-ids/create-tax-id stripe-client params)]
      (is (map? response))
      (is (= "tax_id" (:object response)))
      (is (string? (:id response)))
      (is (= "eu_vat" (:type response)))
      (is (= "DE123456789" (:value response)))
      (is (= "DE" (:country response))))))

(deftest ^:integration retrieve-tax-id-test
  (testing "Retrieve tax id"
    (let [response (tax-ids/retrieve-tax-id stripe-client "txi_mock")]
      (is (map? response))
      (is (= "tax_id" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :type))
      (is (string? (:type response))))))

(deftest ^:integration list-tax-ids-test
  (testing "List tax ids"
    (let [response (tax-ids/list-tax-ids stripe-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [tid (:data response)]
        (is (map? tid))
        (is (= "tax_id" (:object tid)))
        (is (string? (:id tid)))))))

(deftest ^:integration delete-tax-id-test
  (testing "Delete tax id"
    (let [response (tax-ids/delete-tax-id stripe-client "txi_mock")]
      (is (map? response))
      (is (= "tax_id" (:object response)))
      (is (string? (:id response)))
      (is (true? (:deleted response)))))) 