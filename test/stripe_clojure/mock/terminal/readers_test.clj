(ns stripe-clojure.mock.terminal.readers-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.terminal.readers :as readers]))

(deftest create-reader-test
  (testing "Create terminal reader"
    (let [params {:registration_code "simulated-wpe"
                  :label "Blue Rabbit"
                  :location "tml_FDOtHwxAAdIJOh"}
          response (readers/create-reader stripe-mock-client params)]
      (is (map? response))
      (is (= "terminal.reader" (:object response)))
      (is (string? (:id response)))
      (is (= "Blue Rabbit" (:label response)))
      (is (= "tml_FDOtHwxAAdIJOh" (:location response)))
      (is (boolean? (:livemode response)))
      (is (string? (:device_type response)))
      (is (string? (:ip_address response)))
      (is (map? (:metadata response)))
      (is (string? (:serial_number response))))))

(deftest retrieve-reader-test
  (testing "Retrieve terminal reader"
    (let [response (readers/retrieve-reader stripe-mock-client "tmr_mock")]
      (is (map? response))
      (is (= "terminal.reader" (:object response)))
      (is (string? (:id response)))
      (is (string? (:label response)))
      (is (string? (:device_type response)))
      (is (string? (:ip_address response))))))

(deftest update-reader-test
  (testing "Update terminal reader"
    (let [params {:label "Updated Reader"}
          response (readers/update-reader stripe-mock-client "tmr_mock" params)]
      (is (map? response))
      (is (= "terminal.reader" (:object response)))
      (is (string? (:id response))))))

(deftest list-readers-test
  (testing "List terminal readers"
    (let [response (readers/list-readers stripe-mock-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [r (:data response)]
        (is (map? r))
        (is (= "terminal.reader" (:object r)))
        (is (string? (:id r)))
        (is (string? (:label r)))))))

(deftest delete-reader-test
  (testing "Delete terminal reader"
    (let [response (readers/delete-reader stripe-mock-client "tmr_mock")]
      (is (map? response))
      (is (= "terminal.reader" (:object response)))
      (is (string? (:id response)))
      (is (true? (:deleted response))))))

(deftest cancel-action-test
  (testing "Cancel terminal reader action"
    (let [response (readers/cancel-action stripe-mock-client "tmr_mock")]
      (is (map? response))
      (is (= "terminal.reader" (:object response)))
      (is (string? (:id response))))))

(deftest process-payment-intent-test
  (testing "Process payment intent on terminal reader"
    (let [params {:payment_intent "pi_mock"}
          response (readers/process-payment-intent stripe-mock-client "tmr_mock" params)]
      (is (map? response))
      (is (= "terminal.reader" (:object response)))
      (is (string? (:id response))))))

(deftest process-setup-intent-test
  (testing "Process setup intent on terminal reader"
    (let [params {:allow_redisplay "always" :setup_intent "si_mock"}
          response (readers/process-setup-intent stripe-mock-client "tmr_mock" params)]
      (is (map? response))
      (is (= "terminal.reader" (:object response)))
      (is (string? (:id response))))))

(deftest refund-payment-test
  (testing "Refund payment on terminal reader"
    (let [params {:payment_intent "pi_mock" :amount 100}
          response (readers/refund-payment stripe-mock-client "tmr_mock" params)]
      (is (map? response))
      (is (= "terminal.reader" (:object response)))
      (is (string? (:id response))))))

(deftest set-reader-display-test
  (testing "Set terminal reader display"
    (let [params {:type "cart"}
          response (readers/set-reader-display stripe-mock-client "tmr_mock" params)]
      (is (map? response))
      (is (= "terminal.reader" (:object response)))
      (is (string? (:id response)))))) 