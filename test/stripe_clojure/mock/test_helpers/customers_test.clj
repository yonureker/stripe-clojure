(ns stripe-clojure.mock.test-helpers.customers-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.test-helpers.customers :as test-customers]))

(deftest fund-cash-balance-test
  (testing "Fund cash balance"
    (let [params {:amount 1000 :currency "usd"}
          response (test-customers/fund-cash-balance stripe-mock-client "cus_mock" params)]
      (is (map? response))
      ;; stripe-mock may return nil for test helper endpoints
      (is (map? response)))))