(ns stripe-clojure.mock.financial-connections.sessions-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.financial-connections.sessions :as sessions]))

(deftest create-session-test
  (testing "Create a financial connections session using stripe‑mock with required parameters"
    (let [params {:account_holder {:type "customer"}
                  :permissions ["balances" "transactions"]}
          response (sessions/create-session stripe-mock-client params)]
      (is (map? response))
      (is (= "financial_connections.session" (:object response)))
      (is (string? (:id response))))))

(deftest retrieve-session-test
  (testing "Retrieve a financial connections session using a dummy session id"
    (let [dummy-id "fcsess_mock"
          response (sessions/retrieve-session stripe-mock-client dummy-id)]
      (is (map? response))
      (is (= "financial_connections.session" (:object response)))
      (is (string? (:id response)))))) 