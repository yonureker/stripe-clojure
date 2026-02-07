(ns stripe-clojure.unit.v2.core.events-test
  (:require [clojure.test :refer [deftest testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.v2.core.events :as v2-events]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (h/check-endpoint config/stripe-v2-endpoints :v2-core-events "/v2/core/events"))

(deftest function-existence-test
  (testing "all event functions exist"
    (h/check-functions-exist
     [#'v2-events/list-events
      #'v2-events/retrieve-event])))

(deftest function-arities-test
  (h/check-function-arities
   [[#'v2-events/list-events #{1 2 3}]
    [#'v2-events/retrieve-event #{2 3}]]))

(deftest docstrings-test
  (h/check-docstrings
   [[#'v2-events/list-events #"(?i)v2"]
    [#'v2-events/retrieve-event #"(?i)v2"]]))

(deftest list-events-request-test
  (testing "list-events calls request correctly"
    (h/check-request-calls
     [{:api-fn v2-events/list-events
       :args [{:type "v2.billing.meter_event.created"} {:include ["data.payload"]}]
       :method :get
       :endpoint "/v2/core/events"
       :params {:type "v2.billing.meter_event.created"}
       :opts {:include ["data.payload"]}}
      ;; 2-arity defaults opts
      {:api-fn v2-events/list-events
       :args [{:type "v2.billing.meter_event.created"}]
       :method :get
       :endpoint "/v2/core/events"
       :params {:type "v2.billing.meter_event.created"}
       :opts {}}
      ;; 1-arity defaults params and opts
      {:api-fn v2-events/list-events
       :args []
       :method :get
       :endpoint "/v2/core/events"
       :params {}
       :opts {}}])))

(deftest retrieve-event-request-test
  (testing "retrieve-event constructs correct URL with event ID"
    (h/check-request-calls
     [{:api-fn v2-events/retrieve-event
       :args ["evt_123" {:include ["payload"]}]
       :method :get
       :endpoint "/v2/core/events/evt_123"
       :params {}
       :opts {:include ["payload"]}}
      {:api-fn v2-events/retrieve-event
       :args ["evt_123"]
       :method :get
       :endpoint "/v2/core/events/evt_123"
       :params {}
       :opts {}}])))
