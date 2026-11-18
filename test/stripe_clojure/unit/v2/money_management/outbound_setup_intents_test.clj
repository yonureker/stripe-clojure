(ns stripe-clojure.unit.v2.money-management.outbound-setup-intents-test
  (:require [clojure.test :refer [deftest testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.v2.money-management.outbound-setup-intents :as v2-outbound-setup-intents]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (h/check-endpoint config/stripe-v2-endpoints :v2-money-management-outbound-setup-intents "/v2/money_management/outbound_setup_intents"))

(deftest function-existence-test
  (testing "all functions exist"
    (h/check-functions-exist
     [#'v2-outbound-setup-intents/create-outbound-setup-intent
      #'v2-outbound-setup-intents/retrieve-outbound-setup-intent
      #'v2-outbound-setup-intents/update-outbound-setup-intent
      #'v2-outbound-setup-intents/list-outbound-setup-intents
      #'v2-outbound-setup-intents/cancel-outbound-setup-intent])))

(deftest function-arities-test
  (h/check-function-arities
   [[#'v2-outbound-setup-intents/create-outbound-setup-intent #{2 3}]
    [#'v2-outbound-setup-intents/retrieve-outbound-setup-intent #{2 3}]
    [#'v2-outbound-setup-intents/update-outbound-setup-intent #{3 4}]
    [#'v2-outbound-setup-intents/list-outbound-setup-intents #{1 2 3}]
    [#'v2-outbound-setup-intents/cancel-outbound-setup-intent #{2 3}]]))

(deftest docstrings-test
  (h/check-docstrings
   [[#'v2-outbound-setup-intents/create-outbound-setup-intent #"(?i)create"]
    [#'v2-outbound-setup-intents/retrieve-outbound-setup-intent #"(?i)retriev"]
    [#'v2-outbound-setup-intents/update-outbound-setup-intent #"(?i)update"]
    [#'v2-outbound-setup-intents/list-outbound-setup-intents #"(?i)list"]
    [#'v2-outbound-setup-intents/cancel-outbound-setup-intent #"(?i)cancel"]]))

(deftest create-outbound-setup-intent-request-test
  (testing "create uses POST on base endpoint"
    (h/check-request-calls
     [{:api-fn v2-outbound-setup-intents/create-outbound-setup-intent
       :args [{:payout_method_data {:type "bank_account"}} {}]
       :method :post
       :endpoint "/v2/money_management/outbound_setup_intents"
       :params {:payout_method_data {:type "bank_account"}}
       :opts {}}
      {:api-fn v2-outbound-setup-intents/create-outbound-setup-intent
       :args [{:payout_method_data {:type "bank_account"}}]
       :method :post
       :endpoint "/v2/money_management/outbound_setup_intents"
       :params {:payout_method_data {:type "bank_account"}}
       :opts {}}])))

(deftest retrieve-outbound-setup-intent-request-test
  (testing "retrieve uses GET with outbound setup intent ID"
    (h/check-request-calls
     [{:api-fn v2-outbound-setup-intents/retrieve-outbound-setup-intent
       :args ["osi_123" {}]
       :method :get
       :endpoint "/v2/money_management/outbound_setup_intents/osi_123"
       :params {}
       :opts {}}
      {:api-fn v2-outbound-setup-intents/retrieve-outbound-setup-intent
       :args ["osi_123"]
       :method :get
       :endpoint "/v2/money_management/outbound_setup_intents/osi_123"
       :params {}
       :opts {}}])))

(deftest update-outbound-setup-intent-request-test
  (testing "update uses POST with outbound setup intent ID"
    (h/check-request-calls
     [{:api-fn v2-outbound-setup-intents/update-outbound-setup-intent
       :args ["osi_123" {:payout_method "usba_123"} {}]
       :method :post
       :endpoint "/v2/money_management/outbound_setup_intents/osi_123"
       :params {:payout_method "usba_123"}
       :opts {}}
      {:api-fn v2-outbound-setup-intents/update-outbound-setup-intent
       :args ["osi_123" {:payout_method "usba_123"}]
       :method :post
       :endpoint "/v2/money_management/outbound_setup_intents/osi_123"
       :params {:payout_method "usba_123"}
       :opts {}}])))

(deftest list-outbound-setup-intents-request-test
  (testing "list uses GET on base endpoint"
    (h/check-request-calls
     [{:api-fn v2-outbound-setup-intents/list-outbound-setup-intents
       :args [{:limit 10} {}]
       :method :get
       :endpoint "/v2/money_management/outbound_setup_intents"
       :params {:limit 10}
       :opts {}}
      {:api-fn v2-outbound-setup-intents/list-outbound-setup-intents
       :args [{:limit 10}]
       :method :get
       :endpoint "/v2/money_management/outbound_setup_intents"
       :params {:limit 10}
       :opts {}}
      {:api-fn v2-outbound-setup-intents/list-outbound-setup-intents
       :args []
       :method :get
       :endpoint "/v2/money_management/outbound_setup_intents"
       :params {}
       :opts {}}])))

(deftest cancel-outbound-setup-intent-request-test
  (testing "cancel uses POST on /cancel action"
    (h/check-request-calls
     [{:api-fn v2-outbound-setup-intents/cancel-outbound-setup-intent
       :args ["osi_123" {}]
       :method :post
       :endpoint "/v2/money_management/outbound_setup_intents/osi_123/cancel"
       :params {}
       :opts {}}
      {:api-fn v2-outbound-setup-intents/cancel-outbound-setup-intent
       :args ["osi_123"]
       :method :post
       :endpoint "/v2/money_management/outbound_setup_intents/osi_123/cancel"
       :params {}
       :opts {}}])))
