(ns stripe-clojure.mock.payment-attempt-records-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.payment-attempt-records :as par]))

(deftest list-payment-attempt-records-test
  (testing "List payment attempt records"
    (let [params {:payment_record "payrec_mock"}
          response (par/list-payment-attempt-records stripe-mock-client params)]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response))))))

(deftest retrieve-payment-attempt-record-test
  (testing "Retrieve payment attempt record"
    (let [response (par/retrieve-payment-attempt-record stripe-mock-client "par_mock")]
      (is (map? response))
      (is (= "payment_attempt_record" (:object response)))
      (is (string? (:id response))))))
