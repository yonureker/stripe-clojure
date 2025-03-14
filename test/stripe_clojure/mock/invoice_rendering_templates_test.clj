(ns stripe-clojure.mock.invoice-rendering-templates-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.invoice-rendering-templates :as irt]))

(deftest retrieve-invoice-rendering-template-test
  (testing "Retrieve invoice rendering template"
    (let [response (irt/retrieve-invoice-rendering-template stripe-mock-client "irt_mock")]
      (is (map? response))
      (is (= "invoice_rendering_template" (:object response)))
      (is (string? (:id response))))))

(deftest list-invoice-rendering-templates-test
  (testing "List invoice rendering templates"
    (let [response (irt/list-invoice-rendering-templates stripe-mock-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [tmpl (:data response)]
        (is (map? tmpl))
        (is (= "invoice_rendering_template" (:object tmpl)))
        (is (string? (:id tmpl)))))))

(deftest archive-invoice-rendering-template-test
  (testing "Archive invoice rendering template"
    (let [response (irt/archive-invoice-rendering-template stripe-mock-client "irt_mock")]
      (is (map? response))
      (is (= "invoice_rendering_template" (:object response)))
      (is (string? (:id response))))))

(deftest unarchive-invoice-rendering-template-test
  (testing "Unarchive invoice rendering template"
    (let [response (irt/unarchive-invoice-rendering-template stripe-mock-client "irt_mock")]
      (is (map? response))
      (is (= "invoice_rendering_template" (:object response)))
      (is (string? (:id response)))))) 