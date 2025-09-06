(ns stripe-clojure.mock.test-helpers.confirmation-tokens-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.test-helpers.confirmation-tokens :as confirmation-tokens]))

(deftest create-confirmation-token-test
  (testing "Create confirmation token"
    (let [params {:payment_method_data {:type "card"
                                        :card {:number "4242424242424242"
                                               :exp_month 12
                                               :exp_year 2025
                                               :cvc "123"}}}
          response (confirmation-tokens/create-confirmation-token stripe-mock-client params)]
      (is (map? response))
      ;; stripe-mock may return nil for test helper endpoints
      (is (map? response)))))