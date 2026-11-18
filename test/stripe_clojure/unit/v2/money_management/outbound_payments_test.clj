(ns stripe-clojure.unit.v2.money-management.outbound-payments-test
  (:require [clojure.test :refer [deftest testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.v2.money-management.outbound-payments :as v2-outbound-payments]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (h/check-endpoint config/stripe-v2-endpoints :v2-money-management-outbound-payments "/v2/money_management/outbound_payments"))

(deftest function-existence-test
  (testing "all functions exist"
    (h/check-functions-exist
     [#'v2-outbound-payments/create-outbound-payment
      #'v2-outbound-payments/retrieve-outbound-payment
      #'v2-outbound-payments/list-outbound-payments
      #'v2-outbound-payments/cancel-outbound-payment])))

(deftest function-arities-test
  (h/check-function-arities
   [[#'v2-outbound-payments/create-outbound-payment #{2 3}]
    [#'v2-outbound-payments/retrieve-outbound-payment #{2 3}]
    [#'v2-outbound-payments/list-outbound-payments #{1 2 3}]
    [#'v2-outbound-payments/cancel-outbound-payment #{2 3}]]))

(deftest docstrings-test
  (h/check-docstrings
   [[#'v2-outbound-payments/create-outbound-payment #"(?i)create"]
    [#'v2-outbound-payments/retrieve-outbound-payment #"(?i)retriev"]
    [#'v2-outbound-payments/list-outbound-payments #"(?i)list"]
    [#'v2-outbound-payments/cancel-outbound-payment #"(?i)cancel"]]))

(deftest create-outbound-payment-request-test
  (testing "create uses POST on base endpoint"
    (h/check-request-calls
     [{:api-fn v2-outbound-payments/create-outbound-payment
       :args [{:amount {:value 1000 :currency "usd"} :from {:financial_account "fa_123" :currency "usd"} :to {:recipient "acct_123"}} {}]
       :method :post
       :endpoint "/v2/money_management/outbound_payments"
       :params {:amount {:value 1000 :currency "usd"} :from {:financial_account "fa_123" :currency "usd"} :to {:recipient "acct_123"}}
       :opts {}}
      {:api-fn v2-outbound-payments/create-outbound-payment
       :args [{:amount {:value 1000 :currency "usd"} :from {:financial_account "fa_123" :currency "usd"} :to {:recipient "acct_123"}}]
       :method :post
       :endpoint "/v2/money_management/outbound_payments"
       :params {:amount {:value 1000 :currency "usd"} :from {:financial_account "fa_123" :currency "usd"} :to {:recipient "acct_123"}}
       :opts {}}])))

(deftest retrieve-outbound-payment-request-test
  (testing "retrieve uses GET with outbound payment ID"
    (h/check-request-calls
     [{:api-fn v2-outbound-payments/retrieve-outbound-payment
       :args ["obp_123" {}]
       :method :get
       :endpoint "/v2/money_management/outbound_payments/obp_123"
       :params {}
       :opts {}}
      {:api-fn v2-outbound-payments/retrieve-outbound-payment
       :args ["obp_123"]
       :method :get
       :endpoint "/v2/money_management/outbound_payments/obp_123"
       :params {}
       :opts {}}])))

(deftest list-outbound-payments-request-test
  (testing "list uses GET on base endpoint"
    (h/check-request-calls
     [{:api-fn v2-outbound-payments/list-outbound-payments
       :args [{:limit 10} {}]
       :method :get
       :endpoint "/v2/money_management/outbound_payments"
       :params {:limit 10}
       :opts {}}
      {:api-fn v2-outbound-payments/list-outbound-payments
       :args [{:limit 10}]
       :method :get
       :endpoint "/v2/money_management/outbound_payments"
       :params {:limit 10}
       :opts {}}
      {:api-fn v2-outbound-payments/list-outbound-payments
       :args []
       :method :get
       :endpoint "/v2/money_management/outbound_payments"
       :params {}
       :opts {}}])))

(deftest cancel-outbound-payment-request-test
  (testing "cancel uses POST on /cancel action"
    (h/check-request-calls
     [{:api-fn v2-outbound-payments/cancel-outbound-payment
       :args ["obp_123" {}]
       :method :post
       :endpoint "/v2/money_management/outbound_payments/obp_123/cancel"
       :params {}
       :opts {}}
      {:api-fn v2-outbound-payments/cancel-outbound-payment
       :args ["obp_123"]
       :method :post
       :endpoint "/v2/money_management/outbound_payments/obp_123/cancel"
       :params {}
       :opts {}}])))
