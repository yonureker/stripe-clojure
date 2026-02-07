(ns stripe-clojure.unit.invoice-payments-test
  (:require [clojure.test :refer [deftest is testing]]
            [stripe-clojure.invoice-payments :as invoice-payments]
            [stripe-clojure.http.client :as client]
            [stripe-clojure.unit.test-helpers :as h]))

(deftest invoice-payments-function-arities-test
  (h/check-function-arities
   [[#'invoice-payments/retrieve-invoice-payment #{2 3}]
    [#'invoice-payments/list-invoice-payments #{1 2 3}]]))

(deftest list-invoice-payments-request-test
  (testing "1-arity calls with empty params and opts"
    (h/check-request-calls
     [{:api-fn invoice-payments/list-invoice-payments
       :args []
       :method :get
       :endpoint "/v1/invoice_payments"
       :params {}
       :opts {}}]))

  (testing "2-arity passes params with empty opts"
    (h/check-request-calls
     [{:api-fn invoice-payments/list-invoice-payments
       :args [{:limit 10}]
       :method :get
       :endpoint "/v1/invoice_payments"
       :params {:limit 10}
       :opts {}}]))

  (testing "3-arity passes both params and opts"
    (h/check-request-calls
     [{:api-fn invoice-payments/list-invoice-payments
       :args [{:limit 5} {:auto-paginate? true}]
       :method :get
       :endpoint "/v1/invoice_payments"
       :params {:limit 5}
       :opts {:auto-paginate? true}}])))
