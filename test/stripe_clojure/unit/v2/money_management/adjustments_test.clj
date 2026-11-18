(ns stripe-clojure.unit.v2.money-management.adjustments-test
  (:require [clojure.test :refer [deftest testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.v2.money-management.adjustments :as v2-adjustments]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (h/check-endpoint config/stripe-v2-endpoints :v2-money-management-adjustments "/v2/money_management/adjustments"))

(deftest function-existence-test
  (testing "all functions exist"
    (h/check-functions-exist
     [#'v2-adjustments/retrieve-adjustment
      #'v2-adjustments/list-adjustments])))

(deftest function-arities-test
  (h/check-function-arities
   [[#'v2-adjustments/retrieve-adjustment #{2 3}]
    [#'v2-adjustments/list-adjustments #{1 2 3}]]))

(deftest docstrings-test
  (h/check-docstrings
   [[#'v2-adjustments/retrieve-adjustment #"(?i)retriev"]
    [#'v2-adjustments/list-adjustments #"(?i)list"]]))

(deftest retrieve-adjustment-request-test
  (testing "retrieve uses GET with adjustment ID"
    (h/check-request-calls
     [{:api-fn v2-adjustments/retrieve-adjustment
       :args ["adj_123" {}]
       :method :get
       :endpoint "/v2/money_management/adjustments/adj_123"
       :params {}
       :opts {}}
      {:api-fn v2-adjustments/retrieve-adjustment
       :args ["adj_123"]
       :method :get
       :endpoint "/v2/money_management/adjustments/adj_123"
       :params {}
       :opts {}}])))

(deftest list-adjustments-request-test
  (testing "list uses GET on base endpoint"
    (h/check-request-calls
     [{:api-fn v2-adjustments/list-adjustments
       :args [{:limit 10} {}]
       :method :get
       :endpoint "/v2/money_management/adjustments"
       :params {:limit 10}
       :opts {}}
      {:api-fn v2-adjustments/list-adjustments
       :args [{:limit 10}]
       :method :get
       :endpoint "/v2/money_management/adjustments"
       :params {:limit 10}
       :opts {}}
      {:api-fn v2-adjustments/list-adjustments
       :args []
       :method :get
       :endpoint "/v2/money_management/adjustments"
       :params {}
       :opts {}}])))
