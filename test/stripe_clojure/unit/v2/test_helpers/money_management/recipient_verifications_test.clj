(ns stripe-clojure.unit.v2.test-helpers.money-management.recipient-verifications-test
  (:require [clojure.test :refer [deftest testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.v2.test-helpers.money-management.recipient-verifications :as v2-recipient-verifications]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (h/check-endpoint config/stripe-v2-endpoints
                    :v2-test-helpers-money-management-recipient-verifications
                    "/v2/test_helpers/money_management/recipient_verifications"))

(deftest function-existence-test
  (testing "all functions exist"
    (h/check-functions-exist
     [#'v2-recipient-verifications/create-recipient-verification])))

(deftest function-arities-test
  (h/check-function-arities
   [[#'v2-recipient-verifications/create-recipient-verification #{2 3}]]))

(deftest docstrings-test
  (h/check-docstrings
   [[#'v2-recipient-verifications/create-recipient-verification #"(?i)create"]]))

(deftest create-recipient-verification-request-test
  (testing "create uses POST on base endpoint"
    (h/check-request-calls
     [{:api-fn v2-recipient-verifications/create-recipient-verification
       :args [{:match_result "match" :payout_method "usba_123"} {}]
       :method :post
       :endpoint "/v2/test_helpers/money_management/recipient_verifications"
       :params {:match_result "match" :payout_method "usba_123"}
       :opts {}}
      {:api-fn v2-recipient-verifications/create-recipient-verification
       :args [{:match_result "match" :payout_method "usba_123"}]
       :method :post
       :endpoint "/v2/test_helpers/money_management/recipient_verifications"
       :params {:match_result "match" :payout_method "usba_123"}
       :opts {}}])))
