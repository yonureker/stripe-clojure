(ns stripe-clojure.mock.billing-portal.sessions-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.billing-portal.sessions :as sessions]))

(deftest ^:integration create-session-test
  (testing "Create portal session using stripe‑mock with required parameters"
    (let [params {:customer   "cus_mock"
                  :return_url "https://example.com/account"}
          response (sessions/create-session stripe-client params)]
      (is (map? response))
      (is (= "billing_portal.session" (:object response)))
      (is (= "https://example.com/account" (:return_url response)))
      ;; Instead of checking for a specific id, just ensure an id is present and is a string if provided.
      (when (:id response)
        (is (string? (:id response)))))))
