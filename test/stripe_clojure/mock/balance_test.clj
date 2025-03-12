(ns stripe-clojure.mock.balance-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.balance :as balance]))

(deftest retrieve-balance-test
  (testing "Retrieve current account balance"
    (let [response (balance/retrieve-balance stripe-mock-client)]
      (is (map? response))
      (is (= "balance" (:object response)))
      (is (contains? response :available))
      (is (vector? (:available response)))
      (doseq [item (:available response)]
        (is (map? item))
        (is (contains? item :amount))
        (is (number? (:amount item))))
      (is (contains? response :pending))
      (is (vector? (:pending response)))
      (doseq [item (:pending response)]
        (is (map? item))
        (is (contains? item :amount))
        (is (number? (:amount item))))
      (is (contains? response :livemode))
      (is (boolean? (:livemode response))))))
