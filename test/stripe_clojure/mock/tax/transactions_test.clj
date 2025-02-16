(ns stripe-clojure.mock.tax.transactions-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.tax.transactions :as tx]))

(deftest ^:integration retrieve-transaction-test
  (testing "Retrieve a tax transaction"
    (let [response (tx/retrieve-transaction stripe-client "txn_mock")]
      (is (map? response))
      (is (= "tax.transaction" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :reference)))))

(deftest ^:integration create-from-calculation-test
  (testing "Create tax transaction from calculation"
    (let [params   {:calculation "taxcalc_mock" :reference "myOrder_123"}
          response (tx/create-from-calculation stripe-client params)]
      (is (map? response))
      (is (= "tax.transaction" (:object response)))
      (is (string? (:id response)))
      (is (= "myOrder_123" (:reference response)))
      (is (contains? response :line_items)))))

(deftest ^:integration create-reversal-test
  (testing "Create tax transaction reversal"
    (let [params   {:mode "partial"
                    :original_transaction "tax_txn_mock"
                    :reference "myOrder_123-refund_1"
                    :flat_amount -1499}
          response (tx/create-reversal stripe-client params)]
      (is (map? response))
      (is (= "tax.transaction" (:object response)))
      (is (string? (:id response)))
      (is (= "myOrder_123-refund_1" (:reference response)))
      (is (contains? response :shipping_cost))
      (is (contains? response :tax_date)))))

(deftest ^:integration list-line-items-test
  (testing "List line items for a tax transaction"
    (let [response (tx/list-line-items stripe-client "txn_mock" {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [item (:data response)]
        (is (map? item))
        (is (= "tax.transaction_line_item" (:object item)))
        (is (string? (:id item)))
        (is (contains? item :amount))
        (is (number? (:amount item))))))) 