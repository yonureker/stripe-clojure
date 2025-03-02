(ns stripe-clojure.mock.events-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.events :as events]))

(deftest retrieve-event-test
  (testing "Retrieve event"
    (let [response (events/retrieve-event stripe-mock-client "evt_mock")]
      (is (map? response))
      (is (= "event" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :type))
      (is (string? (:type response)))
      (is (contains? response :created))
      (is (number? (:created response)))
      (is (contains? response :livemode))
      (is (boolean? (:livemode response)))
      (is (contains? response :pending_webhooks))
      (is (number? (:pending_webhooks response)))
      (is (contains? response :data))
      (is (map? (:data response))))))

(deftest list-events-test
  (testing "List events"
    (let [response (events/list-events stripe-mock-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [evt (:data response)]
        (is (map? evt))
        (is (= "event" (:object evt)))
        (is (string? (:id evt)))
        (is (contains? evt :type))
        (is (string? (:type evt)))
        (is (contains? evt :created))
        (is (number? (:created evt))))))) 