(ns stripe-clojure.mock.account-links-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.account-links :as account-links]))

(deftest create-account-link-mock-test
  (testing "Create a new account link using stripe-mock"
    (let [params {:account      "acct_mock"
                  :refresh_url  "http://localhost:12111/refresh"
                  :return_url   "http://localhost:12111/return"
                  :type         "account_onboarding"}
          response (account-links/create-account-link stripe-client params)]
      (is (= "account_link" (:object response)))
      (is (string? (:url response))))))