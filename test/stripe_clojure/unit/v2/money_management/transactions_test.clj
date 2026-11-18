(ns stripe-clojure.unit.v2.money-management.transactions-test
  (:require [clojure.test :refer [deftest testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.v2.money-management.transactions :as v2-transactions]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (h/check-endpoint config/stripe-v2-endpoints :v2-money-management-transactions "/v2/money_management/transactions"))

(deftest function-existence-test
  (testing "all functions exist"
    (h/check-functions-exist
     [#'v2-transactions/retrieve-transaction
      #'v2-transactions/update-transaction
      #'v2-transactions/list-transactions
      #'v2-transactions/refresh-regulatory-receipt])))

(deftest function-arities-test
  (h/check-function-arities
   [[#'v2-transactions/retrieve-transaction #{2 3}]
    [#'v2-transactions/update-transaction #{3 4}]
    [#'v2-transactions/list-transactions #{1 2 3}]
    [#'v2-transactions/refresh-regulatory-receipt #{2 3}]]))

(deftest docstrings-test
  (h/check-docstrings
   [[#'v2-transactions/retrieve-transaction #"(?i)retriev"]
    [#'v2-transactions/update-transaction #"(?i)update"]
    [#'v2-transactions/list-transactions #"(?i)list"]
    [#'v2-transactions/refresh-regulatory-receipt #"(?i)regulatory receipt"]]))

(deftest retrieve-transaction-request-test
  (testing "retrieve uses GET with transaction ID"
    (h/check-request-calls
     [{:api-fn v2-transactions/retrieve-transaction
       :args ["trxn_123" {}]
       :method :get
       :endpoint "/v2/money_management/transactions/trxn_123"
       :params {}
       :opts {}}
      {:api-fn v2-transactions/retrieve-transaction
       :args ["trxn_123"]
       :method :get
       :endpoint "/v2/money_management/transactions/trxn_123"
       :params {}
       :opts {}}])))

(deftest update-transaction-request-test
  (testing "update uses POST with transaction ID"
    (h/check-request-calls
     [{:api-fn v2-transactions/update-transaction
       :args ["trxn_123" {:description "Supplier payment"} {}]
       :method :post
       :endpoint "/v2/money_management/transactions/trxn_123"
       :params {:description "Supplier payment"}
       :opts {}}
      {:api-fn v2-transactions/update-transaction
       :args ["trxn_123" {:description "Supplier payment"}]
       :method :post
       :endpoint "/v2/money_management/transactions/trxn_123"
       :params {:description "Supplier payment"}
       :opts {}}])))

(deftest list-transactions-request-test
  (testing "list uses GET on base endpoint"
    (h/check-request-calls
     [{:api-fn v2-transactions/list-transactions
       :args [{:financial_account "fa_123"} {}]
       :method :get
       :endpoint "/v2/money_management/transactions"
       :params {:financial_account "fa_123"}
       :opts {}}
      {:api-fn v2-transactions/list-transactions
       :args [{:financial_account "fa_123"}]
       :method :get
       :endpoint "/v2/money_management/transactions"
       :params {:financial_account "fa_123"}
       :opts {}}
      {:api-fn v2-transactions/list-transactions
       :args []
       :method :get
       :endpoint "/v2/money_management/transactions"
       :params {}
       :opts {}}])))

(deftest refresh-regulatory-receipt-request-test
  (testing "refresh-regulatory-receipt uses POST on /refresh_regulatory_receipt action"
    (h/check-request-calls
     [{:api-fn v2-transactions/refresh-regulatory-receipt
       :args ["trxn_123" {}]
       :method :post
       :endpoint "/v2/money_management/transactions/trxn_123/refresh_regulatory_receipt"
       :params {}
       :opts {}}
      {:api-fn v2-transactions/refresh-regulatory-receipt
       :args ["trxn_123"]
       :method :post
       :endpoint "/v2/money_management/transactions/trxn_123/refresh_regulatory_receipt"
       :params {}
       :opts {}}])))
