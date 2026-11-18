(ns stripe-clojure.mock.test-helpers.treasury.received-credits-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.test-helpers.treasury.received-credits :as test-received-credits]))

(deftest create-received-credit-test
  (testing "Simulates a test mode received credit initiated by a third party"
    (let [response (test-received-credits/create-received-credit stripe-mock-client {:amount 1000 :currency "usd" :financial_account "fa_mock" :network "ach"})]
      (is (map? response))
      (is (= "treasury.received_credit" (:object response)))
      (is (string? (:id response))))))
