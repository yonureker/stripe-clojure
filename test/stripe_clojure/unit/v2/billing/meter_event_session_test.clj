(ns stripe-clojure.unit.v2.billing.meter-event-session-test
  (:require [clojure.test :refer [deftest testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.v2.billing.meter-event-session :as v2-session]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (h/check-endpoint config/stripe-v2-endpoints :v2-billing-meter-event-session "/v2/billing/meter_event_session"))

(deftest function-existence-test
  (h/check-functions-exist [#'v2-session/create-meter-event-session]))

(deftest function-arities-test
  (h/check-function-arities
   [[#'v2-session/create-meter-event-session #{1 2 3}]]))

(deftest docstrings-test
  (h/check-docstrings
   [[#'v2-session/create-meter-event-session #"(?i)authentication" #"(?i)token"]]))

(deftest create-meter-event-session-request-test
  (testing "create-meter-event-session uses POST with correct endpoint"
    (h/check-request-calls
     [{:api-fn v2-session/create-meter-event-session
       :args [{} {:idempotency-key "k1"}]
       :method :post
       :endpoint "/v2/billing/meter_event_session"
       :params {}
       :opts {:idempotency-key "k1"}}
      ;; 2-arity defaults opts
      {:api-fn v2-session/create-meter-event-session
       :args [{}]
       :method :post
       :endpoint "/v2/billing/meter_event_session"
       :params {}
       :opts {}}
      ;; 1-arity defaults params and opts
      {:api-fn v2-session/create-meter-event-session
       :args []
       :method :post
       :endpoint "/v2/billing/meter_event_session"
       :params {}
       :opts {}}])))
