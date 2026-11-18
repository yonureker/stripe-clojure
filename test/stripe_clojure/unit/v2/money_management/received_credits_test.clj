(ns stripe-clojure.unit.v2.money-management.received-credits-test
  (:require [clojure.test :refer [deftest testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.v2.money-management.received-credits :as v2-received-credits]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (h/check-endpoint config/stripe-v2-endpoints :v2-money-management-received-credits "/v2/money_management/received_credits"))

(deftest function-existence-test
  (testing "all functions exist"
    (h/check-functions-exist
     [#'v2-received-credits/retrieve-received-credit
      #'v2-received-credits/list-received-credits])))

(deftest function-arities-test
  (h/check-function-arities
   [[#'v2-received-credits/retrieve-received-credit #{2 3}]
    [#'v2-received-credits/list-received-credits #{1 2 3}]]))

(deftest docstrings-test
  (h/check-docstrings
   [[#'v2-received-credits/retrieve-received-credit #"(?i)retriev"]
    [#'v2-received-credits/list-received-credits #"(?i)list"]]))

(deftest retrieve-received-credit-request-test
  (testing "retrieve uses GET with received credit ID"
    (h/check-request-calls
     [{:api-fn v2-received-credits/retrieve-received-credit
       :args ["rc_123" {}]
       :method :get
       :endpoint "/v2/money_management/received_credits/rc_123"
       :params {}
       :opts {}}
      {:api-fn v2-received-credits/retrieve-received-credit
       :args ["rc_123"]
       :method :get
       :endpoint "/v2/money_management/received_credits/rc_123"
       :params {}
       :opts {}}])))

(deftest list-received-credits-request-test
  (testing "list uses GET on base endpoint"
    (h/check-request-calls
     [{:api-fn v2-received-credits/list-received-credits
       :args [{:limit 10} {}]
       :method :get
       :endpoint "/v2/money_management/received_credits"
       :params {:limit 10}
       :opts {}}
      {:api-fn v2-received-credits/list-received-credits
       :args [{:limit 10}]
       :method :get
       :endpoint "/v2/money_management/received_credits"
       :params {:limit 10}
       :opts {}}
      {:api-fn v2-received-credits/list-received-credits
       :args []
       :method :get
       :endpoint "/v2/money_management/received_credits"
       :params {}
       :opts {}}])))
