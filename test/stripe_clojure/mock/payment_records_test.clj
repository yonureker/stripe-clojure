(ns stripe-clojure.mock.payment-records-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.payment-records :as pr]))

(deftest report-payment-test
  (testing "Report payment"
    (let [params {:amount_requested {:currency "usd" :value 1000}
                  :payment_method_details {:type "card"}
                  :payment_reference "ref_123"}
          response (pr/report-payment stripe-mock-client params)]
      (is (map? response))
      (is (= "payment_record" (:object response)))
      (is (string? (:id response))))))

(deftest retrieve-payment-record-test
  (testing "Retrieve payment record"
    (let [response (pr/retrieve-payment-record stripe-mock-client "payrec_mock")]
      (is (map? response))
      (is (= "payment_record" (:object response)))
      (is (string? (:id response))))))

(deftest report-payment-attempt-test
  (testing "Report payment attempt"
    (let [params {:failed {:failed_at 1234567890}}
          response (pr/report-payment-attempt stripe-mock-client "payrec_mock" params)]
      (is (map? response))
      (is (= "payment_record" (:object response)))
      (is (string? (:id response))))))

(deftest report-payment-attempt-canceled-test
  (testing "Report payment attempt canceled"
    (let [params {:canceled_at 1234567890}
          response (pr/report-payment-attempt-canceled stripe-mock-client "payrec_mock" params)]
      (is (map? response))
      (is (= "payment_record" (:object response)))
      (is (string? (:id response))))))

(deftest report-payment-attempt-failed-test
  (testing "Report payment attempt failed"
    (let [params {:failed_at 1234567890}
          response (pr/report-payment-attempt-failed stripe-mock-client "payrec_mock" params)]
      (is (map? response))
      (is (= "payment_record" (:object response)))
      (is (string? (:id response))))))

(deftest report-payment-attempt-guaranteed-test
  (testing "Report payment attempt guaranteed"
    (let [params {:guaranteed_at 1234567890}
          response (pr/report-payment-attempt-guaranteed stripe-mock-client "payrec_mock" params)]
      (is (map? response))
      (is (= "payment_record" (:object response)))
      (is (string? (:id response))))))

(deftest report-payment-attempt-informational-test
  (testing "Report payment attempt informational"
    (let [params {}
          response (pr/report-payment-attempt-informational stripe-mock-client "payrec_mock" params)]
      (is (map? response))
      (is (= "payment_record" (:object response)))
      (is (string? (:id response))))))

(deftest report-refund-test
  (testing "Report refund"
    (let [params {:amount {:currency "usd" :value 500} :refund_id "ref_mock"}
          response (pr/report-refund stripe-mock-client "payrec_mock" params)]
      (is (map? response))
      (is (= "payment_record" (:object response)))
      (is (string? (:id response))))))
