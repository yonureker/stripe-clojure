(ns stripe-clojure.mock.crypto.onramp-sessions-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.crypto.onramp-sessions :as onramp-sessions]))

(deftest create-onramp-session-test
  (testing "Create onramp session"
    (let [params {:transaction_details {:destination_currency "usd" :destination_exchange_amount 1000}}
          response (onramp-sessions/create-onramp-session stripe-mock-client params)]
      (is (map? response))
      ;; stripe-mock may return nil for crypto endpoints
      (is (map? response)))))

(deftest retrieve-onramp-session-test
  (testing "Retrieve onramp session"
    (let [response (onramp-sessions/retrieve-onramp-session stripe-mock-client "cors_mock")]
      (is (map? response))
      ;; stripe-mock may return nil for crypto endpoints
      (is (map? response)))))

(deftest list-onramp-sessions-test
  (testing "List onramp sessions"
    (let [response (onramp-sessions/list-onramp-sessions stripe-mock-client)]
      (is (map? response))
      ;; stripe-mock may return nil for crypto endpoints
      (is (map? response)))))
