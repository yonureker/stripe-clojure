(ns stripe-clojure.unit.v2.money-management.payout-intents-test
  (:require [clojure.test :refer [deftest testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.v2.money-management.payout-intents :as v2-payout-intents]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (h/check-endpoint config/stripe-v2-endpoints :v2-money-management-payout-intents "/v2/money_management/payout_intents"))

(deftest function-existence-test
  (testing "all functions exist"
    (h/check-functions-exist
     [#'v2-payout-intents/create-payout-intent
      #'v2-payout-intents/retrieve-payout-intent
      #'v2-payout-intents/update-payout-intent
      #'v2-payout-intents/list-payout-intents
      #'v2-payout-intents/cancel-payout-intent
      #'v2-payout-intents/confirm-payout-intent
      #'v2-payout-intents/refresh-fx-quote
      #'v2-payout-intents/refresh-recipient-verification])))

(deftest function-arities-test
  (h/check-function-arities
   [[#'v2-payout-intents/create-payout-intent #{2 3}]
    [#'v2-payout-intents/retrieve-payout-intent #{2 3}]
    [#'v2-payout-intents/update-payout-intent #{3 4}]
    [#'v2-payout-intents/list-payout-intents #{1 2 3}]
    [#'v2-payout-intents/cancel-payout-intent #{2 3}]
    [#'v2-payout-intents/confirm-payout-intent #{2 3}]
    [#'v2-payout-intents/refresh-fx-quote #{2 3}]
    [#'v2-payout-intents/refresh-recipient-verification #{2 3}]]))

(deftest docstrings-test
  (h/check-docstrings
   [[#'v2-payout-intents/create-payout-intent #"(?i)create"]
    [#'v2-payout-intents/retrieve-payout-intent #"(?i)retriev"]
    [#'v2-payout-intents/update-payout-intent #"(?i)update"]
    [#'v2-payout-intents/list-payout-intents #"(?i)list"]
    [#'v2-payout-intents/cancel-payout-intent #"(?i)cancel"]
    [#'v2-payout-intents/confirm-payout-intent #"(?i)confirm"]
    [#'v2-payout-intents/refresh-fx-quote #"(?i)fx quote"]
    [#'v2-payout-intents/refresh-recipient-verification #"(?i)recipient verification"]]))

(deftest create-payout-intent-request-test
  (testing "create uses POST on base endpoint"
    (h/check-request-calls
     [{:api-fn v2-payout-intents/create-payout-intent
       :args [{:amount {:value 1000 :currency "usd"} :from {:financial_account "fa_123" :currency "usd"} :to {:recipient "acct_123"}} {}]
       :method :post
       :endpoint "/v2/money_management/payout_intents"
       :params {:amount {:value 1000 :currency "usd"} :from {:financial_account "fa_123" :currency "usd"} :to {:recipient "acct_123"}}
       :opts {}}
      {:api-fn v2-payout-intents/create-payout-intent
       :args [{:amount {:value 1000 :currency "usd"} :from {:financial_account "fa_123" :currency "usd"} :to {:recipient "acct_123"}}]
       :method :post
       :endpoint "/v2/money_management/payout_intents"
       :params {:amount {:value 1000 :currency "usd"} :from {:financial_account "fa_123" :currency "usd"} :to {:recipient "acct_123"}}
       :opts {}}])))

(deftest retrieve-payout-intent-request-test
  (testing "retrieve uses GET with payout intent ID"
    (h/check-request-calls
     [{:api-fn v2-payout-intents/retrieve-payout-intent
       :args ["poi_123" {}]
       :method :get
       :endpoint "/v2/money_management/payout_intents/poi_123"
       :params {}
       :opts {}}
      {:api-fn v2-payout-intents/retrieve-payout-intent
       :args ["poi_123"]
       :method :get
       :endpoint "/v2/money_management/payout_intents/poi_123"
       :params {}
       :opts {}}])))

(deftest update-payout-intent-request-test
  (testing "update uses POST with payout intent ID"
    (h/check-request-calls
     [{:api-fn v2-payout-intents/update-payout-intent
       :args ["poi_123" {:description "Supplier payout"} {}]
       :method :post
       :endpoint "/v2/money_management/payout_intents/poi_123"
       :params {:description "Supplier payout"}
       :opts {}}
      {:api-fn v2-payout-intents/update-payout-intent
       :args ["poi_123" {:description "Supplier payout"}]
       :method :post
       :endpoint "/v2/money_management/payout_intents/poi_123"
       :params {:description "Supplier payout"}
       :opts {}}])))

(deftest list-payout-intents-request-test
  (testing "list uses GET on base endpoint"
    (h/check-request-calls
     [{:api-fn v2-payout-intents/list-payout-intents
       :args [{:limit 10} {}]
       :method :get
       :endpoint "/v2/money_management/payout_intents"
       :params {:limit 10}
       :opts {}}
      {:api-fn v2-payout-intents/list-payout-intents
       :args [{:limit 10}]
       :method :get
       :endpoint "/v2/money_management/payout_intents"
       :params {:limit 10}
       :opts {}}
      {:api-fn v2-payout-intents/list-payout-intents
       :args []
       :method :get
       :endpoint "/v2/money_management/payout_intents"
       :params {}
       :opts {}}])))

(deftest cancel-payout-intent-request-test
  (testing "cancel uses POST on /cancel action"
    (h/check-request-calls
     [{:api-fn v2-payout-intents/cancel-payout-intent
       :args ["poi_123" {}]
       :method :post
       :endpoint "/v2/money_management/payout_intents/poi_123/cancel"
       :params {}
       :opts {}}
      {:api-fn v2-payout-intents/cancel-payout-intent
       :args ["poi_123"]
       :method :post
       :endpoint "/v2/money_management/payout_intents/poi_123/cancel"
       :params {}
       :opts {}}])))

(deftest confirm-payout-intent-request-test
  (testing "confirm uses POST on /confirm action"
    (h/check-request-calls
     [{:api-fn v2-payout-intents/confirm-payout-intent
       :args ["poi_123" {}]
       :method :post
       :endpoint "/v2/money_management/payout_intents/poi_123/confirm"
       :params {}
       :opts {}}
      {:api-fn v2-payout-intents/confirm-payout-intent
       :args ["poi_123"]
       :method :post
       :endpoint "/v2/money_management/payout_intents/poi_123/confirm"
       :params {}
       :opts {}}])))

(deftest refresh-fx-quote-request-test
  (testing "refresh-fx-quote uses POST on /refresh_fx_quote action"
    (h/check-request-calls
     [{:api-fn v2-payout-intents/refresh-fx-quote
       :args ["poi_123" {}]
       :method :post
       :endpoint "/v2/money_management/payout_intents/poi_123/refresh_fx_quote"
       :params {}
       :opts {}}
      {:api-fn v2-payout-intents/refresh-fx-quote
       :args ["poi_123"]
       :method :post
       :endpoint "/v2/money_management/payout_intents/poi_123/refresh_fx_quote"
       :params {}
       :opts {}}])))

(deftest refresh-recipient-verification-request-test
  (testing "refresh-recipient-verification uses POST on /refresh_recipient_verification action"
    (h/check-request-calls
     [{:api-fn v2-payout-intents/refresh-recipient-verification
       :args ["poi_123" {}]
       :method :post
       :endpoint "/v2/money_management/payout_intents/poi_123/refresh_recipient_verification"
       :params {}
       :opts {}}
      {:api-fn v2-payout-intents/refresh-recipient-verification
       :args ["poi_123"]
       :method :post
       :endpoint "/v2/money_management/payout_intents/poi_123/refresh_recipient_verification"
       :params {}
       :opts {}}])))
