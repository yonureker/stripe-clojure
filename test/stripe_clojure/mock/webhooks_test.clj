(ns stripe-clojure.mock.webhooks-test
  (:require [clojure.test :refer :all]
            [cheshire.core :as json]
            [stripe-clojure.webhooks :as wh]))

;; Define a test webhook secret.
(def test-webhook-secret "whsec_test_secret")

(deftest verify-signature-valid-test
  (testing "verify-signature returns true for a valid signature"
    (let [payload (json/generate-string {:id "evt_test_webhook" :object "event"})
          header  (wh/generate-test-header-string {:payload payload :secret test-webhook-secret})]
      (is (true? (wh/verify-signature payload header test-webhook-secret))))))

(deftest verify-signature-invalid-secret-test
  (testing "verify-signature throws an exception when given an invalid secret"
    (let [payload (json/generate-string {:id "evt_test_webhook" :object "event"})
          wrong-secret "wrong_secret"
          header (wh/generate-test-header-string {:payload payload :secret test-webhook-secret})]
      (is (thrown-with-msg? clojure.lang.ExceptionInfo #"Signature mismatch"
                            (wh/verify-signature payload header wrong-secret))))))

(deftest verify-signature-invalid-format-test
  (testing "verify-signature throws an exception when the header format is invalid"
    (let [payload (json/generate-string {:id "evt_test_webhook" :object "event"})
          header "invalid_header"]
      (is (thrown-with-msg? clojure.lang.ExceptionInfo #"Invalid signature format"
                            (wh/verify-signature payload header test-webhook-secret))))))

(deftest verify-signature-timestamp-mismatch-test
  (testing "verify-signature throws an exception when timestamp is outside tolerance"
    (let [payload (json/generate-string {:id "evt_test_webhook" :object "event"})
          old-timestamp (- (quot (System/currentTimeMillis) 1000) 1000) ; simulate an old timestamp
          header (wh/generate-test-header-string {:payload payload :secret test-webhook-secret :timestamp old-timestamp})
          tolerance 300]
      (is (thrown-with-msg? clojure.lang.ExceptionInfo #"Timestamp outside tolerance window"
                            (wh/verify-signature payload header test-webhook-secret {:tolerance tolerance}))))))

(deftest test-construct-event-valid
  (testing "construct-event returns the event map for a valid signature"
    (let [payload (json/generate-string {:id "evt_test_webhook" :object "event"})
          header  (wh/generate-test-header-string {:payload payload :secret test-webhook-secret})
          event   (wh/construct-event payload header test-webhook-secret)]
      (is (= "evt_test_webhook" (:id event))))))

(deftest test-construct-event-invalid-signature
  (testing "construct-event throws an exception when the signature is invalid"
    (let [payload (json/generate-string {:id "evt_invalid" :object "event"})
          ts (str (quot (System/currentTimeMillis) 1000))
          signing-string (str ts "." payload)
          signature (wh/compute-signature signing-string "wrong_secret")
          header (str "t=" ts ",v1=" signature)]
      (is (thrown-with-msg? Exception #"Signature mismatch"
                            (wh/construct-event payload header test-webhook-secret))))))

(deftest test-invalid-signature-format
  (testing "construct-event throws an exception when the header format is invalid"
    (let [payload (json/generate-string {:id "evt_test" :object "event"})
          header "invalid_header"]
      (is (thrown-with-msg? Exception #"Invalid signature format"
                            (wh/construct-event payload header test-webhook-secret))))))

(deftest test-timestamp-outside-tolerance
  (testing "construct-event throws an exception when timestamp is outside tolerance"
    (let [payload (json/generate-string {:id "evt_old_timestamp" :object "event"})
          old-ts (str (- (quot (System/currentTimeMillis) 1000) 1000))
          signing-string (str old-ts "." payload)
          signature (wh/compute-signature signing-string test-webhook-secret)
          header (str "t=" old-ts ",v1=" signature)
          tolerance 300]
      (is (thrown-with-msg? Exception #"Timestamp outside tolerance window"
                            (wh/construct-event payload header test-webhook-secret {:tolerance tolerance})))))) 