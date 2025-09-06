(ns stripe-clojure.mock.account-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.account :as account]))

(deftest retrieve-account-test
  (testing "Retrieve account"
    (let [response (account/retrieve-account stripe-mock-client)]
      (is (map? response))
      (is (= "account" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :email)))))
