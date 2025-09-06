(ns stripe-clojure.mock.link-account-sessions-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.link-account-sessions :as link-account-sessions]))

(deftest create-link-account-session-test
  (testing "Create link account session"
    (let [params {:account {:country "US" :type "express"}
                  :account_holder {:type "individual"}
                  :permissions {:balances :read :charges :write}
                  :return_url "https://example.com/return"}
          response (link-account-sessions/create-link-account-session stripe-mock-client params)]
      (is (map? response))
      ;; stripe-mock doesn't support create link account session
      (is (= "invalid_request_error" (:type response)))
      (is (string? (:message response))))))

(deftest retrieve-link-account-session-test
  (testing "Retrieve link account session"
    (let [response (link-account-sessions/retrieve-link-account-session stripe-mock-client "las_mock")]
      (is (map? response))
      ;; stripe-mock returns financial_connections.session instead of link_account_session
      (is (= "financial_connections.session" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :accounts))
      (is (contains? response :client_secret)))))
