(ns stripe-clojure.mock.invoice-items-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.invoice-items :as invoice-items]))

(deftest create-invoice-item-test
  (testing "Create invoice item"
    (let [params {:customer "cus_mock" :amount 500}
          response (invoice-items/create-invoice-item stripe-mock-client params)]
      (is (map? response))
      (is (= "invoiceitem" (:object response)))
      (is (string? (:id response)))
      (is (= "cus_mock" (:customer response)))
      (is (= 500 (:amount response))))))

(deftest retrieve-invoice-item-test
  (testing "Retrieve invoice item"
    (let [response (invoice-items/retrieve-invoice-item stripe-mock-client "ii_mock")]
      (is (map? response))
      (is (= "invoiceitem" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :customer))
      (is (string? (:customer response))))))

(deftest update-invoice-item-test
  (testing "Update invoice item"
    (let [params {:description "Updated invoice item"}
          response (invoice-items/update-invoice-item stripe-mock-client "ii_mock" params)]
      (is (map? response))
      (is (= "invoiceitem" (:object response)))
      (is (string? (:id response)))
      (is (= "Updated invoice item" (:description response))))))

(deftest delete-invoice-item-test
  (testing "Delete invoice item"
    (let [response (invoice-items/delete-invoice-item stripe-mock-client "ii_mock")]
      (is (map? response))
      (is (= "invoiceitem" (:object response)))
      (is (string? (:id response)))
      (is (true? (:deleted response))))))

(deftest list-invoice-items-test
  (testing "List invoice items"
    (let [response (invoice-items/list-invoice-items stripe-mock-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [item (:data response)]
        (is (map? item))
        (is (= "invoiceitem" (:object item)))
        (is (string? (:id item)))
        (is (contains? item :amount))
        (is (number? (:amount item))))))) 