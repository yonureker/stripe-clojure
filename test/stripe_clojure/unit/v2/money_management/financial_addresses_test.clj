(ns stripe-clojure.unit.v2.money-management.financial-addresses-test
  (:require [clojure.test :refer [deftest testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.v2.money-management.financial-addresses :as v2-financial-addresses]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (h/check-endpoint config/stripe-v2-endpoints :v2-money-management-financial-addresses "/v2/money_management/financial_addresses"))

(deftest function-existence-test
  (testing "all functions exist"
    (h/check-functions-exist
     [#'v2-financial-addresses/create-financial-address
      #'v2-financial-addresses/retrieve-financial-address
      #'v2-financial-addresses/list-financial-addresses])))

(deftest function-arities-test
  (h/check-function-arities
   [[#'v2-financial-addresses/create-financial-address #{2 3}]
    [#'v2-financial-addresses/retrieve-financial-address #{2 3}]
    [#'v2-financial-addresses/list-financial-addresses #{1 2 3}]]))

(deftest docstrings-test
  (h/check-docstrings
   [[#'v2-financial-addresses/create-financial-address #"(?i)create"]
    [#'v2-financial-addresses/retrieve-financial-address #"(?i)retriev"]
    [#'v2-financial-addresses/list-financial-addresses #"(?i)list"]]))

(deftest create-financial-address-request-test
  (testing "create uses POST on base endpoint"
    (h/check-request-calls
     [{:api-fn v2-financial-addresses/create-financial-address
       :args [{:financial_account "fa_123" :type "bank_account"} {}]
       :method :post
       :endpoint "/v2/money_management/financial_addresses"
       :params {:financial_account "fa_123" :type "bank_account"}
       :opts {}}
      {:api-fn v2-financial-addresses/create-financial-address
       :args [{:financial_account "fa_123" :type "bank_account"}]
       :method :post
       :endpoint "/v2/money_management/financial_addresses"
       :params {:financial_account "fa_123" :type "bank_account"}
       :opts {}}])))

(deftest retrieve-financial-address-request-test
  (testing "retrieve uses GET with financial address ID"
    (h/check-request-calls
     [{:api-fn v2-financial-addresses/retrieve-financial-address
       :args ["finaddr_123" {}]
       :method :get
       :endpoint "/v2/money_management/financial_addresses/finaddr_123"
       :params {}
       :opts {}}
      {:api-fn v2-financial-addresses/retrieve-financial-address
       :args ["finaddr_123"]
       :method :get
       :endpoint "/v2/money_management/financial_addresses/finaddr_123"
       :params {}
       :opts {}}])))

(deftest list-financial-addresses-request-test
  (testing "list uses GET on base endpoint"
    (h/check-request-calls
     [{:api-fn v2-financial-addresses/list-financial-addresses
       :args [{:financial_account "fa_123"} {}]
       :method :get
       :endpoint "/v2/money_management/financial_addresses"
       :params {:financial_account "fa_123"}
       :opts {}}
      {:api-fn v2-financial-addresses/list-financial-addresses
       :args [{:financial_account "fa_123"}]
       :method :get
       :endpoint "/v2/money_management/financial_addresses"
       :params {:financial_account "fa_123"}
       :opts {}}
      {:api-fn v2-financial-addresses/list-financial-addresses
       :args []
       :method :get
       :endpoint "/v2/money_management/financial_addresses"
       :params {}
       :opts {}}])))
