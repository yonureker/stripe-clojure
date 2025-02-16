(ns stripe-clojure.mock.billing.credit-balance-summary-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.billing.credit-balance-summary :as cbs]))

(deftest ^:integration retrieve-credit-balance-summary-test
  (testing "Retrieve credit balance summary for a customer"
    (let [params {:customer "mock"
                  :filter {:type "credit_grant"}}
          response (cbs/retrieve-credit-balance-summary stripe-client params)]
      (is (map? response))
      (is (= "billing.credit_balance_summary" (:object response)))))) 