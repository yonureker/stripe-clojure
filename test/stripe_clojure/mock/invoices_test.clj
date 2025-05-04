(ns stripe-clojure.mock.invoices-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.invoices :as invoices]
            [stripe-clojure.invoice-items :as invoice-items]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Invoice Functions Tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest create-invoice-test
  (testing "create-invoice returns a valid invoice"
    (let [dummy-customer "cus_mock_123"
          invoice (invoices/create-invoice stripe-mock-client {:customer dummy-customer})]
      (is (string? (:id invoice)) "Invoice should have an id")
      (is (= "invoice" (:object invoice)) "Invoice object should be 'invoice'"))))

(deftest create-preview-invoice-test
  (testing "create-preview-invoice returns a valid invoice preview"
    (let [dummy-customer "cus_mock_123"
          preview (invoices/create-preview-invoice stripe-mock-client {:customer dummy-customer})]
      (is (string? (:id preview)) "Preview invoice should have an id")
      (is (= "invoice" (:object preview)) "Preview object should be 'invoice'"))))

(deftest update-invoice-test
  (testing "update-invoice returns an updated invoice"
    (let [dummy-customer "cus_mock_123"
          invoice (invoices/create-invoice stripe-mock-client {:customer dummy-customer})
          invoice-id (:id invoice)
          updated (invoices/update-invoice stripe-mock-client invoice-id {:metadata {"test" "value"}})]
      (is (= invoice-id (:id updated)) "Invoice id should remain unchanged")
      (is (= "invoice" (:object updated))))))

(deftest retrieve-invoice-test
  (testing "retrieve-invoice returns the correct invoice"
    (let [dummy-customer "cus_mock_123"
          invoice (invoices/create-invoice stripe-mock-client {:customer dummy-customer})
          invoice-id (:id invoice)
          retrieved (invoices/retrieve-invoice stripe-mock-client invoice-id)]
      (is (= invoice-id (:id retrieved)) "Retrieved invoice should match created one")
      (is (= "invoice" (:object retrieved))))))

(deftest list-invoices-test
  (testing "list-invoices returns a list response"
    (let [resp (invoices/list-invoices stripe-mock-client {:limit 1})]
      (is (map? resp))
      (is (vector? (:data resp))))))

(deftest delete-invoice-test
  (testing "delete-invoice returns the deleted invoice"
    (let [dummy-customer "cus_mock_123"
          invoice (invoices/create-invoice stripe-mock-client {:customer dummy-customer})
          invoice-id (:id invoice)
          deleted (invoices/delete-invoice stripe-mock-client invoice-id)]
      (is (= invoice-id (:id deleted)))
      (is (= "invoice" (:object deleted))))))

(deftest finalize-invoice-test
  (testing "finalize-invoice finalizes a draft invoice"
    (let [dummy-customer "cus_mock_123"
          _ (invoice-items/create-invoice-item
             stripe-mock-client
             {:customer dummy-customer
              :amount 600
              :currency "usd"
              :description "Mock invoice item for finalization"})
          invoice (invoices/create-invoice stripe-mock-client {:customer dummy-customer})
          invoice-id (:id invoice)
          finalized (invoices/finalize-invoice stripe-mock-client invoice-id)]
      (is (= "invoice" (:object finalized))))))

(deftest mark-invoice-uncollectible-test
  (testing "mark-invoice-uncollectible marks an invoice as uncollectible"
    (let [dummy-customer "cus_mock_123"
          invoice (invoices/create-invoice stripe-mock-client {:customer dummy-customer})
          invoice-id (:id invoice)
          uncollectible (invoices/mark-invoice-uncollectible stripe-mock-client invoice-id)]
      (is (= "invoice" (:object uncollectible))))))

(deftest pay-invoice-test
  (testing "pay-invoice processes invoice payment"
    (let [dummy-customer "cus_mock_123"
          invoice (invoices/create-invoice stripe-mock-client {:customer dummy-customer})
          invoice-id (:id invoice)
          paid (invoices/pay-invoice stripe-mock-client invoice-id {:payment_method "pm_card_visa"})]
      (is (= "invoice" (:object paid))))))

(deftest search-invoices-test
  (testing "search-invoices returns matching invoices"
    (let [resp (invoices/search-invoices stripe-mock-client {:query "status:'draft'"})]
      (is (map? resp))
      (is (vector? (:data resp))))))

(deftest send-invoice-test
  (testing "send-invoice sends an invoice to a customer"
    (let [dummy-customer "cus_mock_123"
          invoice (invoices/create-invoice stripe-mock-client {:customer dummy-customer})
          invoice-id (:id invoice)
          sent (invoices/send-invoice stripe-mock-client invoice-id)]
      (is (= "invoice" (:object sent))))))

(deftest void-invoice-test
  (testing "void-invoice voids an invoice"
    (let [dummy-customer "cus_mock_123"
          invoice (invoices/create-invoice stripe-mock-client {:customer dummy-customer})
          invoice-id (:id invoice)
          voided (invoices/void-invoice stripe-mock-client invoice-id)]
      (is (= "invoice" (:object voided))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Invoice Line Items Functions Tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest list-line-items-test
  (testing "list-line-items returns a list of line items"
    (let [dummy-invoice-id "inv_mock_123"
          resp (invoices/list-line-items stripe-mock-client dummy-invoice-id {:limit 1})]
      (is (map? resp))
      (is (vector? (:data resp))))))

(deftest update-line-item-test
  (testing "update-line-item updates an invoice line item"
    (let [dummy-invoice-id "inv_mock_123"
          dummy-line-item-id "il_mock_123"
          updated (invoices/update-line-item stripe-mock-client dummy-invoice-id dummy-line-item-id {:description "updated description"})]
      (is (map? updated))
      (is (= "line_item" (:object updated))))))

(deftest add-line-items-test
  (testing "add-line-items adds line items to an invoice"
    (let [dummy-invoice-id "inv_mock_123"
          added (invoices/add-line-items stripe-mock-client dummy-invoice-id {:lines [{:amount 500
                                                                    :currency "usd"
                                                                    :description "additional line item"
                                                                    :quantity 1}]})]
      (is (map? added))
      (is (= "invoice" (:object added))))))

(deftest remove-line-items-test
  (testing "remove-line-items removes line items from an invoice"
    (let [dummy-invoice-id "inv_mock_123"
          removed (invoices/remove-line-items stripe-mock-client dummy-invoice-id {:lines [{:id "il_mock_123"
                                                                         :behavior "delete"}]})]
      (is (map? removed))
      (is (= "invoice" (:object removed))))))

(deftest update-line-items-test
  (testing "update-line-items updates line items on an invoice"
    (let [dummy-invoice-id "inv_mock_123"
          updated (invoices/update-line-items stripe-mock-client dummy-invoice-id {:lines [{:id "il_mock_123" :description "updated line"}]})]
      (is (map? updated))
      (is (= "invoice" (:object updated))))))
