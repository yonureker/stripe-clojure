(ns stripe-clojure.unit.v2.money-management.inbound-transfers-test
  (:require [clojure.test :refer [deftest testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.v2.money-management.inbound-transfers :as v2-inbound-transfers]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (h/check-endpoint config/stripe-v2-endpoints :v2-money-management-inbound-transfers "/v2/money_management/inbound_transfers"))

(deftest function-existence-test
  (testing "all functions exist"
    (h/check-functions-exist
     [#'v2-inbound-transfers/create-inbound-transfer
      #'v2-inbound-transfers/retrieve-inbound-transfer
      #'v2-inbound-transfers/list-inbound-transfers])))

(deftest function-arities-test
  (h/check-function-arities
   [[#'v2-inbound-transfers/create-inbound-transfer #{2 3}]
    [#'v2-inbound-transfers/retrieve-inbound-transfer #{2 3}]
    [#'v2-inbound-transfers/list-inbound-transfers #{1 2 3}]]))

(deftest docstrings-test
  (h/check-docstrings
   [[#'v2-inbound-transfers/create-inbound-transfer #"(?i)create"]
    [#'v2-inbound-transfers/retrieve-inbound-transfer #"(?i)retriev"]
    [#'v2-inbound-transfers/list-inbound-transfers #"(?i)list"]]))

(deftest create-inbound-transfer-request-test
  (testing "create uses POST on base endpoint"
    (h/check-request-calls
     [{:api-fn v2-inbound-transfers/create-inbound-transfer
       :args [{:amount {:value 1000 :currency "usd"} :from {:payment_method "usba_123"} :to {:financial_account "fa_123" :currency "usd"}} {}]
       :method :post
       :endpoint "/v2/money_management/inbound_transfers"
       :params {:amount {:value 1000 :currency "usd"} :from {:payment_method "usba_123"} :to {:financial_account "fa_123" :currency "usd"}}
       :opts {}}
      {:api-fn v2-inbound-transfers/create-inbound-transfer
       :args [{:amount {:value 1000 :currency "usd"} :from {:payment_method "usba_123"} :to {:financial_account "fa_123" :currency "usd"}}]
       :method :post
       :endpoint "/v2/money_management/inbound_transfers"
       :params {:amount {:value 1000 :currency "usd"} :from {:payment_method "usba_123"} :to {:financial_account "fa_123" :currency "usd"}}
       :opts {}}])))

(deftest retrieve-inbound-transfer-request-test
  (testing "retrieve uses GET with inbound transfer ID"
    (h/check-request-calls
     [{:api-fn v2-inbound-transfers/retrieve-inbound-transfer
       :args ["ibt_123" {}]
       :method :get
       :endpoint "/v2/money_management/inbound_transfers/ibt_123"
       :params {}
       :opts {}}
      {:api-fn v2-inbound-transfers/retrieve-inbound-transfer
       :args ["ibt_123"]
       :method :get
       :endpoint "/v2/money_management/inbound_transfers/ibt_123"
       :params {}
       :opts {}}])))

(deftest list-inbound-transfers-request-test
  (testing "list uses GET on base endpoint"
    (h/check-request-calls
     [{:api-fn v2-inbound-transfers/list-inbound-transfers
       :args [{:limit 10} {}]
       :method :get
       :endpoint "/v2/money_management/inbound_transfers"
       :params {:limit 10}
       :opts {}}
      {:api-fn v2-inbound-transfers/list-inbound-transfers
       :args [{:limit 10}]
       :method :get
       :endpoint "/v2/money_management/inbound_transfers"
       :params {:limit 10}
       :opts {}}
      {:api-fn v2-inbound-transfers/list-inbound-transfers
       :args []
       :method :get
       :endpoint "/v2/money_management/inbound_transfers"
       :params {}
       :opts {}}])))
