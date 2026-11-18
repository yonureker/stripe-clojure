(ns stripe-clojure.unit.v2.money-management.recipient-verifications-test
  (:require [clojure.test :refer [deftest testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.v2.money-management.recipient-verifications :as v2-recipient-verifications]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (h/check-endpoint config/stripe-v2-endpoints :v2-money-management-recipient-verifications "/v2/money_management/recipient_verifications"))

(deftest function-existence-test
  (testing "all functions exist"
    (h/check-functions-exist
     [#'v2-recipient-verifications/create-recipient-verification
      #'v2-recipient-verifications/retrieve-recipient-verification
      #'v2-recipient-verifications/acknowledge-recipient-verification])))

(deftest function-arities-test
  (h/check-function-arities
   [[#'v2-recipient-verifications/create-recipient-verification #{2 3}]
    [#'v2-recipient-verifications/retrieve-recipient-verification #{2 3}]
    [#'v2-recipient-verifications/acknowledge-recipient-verification #{2 3}]]))

(deftest docstrings-test
  (h/check-docstrings
   [[#'v2-recipient-verifications/create-recipient-verification #"(?i)create"]
    [#'v2-recipient-verifications/retrieve-recipient-verification #"(?i)retriev"]
    [#'v2-recipient-verifications/acknowledge-recipient-verification #"(?i)acknowledg"]]))

(deftest create-recipient-verification-request-test
  (testing "create uses POST on base endpoint"
    (h/check-request-calls
     [{:api-fn v2-recipient-verifications/create-recipient-verification
       :args [{:payout_method "usba_123"} {}]
       :method :post
       :endpoint "/v2/money_management/recipient_verifications"
       :params {:payout_method "usba_123"}
       :opts {}}
      {:api-fn v2-recipient-verifications/create-recipient-verification
       :args [{:payout_method "usba_123"}]
       :method :post
       :endpoint "/v2/money_management/recipient_verifications"
       :params {:payout_method "usba_123"}
       :opts {}}])))

(deftest retrieve-recipient-verification-request-test
  (testing "retrieve uses GET with recipient verification ID"
    (h/check-request-calls
     [{:api-fn v2-recipient-verifications/retrieve-recipient-verification
       :args ["rv_123" {}]
       :method :get
       :endpoint "/v2/money_management/recipient_verifications/rv_123"
       :params {}
       :opts {}}
      {:api-fn v2-recipient-verifications/retrieve-recipient-verification
       :args ["rv_123"]
       :method :get
       :endpoint "/v2/money_management/recipient_verifications/rv_123"
       :params {}
       :opts {}}])))

(deftest acknowledge-recipient-verification-request-test
  (testing "acknowledge uses POST on /acknowledge action"
    (h/check-request-calls
     [{:api-fn v2-recipient-verifications/acknowledge-recipient-verification
       :args ["rv_123" {}]
       :method :post
       :endpoint "/v2/money_management/recipient_verifications/rv_123/acknowledge"
       :params {}
       :opts {}}
      {:api-fn v2-recipient-verifications/acknowledge-recipient-verification
       :args ["rv_123"]
       :method :post
       :endpoint "/v2/money_management/recipient_verifications/rv_123/acknowledge"
       :params {}
       :opts {}}])))
