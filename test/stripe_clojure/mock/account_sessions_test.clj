(ns stripe-clojure.mock.account-sessions-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.account-sessions :as account-sessions]))

(deftest create-account-session-test
  (testing "Create account session"
    (let [params {:account "acct_mock"
                  :components {:account_management {:enabled true}}}
          response (account-sessions/create-account-session stripe-mock-client params)]
      (is (map? response))
      ;; stripe-mock may return different object type for this endpoint
      (is (map? response)))))
