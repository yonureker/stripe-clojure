(ns stripe-clojure.unit.v2.money-management.payout-methods-test
  (:require [clojure.test :refer [deftest testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.v2.money-management.payout-methods :as v2-payout-methods]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (h/check-endpoint config/stripe-v2-endpoints :v2-money-management-payout-methods "/v2/money_management/payout_methods"))

(deftest function-existence-test
  (testing "all functions exist"
    (h/check-functions-exist
     [#'v2-payout-methods/retrieve-payout-method
      #'v2-payout-methods/list-payout-methods
      #'v2-payout-methods/archive-payout-method
      #'v2-payout-methods/unarchive-payout-method])))

(deftest function-arities-test
  (h/check-function-arities
   [[#'v2-payout-methods/retrieve-payout-method #{2 3}]
    [#'v2-payout-methods/list-payout-methods #{1 2 3}]
    [#'v2-payout-methods/archive-payout-method #{2 3}]
    [#'v2-payout-methods/unarchive-payout-method #{2 3}]]))

(deftest docstrings-test
  (h/check-docstrings
   [[#'v2-payout-methods/retrieve-payout-method #"(?i)retriev"]
    [#'v2-payout-methods/list-payout-methods #"(?i)list"]
    [#'v2-payout-methods/archive-payout-method #"(?i)archiv"]
    [#'v2-payout-methods/unarchive-payout-method #"(?i)unarchiv"]]))

(deftest retrieve-payout-method-request-test
  (testing "retrieve uses GET with payout method ID"
    (h/check-request-calls
     [{:api-fn v2-payout-methods/retrieve-payout-method
       :args ["usba_123" {}]
       :method :get
       :endpoint "/v2/money_management/payout_methods/usba_123"
       :params {}
       :opts {}}
      {:api-fn v2-payout-methods/retrieve-payout-method
       :args ["usba_123"]
       :method :get
       :endpoint "/v2/money_management/payout_methods/usba_123"
       :params {}
       :opts {}}])))

(deftest list-payout-methods-request-test
  (testing "list uses GET on base endpoint"
    (h/check-request-calls
     [{:api-fn v2-payout-methods/list-payout-methods
       :args [{:limit 10} {}]
       :method :get
       :endpoint "/v2/money_management/payout_methods"
       :params {:limit 10}
       :opts {}}
      {:api-fn v2-payout-methods/list-payout-methods
       :args [{:limit 10}]
       :method :get
       :endpoint "/v2/money_management/payout_methods"
       :params {:limit 10}
       :opts {}}
      {:api-fn v2-payout-methods/list-payout-methods
       :args []
       :method :get
       :endpoint "/v2/money_management/payout_methods"
       :params {}
       :opts {}}])))

(deftest archive-payout-method-request-test
  (testing "archive uses POST on /archive action"
    (h/check-request-calls
     [{:api-fn v2-payout-methods/archive-payout-method
       :args ["usba_123" {}]
       :method :post
       :endpoint "/v2/money_management/payout_methods/usba_123/archive"
       :params {}
       :opts {}}
      {:api-fn v2-payout-methods/archive-payout-method
       :args ["usba_123"]
       :method :post
       :endpoint "/v2/money_management/payout_methods/usba_123/archive"
       :params {}
       :opts {}}])))

(deftest unarchive-payout-method-request-test
  (testing "unarchive uses POST on /unarchive action"
    (h/check-request-calls
     [{:api-fn v2-payout-methods/unarchive-payout-method
       :args ["usba_123" {}]
       :method :post
       :endpoint "/v2/money_management/payout_methods/usba_123/unarchive"
       :params {}
       :opts {}}
      {:api-fn v2-payout-methods/unarchive-payout-method
       :args ["usba_123"]
       :method :post
       :endpoint "/v2/money_management/payout_methods/usba_123/unarchive"
       :params {}
       :opts {}}])))
