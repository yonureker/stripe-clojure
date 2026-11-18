(ns stripe-clojure.unit.v2.money-management.financial-accounts-test
  (:require [clojure.test :refer [deftest testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.v2.money-management.financial-accounts :as v2-financial-accounts]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (h/check-endpoint config/stripe-v2-endpoints :v2-money-management-financial-accounts "/v2/money_management/financial_accounts"))

(deftest function-existence-test
  (testing "all functions exist"
    (h/check-functions-exist
     [#'v2-financial-accounts/retrieve-financial-account
      #'v2-financial-accounts/update-financial-account
      #'v2-financial-accounts/list-financial-accounts])))

(deftest function-arities-test
  (h/check-function-arities
   [[#'v2-financial-accounts/retrieve-financial-account #{2 3}]
    [#'v2-financial-accounts/update-financial-account #{3 4}]
    [#'v2-financial-accounts/list-financial-accounts #{1 2 3}]]))

(deftest docstrings-test
  (h/check-docstrings
   [[#'v2-financial-accounts/retrieve-financial-account #"(?i)retriev"]
    [#'v2-financial-accounts/update-financial-account #"(?i)update"]
    [#'v2-financial-accounts/list-financial-accounts #"(?i)list"]]))

(deftest retrieve-financial-account-request-test
  (testing "retrieve uses GET with financial account ID"
    (h/check-request-calls
     [{:api-fn v2-financial-accounts/retrieve-financial-account
       :args ["fa_123" {}]
       :method :get
       :endpoint "/v2/money_management/financial_accounts/fa_123"
       :params {}
       :opts {}}
      {:api-fn v2-financial-accounts/retrieve-financial-account
       :args ["fa_123"]
       :method :get
       :endpoint "/v2/money_management/financial_accounts/fa_123"
       :params {}
       :opts {}}])))

(deftest update-financial-account-request-test
  (testing "update uses POST with financial account ID"
    (h/check-request-calls
     [{:api-fn v2-financial-accounts/update-financial-account
       :args ["fa_123" {:display_name "Operating account"} {}]
       :method :post
       :endpoint "/v2/money_management/financial_accounts/fa_123"
       :params {:display_name "Operating account"}
       :opts {}}
      {:api-fn v2-financial-accounts/update-financial-account
       :args ["fa_123" {:display_name "Operating account"}]
       :method :post
       :endpoint "/v2/money_management/financial_accounts/fa_123"
       :params {:display_name "Operating account"}
       :opts {}}])))

(deftest list-financial-accounts-request-test
  (testing "list uses GET on base endpoint"
    (h/check-request-calls
     [{:api-fn v2-financial-accounts/list-financial-accounts
       :args [{:limit 10} {}]
       :method :get
       :endpoint "/v2/money_management/financial_accounts"
       :params {:limit 10}
       :opts {}}
      {:api-fn v2-financial-accounts/list-financial-accounts
       :args [{:limit 10}]
       :method :get
       :endpoint "/v2/money_management/financial_accounts"
       :params {:limit 10}
       :opts {}}
      {:api-fn v2-financial-accounts/list-financial-accounts
       :args []
       :method :get
       :endpoint "/v2/money_management/financial_accounts"
       :params {}
       :opts {}}])))
