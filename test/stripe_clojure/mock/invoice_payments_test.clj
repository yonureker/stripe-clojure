(ns stripe-clojure.mock.invoice-payments-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [stripe-clojure.invoice-payments :as invoice-payments]
            [clojure.test :refer [deftest is testing]]))

(deftest retrieve-invoice-payment-test
  (testing "Retrieve invoice payment"
    (let [response (invoice-payments/retrieve-invoice-payment stripe-mock-client "ip_mock")]
      (is (map? response))
      (is (= "invoice_payment" (:object response)))
      (is (string? (:id response))))))

(deftest list-invoice-payments-test
  (testing "List invoice payments"
    (let [response (invoice-payments/list-invoice-payments stripe-mock-client)]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response))))))
