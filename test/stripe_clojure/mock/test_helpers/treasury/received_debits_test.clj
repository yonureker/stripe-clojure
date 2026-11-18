(ns stripe-clojure.mock.test-helpers.treasury.received-debits-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.test-helpers.treasury.received-debits :as test-received-debits]))

(deftest create-received-debit-test
  (testing "Simulates a test mode received debit initiated by a third party"
    (let [response (test-received-debits/create-received-debit stripe-mock-client {:amount 1000 :currency "usd" :financial_account "fa_mock" :network "ach"})]
      (is (map? response))
      (is (= "treasury.received_debit" (:object response)))
      (is (string? (:id response))))))
