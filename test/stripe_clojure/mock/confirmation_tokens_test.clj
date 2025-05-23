(ns stripe-clojure.mock.confirmation-tokens-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.confirmation-tokens :as ct]))

(deftest retrieve-confirmation-token-test
  (testing "Retrieve confirmation token"
    (let [response (ct/retrieve-confirmation-token stripe-mock-client "ct_mock")]
      (is (map? response))
      (is (= "confirmation_token" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :livemode))
      (is (boolean? (:livemode response)))
      (is (contains? response :created))
      (is (number? (:created response))))))
