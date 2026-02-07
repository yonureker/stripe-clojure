(ns stripe-clojure.unit.v2.billing.meter-event-adjustments-test
  (:require [clojure.test :refer [deftest testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.v2.billing.meter-event-adjustments :as v2-adjustments]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (h/check-endpoint config/stripe-v2-endpoints :v2-billing-meter-event-adjustments "/v2/billing/meter_event_adjustments"))

(deftest function-existence-test
  (h/check-functions-exist [#'v2-adjustments/create-meter-event-adjustment]))

(deftest function-arities-test
  (h/check-function-arities
   [[#'v2-adjustments/create-meter-event-adjustment #{2 3}]]))

(deftest docstrings-test
  (h/check-docstrings
   [[#'v2-adjustments/create-meter-event-adjustment #"(?i)cancel" #"(?i)adjustment"]]))

(deftest create-meter-event-adjustment-request-test
  (testing "create-meter-event-adjustment uses POST with correct endpoint"
    (let [params {:event_name "api_requests"
                  :type "cancel"
                  :cancel {:identifier "unique-event-id-123"}}]
      (h/check-request-calls
       [{:api-fn v2-adjustments/create-meter-event-adjustment
         :args [params {:idempotency-key "k1"}]
         :method :post
         :endpoint "/v2/billing/meter_event_adjustments"
         :params params
         :opts {:idempotency-key "k1"}}
        ;; 2-arity defaults opts
        {:api-fn v2-adjustments/create-meter-event-adjustment
         :args [params]
         :method :post
         :endpoint "/v2/billing/meter_event_adjustments"
         :params params
         :opts {}}]))))
