(ns stripe-clojure.mock.test-helpers.terminal.readers-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.test-helpers.terminal.readers :as test-readers]))

(deftest present-payment-method-test
  (testing "Presents a payment method on a simulated reader"
    (let [response (test-readers/present-payment-method stripe-mock-client "tmr_mock")]
      (is (map? response))
      (is (= "terminal.reader" (:object response)))
      (is (string? (:id response))))))

(deftest succeed-input-collection-test
  (testing "Triggers a successful input collection on a simulated reader"
    (let [response (test-readers/succeed-input-collection stripe-mock-client "tmr_mock")]
      (is (map? response))
      (is (= "terminal.reader" (:object response)))
      (is (string? (:id response))))))

(deftest timeout-input-collection-test
  (testing "Completes an input collection with a timeout error on a simulated reader"
    (let [response (test-readers/timeout-input-collection stripe-mock-client "tmr_mock")]
      (is (map? response))
      (is (= "terminal.reader" (:object response)))
      (is (string? (:id response))))))
