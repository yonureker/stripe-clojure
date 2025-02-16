(ns stripe-clojure.mock.billing.meter-events-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.billing.meter-events :as meter-events]))

(deftest ^:integration create-meter-event-test
  (testing "Create a meter event using stripe‑mock"
    (let [params {:event_name "test_meter_event"
                  :payload {:test_key "test_value"}}
          response (meter-events/create-meter-event stripe-client params)]
      (is (map? response))
      (is (= "billing.meter_event" (:object response))))))