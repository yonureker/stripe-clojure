(ns stripe-clojure.unit.v2.billing.meter-event-stream-test
  (:require [clojure.test :refer [deftest testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.v2.billing.meter-event-stream :as v2-stream]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (h/check-endpoint config/stripe-v2-endpoints :v2-billing-meter-event-stream "/v2/billing/meter_event_stream"))

(deftest function-existence-test
  (h/check-functions-exist [#'v2-stream/send-meter-events]))

(deftest function-arities-test
  (h/check-function-arities
   [[#'v2-stream/send-meter-events #{2 3}]]))

(deftest docstrings-test
  (h/check-docstrings
   [[#'v2-stream/send-meter-events #"(?i)session" #"(?i)events"]]))

(deftest send-meter-events-request-test
  (testing "send-meter-events uses POST with correct endpoint"
    (let [params {:events [{:event_name "api_requests"
                            :payload {:stripe_customer_id "cus_123" :value 1}}]}
          custom-headers {"Authorization" "Bearer session_token_123"}]
      (h/check-request-calls
       [{:api-fn v2-stream/send-meter-events
         :args [params {:custom-headers custom-headers}]
         :method :post
         :endpoint "/v2/billing/meter_event_stream"
         :params params
         :opts {:custom-headers custom-headers}}
        ;; 2-arity defaults opts to {}
        {:api-fn v2-stream/send-meter-events
         :args [params]
         :method :post
         :endpoint "/v2/billing/meter_event_stream"
         :params params
         :opts {}}]))))
