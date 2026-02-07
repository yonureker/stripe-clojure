(ns stripe-clojure.unit.v2.billing.meter-events-test
  (:require [clojure.test :refer [deftest testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.v2.billing.meter-events :as v2-meter-events]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (h/check-endpoint config/stripe-v2-endpoints :v2-billing-meter-events "/v2/billing/meter_events"))

(deftest function-existence-test
  (h/check-functions-exist [#'v2-meter-events/create-meter-event]))

(deftest function-arities-test
  (h/check-function-arities
   [[#'v2-meter-events/create-meter-event #{2 3}]]))

(deftest docstrings-test
  (h/check-docstrings
   [[#'v2-meter-events/create-meter-event #"event_name" #"payload" #"stripe_customer_id"]]))

(deftest create-meter-event-request-test
  (testing "create-meter-event uses POST with correct endpoint and params"
    (let [params {:event_name "api_requests"
                  :payload {:stripe_customer_id "cus_123" :value 1}}]
      (h/check-request-calls
       [{:api-fn v2-meter-events/create-meter-event
         :args [params {:idempotency-key "idem_123"}]
         :method :post
         :endpoint "/v2/billing/meter_events"
         :params params
         :opts {:idempotency-key "idem_123"}}
        ;; 2-arity defaults opts to {}
        {:api-fn v2-meter-events/create-meter-event
         :args [params]
         :method :post
         :endpoint "/v2/billing/meter_events"
         :params params
         :opts {}}]))))
