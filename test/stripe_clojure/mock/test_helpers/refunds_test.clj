(ns stripe-clojure.mock.test-helpers.refunds-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.test-helpers.refunds :as test-refunds]))

(deftest expire-refund-test
  (testing "Expires a refund with a status of requires_action"
    (let [response (test-refunds/expire-refund stripe-mock-client "re_mock")]
      (is (map? response))
      (is (= "refund" (:object response)))
      (is (string? (:id response))))))
