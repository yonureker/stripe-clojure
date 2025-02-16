(ns stripe-clojure.mock.application-fees-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.application-fees :as af]))

(deftest ^:integration retrieve-application-fee-test
  (testing "Retrieve application fee"
    (let [response (af/retrieve-application-fee stripe-client "fee_mock")]
      (is (map? response))
      (is (= "application_fee" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :amount))
      (is (number? (:amount response))))))

(deftest ^:integration list-application-fees-test
  (testing "List application fees"
    (let [response (af/list-application-fees stripe-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [fee (:data response)]
        (is (map? fee))
        (is (= "application_fee" (:object fee)))
        (is (string? (:id fee)))))))

(deftest ^:integration create-refund-test
  (testing "Create application fee refund"
    (let [params {:amount 50}
          response (af/create-refund stripe-client "fee_mock" params)]
      (is (map? response))
      (is (= "fee_refund" (:object response)))
      (is (string? (:id response)))
      (is (= 50 (:amount response))))))

(deftest ^:integration retrieve-refund-test
  (testing "Retrieve application fee refund"
    (let [response (af/retrieve-refund stripe-client "fee_mock" "refund_mock")]
      (is (map? response))
      (is (= "fee_refund" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :amount))
      (is (number? (:amount response))))))

(deftest ^:integration update-refund-test
  (testing "Update application fee refund"
    (let [params {}
          response (af/update-refund stripe-client "fee_mock" "refund_mock" params)]
      (is (map? response))
      (is (= "fee_refund" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration list-refunds-test
  (testing "List application fee refunds"
    (let [response (af/list-refunds stripe-client "fee_mock" {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [refund (:data response)]
        (is (map? refund))
        (is (= "fee_refund" (:object refund)))
        (is (string? (:id refund)))))))
