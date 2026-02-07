(ns stripe-clojure.unit.v2.core.event-destinations-test
  (:require [clojure.test :refer [deftest testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.v2.core.event-destinations :as v2-dest]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (h/check-endpoint config/stripe-v2-endpoints :v2-core-event-destinations "/v2/core/event_destinations"))

(deftest function-existence-test
  (testing "all CRUD functions exist"
    (h/check-functions-exist
     [#'v2-dest/create-event-destination
      #'v2-dest/retrieve-event-destination
      #'v2-dest/update-event-destination
      #'v2-dest/delete-event-destination
      #'v2-dest/list-event-destinations]))

  (testing "additional action functions exist"
    (h/check-functions-exist
     [#'v2-dest/ping-event-destination
      #'v2-dest/disable-event-destination
      #'v2-dest/enable-event-destination])))

(deftest function-arities-test
  (h/check-function-arities
   [[#'v2-dest/create-event-destination #{2 3}]
    [#'v2-dest/retrieve-event-destination #{2 3}]
    [#'v2-dest/update-event-destination #{3 4}]
    [#'v2-dest/delete-event-destination #{2 3}]
    [#'v2-dest/list-event-destinations #{1 2 3}]
    [#'v2-dest/ping-event-destination #{2 3}]
    [#'v2-dest/disable-event-destination #{2 3}]
    [#'v2-dest/enable-event-destination #{2 3}]]))

(deftest docstrings-test
  (h/check-docstrings
   [[#'v2-dest/create-event-destination #"(?i)create"]
    [#'v2-dest/retrieve-event-destination #"(?i)retriev"]
    [#'v2-dest/update-event-destination #"(?i)update"]
    [#'v2-dest/delete-event-destination #"(?i)delet"]
    [#'v2-dest/list-event-destinations #"(?i)list"]
    [#'v2-dest/ping-event-destination #"(?i)ping"]
    [#'v2-dest/disable-event-destination #"(?i)disabl"]
    [#'v2-dest/enable-event-destination #"(?i)enabl"]]))

(deftest create-event-destination-request-test
  (testing "create uses POST on base endpoint"
    (h/check-request-calls
     [{:api-fn v2-dest/create-event-destination
       :args [{:type "webhook_endpoint" :name "My Webhook"} {}]
       :method :post
       :endpoint "/v2/core/event_destinations"
       :params {:type "webhook_endpoint" :name "My Webhook"}
       :opts {}}
      {:api-fn v2-dest/create-event-destination
       :args [{:type "webhook_endpoint"}]
       :method :post
       :endpoint "/v2/core/event_destinations"
       :params {:type "webhook_endpoint"}
       :opts {}}])))

(deftest retrieve-event-destination-request-test
  (testing "retrieve uses GET with destination ID"
    (h/check-request-calls
     [{:api-fn v2-dest/retrieve-event-destination
       :args ["ed_123" {}]
       :method :get
       :endpoint "/v2/core/event_destinations/ed_123"
       :params {}
       :opts {}}
      {:api-fn v2-dest/retrieve-event-destination
       :args ["ed_123"]
       :method :get
       :endpoint "/v2/core/event_destinations/ed_123"
       :params {}
       :opts {}}])))

(deftest update-event-destination-request-test
  (testing "update uses POST with destination ID"
    (h/check-request-calls
     [{:api-fn v2-dest/update-event-destination
       :args ["ed_123" {:name "Updated"} {}]
       :method :post
       :endpoint "/v2/core/event_destinations/ed_123"
       :params {:name "Updated"}
       :opts {}}
      {:api-fn v2-dest/update-event-destination
       :args ["ed_123" {:name "Updated"}]
       :method :post
       :endpoint "/v2/core/event_destinations/ed_123"
       :params {:name "Updated"}
       :opts {}}])))

(deftest delete-event-destination-request-test
  (testing "delete uses DELETE with destination ID"
    (h/check-request-calls
     [{:api-fn v2-dest/delete-event-destination
       :args ["ed_123" {}]
       :method :delete
       :endpoint "/v2/core/event_destinations/ed_123"
       :params {}
       :opts {}}
      {:api-fn v2-dest/delete-event-destination
       :args ["ed_123"]
       :method :delete
       :endpoint "/v2/core/event_destinations/ed_123"
       :params {}
       :opts {}}])))

(deftest list-event-destinations-request-test
  (testing "list uses GET on base endpoint"
    (h/check-request-calls
     [{:api-fn v2-dest/list-event-destinations
       :args [{:limit 10} {:include ["data"]}]
       :method :get
       :endpoint "/v2/core/event_destinations"
       :params {:limit 10}
       :opts {:include ["data"]}}
      {:api-fn v2-dest/list-event-destinations
       :args []
       :method :get
       :endpoint "/v2/core/event_destinations"
       :params {}
       :opts {}}])))

(deftest ping-event-destination-request-test
  (testing "ping uses POST on /ping action"
    (h/check-request-calls
     [{:api-fn v2-dest/ping-event-destination
       :args ["ed_123" {}]
       :method :post
       :endpoint "/v2/core/event_destinations/ed_123/ping"
       :params {}
       :opts {}}
      {:api-fn v2-dest/ping-event-destination
       :args ["ed_123"]
       :method :post
       :endpoint "/v2/core/event_destinations/ed_123/ping"
       :params {}
       :opts {}}])))

(deftest disable-event-destination-request-test
  (testing "disable uses POST on /disable action"
    (h/check-request-calls
     [{:api-fn v2-dest/disable-event-destination
       :args ["ed_123" {}]
       :method :post
       :endpoint "/v2/core/event_destinations/ed_123/disable"
       :params {}
       :opts {}}
      {:api-fn v2-dest/disable-event-destination
       :args ["ed_123"]
       :method :post
       :endpoint "/v2/core/event_destinations/ed_123/disable"
       :params {}
       :opts {}}])))

(deftest enable-event-destination-request-test
  (testing "enable uses POST on /enable action"
    (h/check-request-calls
     [{:api-fn v2-dest/enable-event-destination
       :args ["ed_123" {}]
       :method :post
       :endpoint "/v2/core/event_destinations/ed_123/enable"
       :params {}
       :opts {}}
      {:api-fn v2-dest/enable-event-destination
       :args ["ed_123"]
       :method :post
       :endpoint "/v2/core/event_destinations/ed_123/enable"
       :params {}
       :opts {}}])))
