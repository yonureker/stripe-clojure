(ns stripe-clojure.mock.forwarding.requests-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.forwarding.requests :as requests]))

(deftest create-request-test
  (testing "Create a financial connections request using stripe‑mock with required parameters"
    (let [params {:payment_method "pm_card_visa"
                  :replacements ["card_number", "card_expiry"]
                  :request {:url "https://example.com"}
                  :url "https://example.com"
                  :metadata {:order_id "1234567890"}}
          response (requests/create-request stripe-mock-client params)]
      (is (map? response))
      (is (= "forwarding.request" (:object response)))
      (is (string? (:id response))))))

(deftest retrieve-request-test
  (testing "Retrieve a financial connections request using a dummy request id"
    (let [dummy-id "req_mock"
          response (requests/retrieve-request stripe-mock-client dummy-id)]
      (is (map? response))
      (is (= "forwarding.request" (:object response)))
      (is (string? (:id response))))))

(deftest list-requests-test
  (testing "List financial connections requests using stripe‑mock"
    (let [response (requests/list-requests stripe-mock-client {})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [req (:data response)]
        (is (map? req))
        (is (string? (:id req))))))) 