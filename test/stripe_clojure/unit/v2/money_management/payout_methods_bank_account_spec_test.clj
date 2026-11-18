(ns stripe-clojure.unit.v2.money-management.payout-methods-bank-account-spec-test
  (:require [clojure.test :refer [deftest testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.v2.money-management.payout-methods-bank-account-spec :as v2-payout-methods-bank-account-spec]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (h/check-endpoint config/stripe-v2-endpoints :v2-money-management-payout-methods-bank-account-spec "/v2/money_management/payout_methods_bank_account_spec"))

(deftest function-existence-test
  (testing "all functions exist"
    (h/check-functions-exist
     [#'v2-payout-methods-bank-account-spec/retrieve-payout-methods-bank-account-spec])))

(deftest function-arities-test
  (h/check-function-arities
   [[#'v2-payout-methods-bank-account-spec/retrieve-payout-methods-bank-account-spec #{1 2 3}]]))

(deftest docstrings-test
  (h/check-docstrings
   [[#'v2-payout-methods-bank-account-spec/retrieve-payout-methods-bank-account-spec #"(?i)retriev"]]))

(deftest retrieve-payout-methods-bank-account-spec-request-test
  (testing "retrieve uses GET on base endpoint"
    (h/check-request-calls
     [{:api-fn v2-payout-methods-bank-account-spec/retrieve-payout-methods-bank-account-spec
       :args [{:countries ["US" "GB"]} {}]
       :method :get
       :endpoint "/v2/money_management/payout_methods_bank_account_spec"
       :params {:countries ["US" "GB"]}
       :opts {}}
      {:api-fn v2-payout-methods-bank-account-spec/retrieve-payout-methods-bank-account-spec
       :args [{:countries ["US" "GB"]}]
       :method :get
       :endpoint "/v2/money_management/payout_methods_bank_account_spec"
       :params {:countries ["US" "GB"]}
       :opts {}}
      {:api-fn v2-payout-methods-bank-account-spec/retrieve-payout-methods-bank-account-spec
       :args []
       :method :get
       :endpoint "/v2/money_management/payout_methods_bank_account_spec"
       :params {}
       :opts {}}])))
