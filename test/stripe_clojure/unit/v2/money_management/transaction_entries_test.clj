(ns stripe-clojure.unit.v2.money-management.transaction-entries-test
  (:require [clojure.test :refer [deftest testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.v2.money-management.transaction-entries :as v2-transaction-entries]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (h/check-endpoint config/stripe-v2-endpoints :v2-money-management-transaction-entries "/v2/money_management/transaction_entries"))

(deftest function-existence-test
  (testing "all functions exist"
    (h/check-functions-exist
     [#'v2-transaction-entries/retrieve-transaction-entry
      #'v2-transaction-entries/list-transaction-entries])))

(deftest function-arities-test
  (h/check-function-arities
   [[#'v2-transaction-entries/retrieve-transaction-entry #{2 3}]
    [#'v2-transaction-entries/list-transaction-entries #{1 2 3}]]))

(deftest docstrings-test
  (h/check-docstrings
   [[#'v2-transaction-entries/retrieve-transaction-entry #"(?i)retriev"]
    [#'v2-transaction-entries/list-transaction-entries #"(?i)list"]]))

(deftest retrieve-transaction-entry-request-test
  (testing "retrieve uses GET with transaction entry ID"
    (h/check-request-calls
     [{:api-fn v2-transaction-entries/retrieve-transaction-entry
       :args ["trxne_123" {}]
       :method :get
       :endpoint "/v2/money_management/transaction_entries/trxne_123"
       :params {}
       :opts {}}
      {:api-fn v2-transaction-entries/retrieve-transaction-entry
       :args ["trxne_123"]
       :method :get
       :endpoint "/v2/money_management/transaction_entries/trxne_123"
       :params {}
       :opts {}}])))

(deftest list-transaction-entries-request-test
  (testing "list uses GET on base endpoint"
    (h/check-request-calls
     [{:api-fn v2-transaction-entries/list-transaction-entries
       :args [{:transaction "trxn_123"} {}]
       :method :get
       :endpoint "/v2/money_management/transaction_entries"
       :params {:transaction "trxn_123"}
       :opts {}}
      {:api-fn v2-transaction-entries/list-transaction-entries
       :args [{:transaction "trxn_123"}]
       :method :get
       :endpoint "/v2/money_management/transaction_entries"
       :params {:transaction "trxn_123"}
       :opts {}}
      {:api-fn v2-transaction-entries/list-transaction-entries
       :args []
       :method :get
       :endpoint "/v2/money_management/transaction_entries"
       :params {}
       :opts {}}])))
