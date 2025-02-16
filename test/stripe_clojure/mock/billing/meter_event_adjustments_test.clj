(ns stripe-clojure.mock.billing.meter-event-adjustments-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.billing.meter-event-adjustments :as adjustments]))

(deftest ^:integration create-meter-event-adjustment-test
  (testing "Create a meter event adjustment using stripe‑mock"
    (let [params {:event_name "test_meter_event"
                  :type "cancel"
                  :cancel {:identifier "evt_test_id"}}
          response (adjustments/create-meter-event-adjustment stripe-client params)]
      (is (map? response))
      (is (= "billing.meter_event_adjustment" (:object response)))))) 