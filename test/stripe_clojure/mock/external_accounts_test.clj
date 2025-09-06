(ns stripe-clojure.mock.external-accounts-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.external-accounts :as external-accounts]))

(deftest retrieve-external-account-test
  (testing "Retrieve external account"
    (let [response (external-accounts/retrieve-external-account stripe-mock-client "ba_mock")]
      (is (map? response))
      ;; stripe-mock returns an error for this endpoint as it's not supported
      (is (= "invalid_request_error" (:type response)))
      (is (string? (:message response)))
      (is (contains? response :status)))))
