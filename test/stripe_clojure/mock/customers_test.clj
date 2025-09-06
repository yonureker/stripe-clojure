(ns stripe-clojure.mock.customers-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.customers :as customers]))

(deftest create-customer-test
  (testing "Create customer"
    (let [params {:email "test@example.com"}
          response (customers/create-customer stripe-mock-client params)]
      (is (map? response))
      (is (= "customer" (:object response)))
      (is (string? (:id response)))
      (is (= "test@example.com" (:email response))))))

(deftest retrieve-customer-test
  (testing "Retrieve customer"
    (let [response (customers/retrieve-customer stripe-mock-client "cus_mock")]
      (is (map? response))
      (is (= "customer" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :email)))))

(deftest update-customer-test
  (testing "Update customer"
    (let [params {:email "updated@example.com"}
          response (customers/update-customer stripe-mock-client "cus_mock" params)]
      (is (map? response))
      (is (= "customer" (:object response)))
      (is (string? (:id response)))
      (is (= "updated@example.com" (:email response))))))

(deftest delete-customer-test
  (testing "Delete customer"
    (let [response (customers/delete-customer stripe-mock-client "cus_mock")]
      (is (map? response))
      (is (= "customer" (:object response)))
      (is (string? (:id response)))
      (is (true? (:deleted response))))))

(deftest list-customers-test
  (testing "List customers"
    (let [response (customers/list-customers stripe-mock-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [cust (:data response)]
        (is (map? cust))
        (is (= "customer" (:object cust)))
        (is (string? (:id cust)))
        (is (contains? cust :email))))))

(deftest search-customers-test
  (testing "Search customers"
    (let [response (customers/search-customers stripe-mock-client {:query "email:'test@example.com'"})]
      (is (map? response))
      (is (= "search_result" (:object response)))
      (is (vector? (:data response)))
      (doseq [cust (:data response)]
        (is (map? cust))
        (is (= "customer" (:object cust)))
        (is (string? (:id cust)))
        (is (contains? cust :email))))))

(deftest list-balance-transactions-test
  (testing "List balance transactions"
    (let [response (customers/list-balance-transactions stripe-mock-client "cus_mock" {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [bt (:data response)]
        (is (map? bt))
        (is (= "customer_balance_transaction" (:object bt)))
        (is (string? (:id bt)))
        (is (contains? bt :amount))))))

(deftest create-balance-transaction-test
  (testing "Create balance transaction"
    (let [params {:amount 200 :currency "usd"}
          response (customers/create-balance-transaction stripe-mock-client "cus_mock" params)]
      (is (map? response))
      (is (= "customer_balance_transaction" (:object response)))
      (is (string? (:id response)))
      (is (= 200 (:amount response))))))

(deftest retrieve-balance-transaction-test
  (testing "Retrieve balance transaction"
    (let [response (customers/retrieve-balance-transaction stripe-mock-client "cus_mock" "bt_mock")]
      (is (map? response))
      (is (= "customer_balance_transaction" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :amount)))))

(deftest update-balance-transaction-test
  (testing "Update balance transaction"
    (let [params {:description "test_description"}
          response (customers/update-balance-transaction stripe-mock-client "cus_mock" "bt_mock" params)]
      (is (map? response))
      (is (= "customer_balance_transaction" (:object response)))
      (is (string? (:id response)))
      (is (= "test_description" (:description response))))))

(deftest retrieve-cash-balance-test
  (testing "Retrieve cash balance"
    (let [response (customers/retrieve-cash-balance stripe-mock-client "cus_mock")]
      (is (map? response))
      (is (= "cash_balance" (:object response)))
      (is (contains? response :available)))))

(deftest update-cash-balance-test
  (testing "Update cash balance"
    (let [params {:settings {:reconciliation_mode "manual"}}
          response (customers/update-cash-balance stripe-mock-client "cus_mock" params)]
      (is (map? response))
      (is (= "cash_balance" (:object response)))
      (is (contains? response :settings)))))

(deftest list-cash-balance-transactions-test
  (testing "List cash balance transactions"
    (let [response (customers/list-cash-balance-transactions stripe-mock-client "cus_mock" {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [tx (:data response)]
        (is (map? tx))
        (is (= "customer_cash_balance_transaction" (:object tx)))
        (is (string? (:id tx)))))))

(deftest retrieve-cash-balance-transaction-test
  (testing "Retrieve cash balance transaction"
    (let [response (customers/retrieve-cash-balance-transaction stripe-mock-client "cus_mock" "cbst_mock")]
      (is (map? response))
      (is (= "customer_cash_balance_transaction" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :net_amount)))))

(deftest list-payment-methods-test
  (testing "List payment methods"
    (let [response (customers/list-payment-methods stripe-mock-client "cus_mock" {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [pm (:data response)]
        (is (map? pm))
        (is (= "payment_method" (:object pm)))
        (is (string? (:id pm)))))))

(deftest list-sources-test
  (testing "List sources"
    (let [response (customers/list-sources stripe-mock-client "cus_mock" {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [src (:data response)]
        (is (map? src))
        (is (= "bank_account" (:object src)))
        (is (string? (:id src)))))))

(deftest create-source-test
  (testing "Create source"
    (let [params {:source "tok_visa"}
          response (customers/create-source stripe-mock-client "cus_mock" params)]
      (is (map? response))
      (is (= "account" (:object response)))
      (is (string? (:id response))))))

(deftest update-source-test
  (testing "Update source"
    (let [params {:metadata {}}
          response (customers/update-source stripe-mock-client "cus_mock" "src_mock" params)]
      (is (map? response))
      (is (= "card" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :metadata)))))

(deftest delete-source-test
  (testing "Delete source"
    (let [response (customers/delete-source stripe-mock-client "cus_mock" "src_mock")]
      (is (map? response))
      (is (= "account" (:object response)))
      (is (string? (:id response))))))

(deftest list-tax-ids-test
  (testing "List tax IDs"
    (let [response (customers/list-tax-ids stripe-mock-client "cus_mock" {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [tax (:data response)]
        (is (map? tax))
        (is (= "tax_id" (:object tax)))
        (is (string? (:id tax)))))))

(deftest create-tax-id-test
  (testing "Create tax ID"
    (let [params {:type "eu_vat" :value "DE123456789"}
          response (customers/create-tax-id stripe-mock-client "cus_mock" params)]
      (is (map? response))
      (is (= "tax_id" (:object response)))
      (is (string? (:id response)))
      (is (= "eu_vat" (:type response)))
      (is (= "DE123456789" (:value response))))))

(deftest delete-tax-id-test
  (testing "Delete tax ID"
    (let [response (customers/delete-tax-id stripe-mock-client "cus_mock" "taxid_mock")]
      (is (map? response))
      (is (= "tax_id" (:object response)))
      (is (string? (:id response)))
      (is (true? (:deleted response))))))

(deftest verify-source-test
  (testing "Verify source"
    (let [params {:amounts [32 45]}
          response (customers/verify-source stripe-mock-client "cus_mock" "ba_mock" params)]
      (is (map? response))
      (is (= "bank_account" (:object response)))
      (is (string? (:id response))))))

(deftest retrieve-card-test
  (testing "Retrieve card"
    (let [response (customers/retrieve-card stripe-mock-client "cus_mock" "card_mock")]
      (is (map? response))
      (is (= "card" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :last4)))))

(deftest list-cards-test
  (testing "List cards"
    (let [response (customers/list-cards stripe-mock-client "cus_mock" {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [card (:data response)]
        (is (map? card))
        (is (= "card" (:object card)))
        (is (string? (:id card)))
        (is (contains? card :last4))))))

(deftest delete-discount-test
  (testing "Delete discount"
    (let [response (customers/delete-discount stripe-mock-client "cus_mock")]
      (is (map? response))
      (is (= "discount" (:object response)))
      (is (string? (:id response)))
      (is (true? (:deleted response))))))

(deftest retrieve-payment-method-test
  (testing "Retrieve payment method"
    (let [response (customers/retrieve-payment-method stripe-mock-client "cus_mock" "pm_mock")]
      (is (map? response))
      (is (= "payment_method" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :card)))))

(deftest retrieve-source-test
  (testing "Retrieve source"
    (let [response (customers/retrieve-source stripe-mock-client "cus_mock" "src_mock")]
      (is (map? response))
      (is (= "account" (:object response)))
      (is (string? (:id response))))))

(deftest retrieve-tax-id-test
  (testing "Retrieve tax ID"
    (let [response (customers/retrieve-tax-id stripe-mock-client "cus_mock" "taxid_mock")]
      (is (map? response))
      (is (= "tax_id" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :type)))))

;; Funding Instructions Tests
(deftest create-funding-instructions-test
  (testing "Create funding instructions"
    (let [params {:bank_transfer {:type "us_bank_transfer"}
                  :currency "usd"
                  :funding_type "bank_transfer"}
          response (customers/create-funding-instructions stripe-mock-client "cus_mock" params)]
      (is (map? response))
      (is (= "funding_instructions" (:object response)))
      (is (boolean? (:livemode response))))))

;; Bank Account Tests
(deftest create-bank-account-test
  (testing "Create bank account"
    (let [params {:source "btok_us"}
          response (customers/create-bank-account stripe-mock-client "cus_mock" params)]
      (is (map? response))
      ;; stripe-mock returns account object instead of bank_account for this endpoint
      (is (= "account" (:object response)))
      (is (string? (:id response))))))

(deftest update-bank-account-test
  (testing "Update bank account"
    (let [params {:metadata {:key "value"}}
          response (customers/update-bank-account stripe-mock-client "cus_mock" "ba_mock" params)]
      (is (map? response))
      ;; stripe-mock returns card object for this endpoint  
      (is (= "card" (:object response)))
      (is (string? (:id response))))))

(deftest delete-bank-account-test
  (testing "Delete bank account"
    (let [response (customers/delete-bank-account stripe-mock-client "cus_mock" "ba_mock")]
      (is (map? response))
      ;; stripe-mock returns account object instead of bank_account for this endpoint
      (is (= "account" (:object response)))
      (is (string? (:id response))))))

(deftest list-bank-accounts-test
  (testing "List bank accounts"
    (let [response (customers/list-bank-accounts stripe-mock-client "cus_mock")]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (is (boolean? (:has_more response))))))

(deftest verify-bank-account-test
  (testing "Verify bank account"
    (let [params {:amounts [32 45]}
          response (customers/verify-bank-account stripe-mock-client "cus_mock" "ba_mock" params)]
      (is (map? response))
      (is (= "bank_account" (:object response)))
      (is (string? (:id response))))))

;; Card Tests
(deftest create-card-test
  (testing "Create card"
    (let [params {:source "tok_visa"}
          response (customers/create-card stripe-mock-client "cus_mock" params)]
      (is (map? response))
      ;; stripe-mock returns account object instead of card for this endpoint
      (is (= "account" (:object response)))
      (is (string? (:id response))))))

(deftest update-card-test
  (testing "Update card"
    (let [params {:metadata {:key "value"}}
          response (customers/update-card stripe-mock-client "cus_mock" "card_mock" params)]
      (is (map? response))
      (is (= "card" (:object response)))
      (is (string? (:id response))))))

(deftest delete-card-test
  (testing "Delete card"
    (let [response (customers/delete-card stripe-mock-client "cus_mock" "card_mock")]
      (is (map? response))
      ;; stripe-mock returns account object instead of card for this endpoint
      (is (= "account" (:object response)))
      (is (string? (:id response))))))
