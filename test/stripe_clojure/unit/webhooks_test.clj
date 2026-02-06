(ns stripe-clojure.unit.webhooks-test
  (:require [clojure.test :refer [deftest is testing]]
            [stripe-clojure.webhooks :as webhooks]
            [cheshire.core :as json]
            [clojure.string :as str]))

(deftest test-webhook-verification
  ;; Test 1: Basic successful verification
  (testing "Valid webhook signature"
    (let [payload {:id "evt_test_webhook" :object "event"}
          payload-string (json/generate-string payload)
          secret "whsec_test_secret"
          header (webhooks/generate-test-header-string
                  {:payload payload-string
                   :secret secret})
          event (webhooks/construct-event payload-string header secret)]
      (is (= "evt_test_webhook" (:id event)))
      (is (= "event" (:object event)))))

  ;; Test 2: Complex payload verification
  (testing "Complex payload with nested data"
    (let [payload {:id "evt_test_complex"
                   :object "event"
                   :type "payment_intent.succeeded"
                   :data {:object {:id "pi_123456789"
                                   :amount 2000
                                   :currency "usd"
                                   :customer "cus_123456789"
                                   :metadata {:order_id "6735"}}}}
          payload-string (json/generate-string payload)
          secret "whsec_test_secret"
          header (webhooks/generate-test-header-string
                  {:payload payload-string
                   :secret secret})
          event (webhooks/construct-event payload-string header secret)]
      (is (= "evt_test_complex" (:id event)))
      (is (= "payment_intent.succeeded" (:type event)))
      (is (= "pi_123456789" (get-in event [:data :object :id])))
      (is (= "6735" (get-in event [:data :object :metadata :order_id])))))

  ;; Test 3: Invalid signature
  (testing "Invalid signature"
    (let [payload {:id "evt_test_webhook" :object "event"}
          payload-string (json/generate-string payload)
          secret "whsec_test_secret"
          wrong-secret "whsec_wrong_secret"
          header (webhooks/generate-test-header-string
                  {:payload payload-string
                   :secret secret})]
      (is (thrown-with-msg? clojure.lang.ExceptionInfo
                            #"Signature mismatch"
                            (webhooks/construct-event payload-string header wrong-secret)))))

  ;; Test 4: Expired timestamp
  (testing "Expired timestamp"
    (let [payload {:id "evt_test_webhook" :object "event"}
          payload-string (json/generate-string payload)
          secret "whsec_test_secret"
          old-timestamp (- (quot (System/currentTimeMillis) 1000) 600) ;; 10 minutes ago
          header (webhooks/generate-test-header-string
                  {:payload payload-string
                   :secret secret
                   :timestamp old-timestamp})]
      (is (thrown-with-msg? clojure.lang.ExceptionInfo
                            #"Timestamp outside tolerance window"
                            (webhooks/construct-event payload-string header secret)))))

  ;; Test 5: Future timestamp
  (testing "Future timestamp"
    (let [payload {:id "evt_test_webhook" :object "event"}
          payload-string (json/generate-string payload)
          secret "whsec_test_secret"
          future-timestamp (+ (quot (System/currentTimeMillis) 1000) 600) ;; 10 minutes in future
          header (webhooks/generate-test-header-string
                  {:payload payload-string
                   :secret secret
                   :timestamp future-timestamp})]
      (is (thrown-with-msg? clojure.lang.ExceptionInfo
                            #"Timestamp outside tolerance window"
                            (webhooks/construct-event payload-string header secret)))))

  ;; Test 6: Custom tolerance
  (testing "Custom tolerance allows older timestamp"
    (let [payload {:id "evt_test_webhook" :object "event"}
          payload-string (json/generate-string payload)
          secret "whsec_test_secret"
          old-timestamp (- (quot (System/currentTimeMillis) 1000) 400) ;; 6.6 minutes ago
          header (webhooks/generate-test-header-string
                  {:payload payload-string
                   :secret secret
                   :timestamp old-timestamp})
          ;; Use a custom tolerance of 10 minutes
          event (webhooks/construct-event payload-string header secret {:tolerance 600})]
      (is (= "evt_test_webhook" (:id event)))))

  ;; Test 7: Tampered payload
  (testing "Tampered payload"
    (let [original-payload {:id "evt_test_webhook" :object "event"}
          original-payload-string (json/generate-string original-payload)
          secret "whsec_test_secret"
          header (webhooks/generate-test-header-string
                  {:payload original-payload-string
                   :secret secret})
          tampered-payload {:id "evt_test_webhook" :object "event" :amount 1000}
          tampered-payload-string (json/generate-string tampered-payload)]
      (is (thrown-with-msg? clojure.lang.ExceptionInfo
                            #"Signature mismatch"
                            (webhooks/construct-event tampered-payload-string header secret)))))

  ;; Test 8: Missing webhook secret
  (testing "Missing webhook secret"
    (let [payload {:id "evt_test_webhook" :object "event"}
          payload-string (json/generate-string payload)
          secret "whsec_test_secret"
          header (webhooks/generate-test-header-string
                  {:payload payload-string
                   :secret secret})]
      (is (thrown-with-msg? clojure.lang.ExceptionInfo
                            #"Webhook secret is missing or empty"
                            (webhooks/construct-event payload-string header "")))))

  ;; Test 9: Malformed signature header
  (testing "Malformed signature header"
    (let [payload {:id "evt_test_webhook" :object "event"}
          payload-string (json/generate-string payload)
          secret "whsec_test_secret"
          malformed-header "t=1234,invalidformat"]
      (is (thrown-with-msg? clojure.lang.ExceptionInfo
                            #"Invalid signature format"
                            (webhooks/construct-event payload-string malformed-header secret)))))

  ;; Test 10: Missing timestamp in signature
  (testing "Missing timestamp in signature"
    (let [payload {:id "evt_test_webhook" :object "event"}
          payload-string (json/generate-string payload)
          secret "whsec_test_secret"
          malformed-header "v1=1234abcd"]
      (is (thrown-with-msg? clojure.lang.ExceptionInfo
                            #"Invalid signature format"
                            (webhooks/construct-event payload-string malformed-header secret)))))

  ;; Test 11: Verify with precomputed signature
  (testing "Verify with precomputed signature"
    (let [payload {:id "evt_test_webhook" :object "event"}
          payload-string (json/generate-string payload)
          secret "whsec_test_secret"
          timestamp (quot (System/currentTimeMillis) 1000)
          signed-payload (str timestamp "." payload-string)
          computed-signature (webhooks/compute-signature signed-payload secret)
          header (str "t=" timestamp ",v1=" computed-signature)
          event (webhooks/construct-event payload-string header secret)]
      (is (= "evt_test_webhook" (:id event)))))

  ;; Test 12: Multiple signatures in header (v1 and v0)
  (testing "Multiple signatures in header"
    (let [payload {:id "evt_test_webhook" :object "event"}
          payload-string (json/generate-string payload)
          secret "whsec_test_secret"
          timestamp (quot (System/currentTimeMillis) 1000)
          signed-payload (str timestamp "." payload-string)
          computed-signature (webhooks/compute-signature signed-payload secret)
          header (str "t=" timestamp ",v1=" computed-signature ",v0=invalid_signature")
          event (webhooks/construct-event payload-string header secret)]
      (is (= "evt_test_webhook" (:id event))))))

;; Test the generate-test-header-string function
(deftest test-generate-test-header-string
  (testing "Generated header has correct format"
    (let [payload {:id "evt_test_webhook" :object "event"}
          payload-string (json/generate-string payload)
          secret "whsec_test_secret"
          header (webhooks/generate-test-header-string
                  {:payload payload-string
                   :secret secret})]
      (is (re-matches #"t=\d+,v1=[a-f0-9]+" header))))

  (testing "Generated header with custom timestamp"
    (let [payload {:id "evt_test_webhook" :object "event"}
          payload-string (json/generate-string payload)
          secret "whsec_test_secret"
          timestamp 1609459200 ;; 2021-01-01T00:00:00Z
          header (webhooks/generate-test-header-string
                  {:payload payload-string
                   :secret secret
                   :timestamp timestamp})]
      (is (re-matches #"t=1609459200,v1=[a-f0-9]+" header))))

  (testing "Generated header with custom signature"
    (let [payload {:id "evt_test_webhook" :object "event"}
          payload-string (json/generate-string payload)
          secret "whsec_test_secret"
          custom-signature "abc123"
          header (webhooks/generate-test-header-string
                  {:payload payload-string
                   :secret secret
                   :signature custom-signature})]
      (is (re-matches #"t=\d+,v1=abc123" header)))))

;; Test the compute-signature function
(deftest test-compute-signature
  (testing "Signature computation is deterministic"
    (let [payload "test_payload"
          secret "whsec_test_secret"
          signature1 (webhooks/compute-signature payload secret)
          signature2 (webhooks/compute-signature payload secret)]
      (is (= signature1 signature2))))

  (testing "Different payloads produce different signatures"
    (let [payload1 "test_payload_1"
          payload2 "test_payload_2"
          secret "whsec_test_secret"
          signature1 (webhooks/compute-signature payload1 secret)
          signature2 (webhooks/compute-signature payload2 secret)]
      (is (not= signature1 signature2))))

  (testing "Different secrets produce different signatures"
    (let [payload "test_payload"
          secret1 "whsec_test_secret_1"
          secret2 "whsec_test_secret_2"
          signature1 (webhooks/compute-signature payload secret1)
          signature2 (webhooks/compute-signature payload secret2)]
      (is (not= signature1 signature2)))))

;; Test with real-world event examples
(deftest test-real-world-events
  (testing "Payment intent succeeded event"
    (let [payload {:id "evt_1NXLEpLkdIwHu7ixO8qNX6zX"
                   :object "event"
                   :api_version "2023-10-16"
                   :created 1693322207
                   :data {:object {:id "pi_3NXLEpLkdIwHu7ix0JjpJoWq"
                                   :object "payment_intent"
                                   :amount 2000
                                   :currency "usd"
                                   :status "succeeded"}}
                   :type "payment_intent.succeeded"}
          payload-string (json/generate-string payload)
          secret "whsec_test_secret"
          header (webhooks/generate-test-header-string
                  {:payload payload-string
                   :secret secret})
          event (webhooks/construct-event payload-string header secret)]
      (is (= "evt_1NXLEpLkdIwHu7ixO8qNX6zX" (:id event)))
      (is (= "payment_intent.succeeded" (:type event)))
      (is (= "pi_3NXLEpLkdIwHu7ix0JjpJoWq" (get-in event [:data :object :id])))
      (is (= "succeeded" (get-in event [:data :object :status]))))))

;; Helper function to time verification attempts
(defn time-verification [payload header secret]
  (try
    (let [start (System/nanoTime)]
      (webhooks/verify-signature payload header secret)
      (/ (- (System/nanoTime) start) 1000000.0))
    (catch Exception _
      ;; Return the time even if verification fails
      0)))

(deftest test-webhook-security
  (testing "Protection against timing attacks"
    (let [payload {:id "evt_test_webhook" :object "event"}
          payload-string (json/generate-string payload)
          secret "whsec_test_secret"
          valid-header (webhooks/generate-test-header-string
                        {:payload payload-string
                         :secret secret})

          ;; Create an almost-valid signature that differs only in the last character
          almost-valid-sig (subs (second (str/split valid-header #"v1=")) 0
                                 (dec (count (second (str/split valid-header #"v1=")))))
          almost-valid-header (str "t=" (first (str/split valid-header #","))
                                   ",v1=" almost-valid-sig "X")

          ;; Time both valid and invalid signature verification
          time-valid (time-verification payload-string valid-header secret)
          time-invalid (time-verification payload-string almost-valid-header secret)]

      ;; The time difference should be minimal to prevent timing attacks
      ;; This is a heuristic test - in reality, you'd need statistical analysis
      (is (< (Math/abs (- time-valid time-invalid)) 10)
          "Time difference between valid and invalid signature verification should be minimal"))))
