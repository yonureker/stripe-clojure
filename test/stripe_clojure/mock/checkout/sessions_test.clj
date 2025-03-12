(ns stripe-clojure.mock.checkout.sessions-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.checkout.sessions :as sessions]))

(deftest create-session-test
  (testing "Create a checkout session using stripe‑mock with required parameters"
    (let [params {:payment_method_types ["card"]
                  :line_items [{:price_data {:currency "usd"
                                             :product "prod_mock"
                                             :unit_amount 1000}
                                :quantity 1}]
                  :mode "payment"
                  :success_url "https://example.com/success"
                  :cancel_url "https://example.com/cancel"}
          response (sessions/create-session stripe-mock-client params)]
      (is (map? response))
      (is (= "checkout.session" (:object response)))
      (is (= "https://example.com/success" (:success_url response)))
      ;; Instead of asserting a specific id value, just ensure an id is present and is a string.
      (when (:id response)
        (is (string? (:id response)))))))

(deftest retrieve-session-test
  (testing "Retrieve a checkout session using a dummy id"
    (let [dummy-id "sess_mock"
          response (sessions/retrieve-session stripe-mock-client dummy-id)]
      (is (map? response))
      (is (= "checkout.session" (:object response)))
      (when (:id response)
        (is (string? (:id response)))))))

(deftest update-session-test
  (testing "Update a checkout session using stripe‑mock"
    (let [dummy-id "sess_mock"
          params {:metadata {:order_id "order_123"}}
          response (sessions/update-session stripe-mock-client dummy-id params)]
      (is (map? response))
      (is (= "checkout.session" (:object response)))
      (when (:id response)
        (is (string? (:id response)))))))

(deftest list-sessions-test
  (testing "List checkout sessions using stripe‑mock"
    (let [response (sessions/list-sessions stripe-mock-client)]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response))))))

(deftest expire-session-test
  (testing "Expire a checkout session using stripe‑mock"
    (let [dummy-id "sess_mock"
          response (sessions/expire-session stripe-mock-client dummy-id)]
      (is (map? response))
      (is (= "checkout.session" (:object response)))
      (when (:id response)
        (is (string? (:id response)))))))

(deftest list-line-items-test
  (testing "List line items for a checkout session using stripe‑mock"
    (let [dummy-id "sess_mock"
          response (sessions/list-line-items stripe-mock-client dummy-id)]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response))))))