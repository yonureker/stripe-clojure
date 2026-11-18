(ns stripe-clojure.unit.v2.test-helpers.money-management.financial-addresses-test
  (:require [clojure.test :refer [deftest testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.v2.test-helpers.money-management.financial-addresses :as v2-financial-addresses]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (h/check-endpoint config/stripe-v2-endpoints
                    :v2-test-helpers-money-management-financial-addresses
                    "/v2/money_management/test_helpers/financial_addresses"))

(deftest function-existence-test
  (testing "all functions exist"
    (h/check-functions-exist
     [#'v2-financial-addresses/credit-financial-address])))

(deftest function-arities-test
  (h/check-function-arities
   [[#'v2-financial-addresses/credit-financial-address #{3 4}]]))

(deftest docstrings-test
  (h/check-docstrings
   [[#'v2-financial-addresses/credit-financial-address #"(?i)credit"]]))

(deftest credit-financial-address-request-test
  (testing "credit uses POST on /credit action"
    (h/check-request-calls
     [{:api-fn v2-financial-addresses/credit-financial-address
       :args ["finaddr_123" {:amount {:value 1000 :currency "usd"} :network "ach"} {}]
       :method :post
       :endpoint "/v2/money_management/test_helpers/financial_addresses/finaddr_123/credit"
       :params {:amount {:value 1000 :currency "usd"} :network "ach"}
       :opts {}}
      {:api-fn v2-financial-addresses/credit-financial-address
       :args ["finaddr_123" {:amount {:value 1000 :currency "usd"} :network "ach"}]
       :method :post
       :endpoint "/v2/money_management/test_helpers/financial_addresses/finaddr_123/credit"
       :params {:amount {:value 1000 :currency "usd"} :network "ach"}
       :opts {}}])))
