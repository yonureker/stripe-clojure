(ns stripe-clojure.unit.http.request-test
  (:require [clojure.test :refer [deftest is testing]]
            [stripe-clojure.http.request :as request]))

;; Tests for build-request-headers

(deftest build-request-headers-test
  (testing "includes required headers"
    (let [headers (#'request/build-request-headers {:api-key "sk_test_123"
                                                    :api-version "2025-12-15"})]
      (is (= "Bearer sk_test_123" (get headers "authorization")))
      (is (= "application/x-www-form-urlencoded" (get headers "content-type")))
      (is (= "2025-12-15" (get headers "stripe-version")))))

  (testing "includes stripe-account header when provided"
    (let [headers (#'request/build-request-headers {:api-key "sk_test_123"
                                                    :api-version "2025-12-15"
                                                    :stripe-account "acct_123"})]
      (is (= "acct_123" (get headers "stripe-account")))))

  (testing "omits stripe-account header when not provided"
    (let [headers (#'request/build-request-headers {:api-key "sk_test_123"
                                                    :api-version "2025-12-15"})]
      (is (not (contains? headers "stripe-account")))))

  (testing "includes idempotency-key header when provided"
    (let [headers (#'request/build-request-headers {:api-key "sk_test_123"
                                                    :api-version "2025-12-15"
                                                    :idempotency-key "idem_123"})]
      (is (= "idem_123" (get headers "idempotency-key")))))

  (testing "includes test-clock header when provided"
    (let [headers (#'request/build-request-headers {:api-key "sk_test_123"
                                                    :api-version "2025-12-15"
                                                    :test-clock "clock_123"})]
      (is (= "clock_123" (get headers "stripe-test-clock")))))

  (testing "includes stripe-beta header when provided"
    (let [headers (#'request/build-request-headers {:api-key "sk_test_123"
                                                    :api-version "2025-12-15"
                                                    :stripe-beta "feature_v1=true"})]
      (is (= "feature_v1=true" (get headers "stripe-beta")))))

  (testing "omits stripe-beta header when not provided"
    (let [headers (#'request/build-request-headers {:api-key "sk_test_123"
                                                    :api-version "2025-12-15"})]
      (is (not (contains? headers "stripe-beta")))))

  (testing "merges custom-headers"
    (let [headers (#'request/build-request-headers {:api-key "sk_test_123"
                                                    :api-version "2025-12-15"
                                                    :custom-headers {"x-custom" "value"}})]
      (is (= "value" (get headers "x-custom")))))

  (testing "custom-headers can override defaults"
    (let [headers (#'request/build-request-headers {:api-key "sk_test_123"
                                                    :api-version "2025-12-15"
                                                    :custom-headers {"content-type" "application/json"}})]
      (is (= "application/json" (get headers "content-type"))))))

;; Tests for idempotent-method?

(deftest idempotent-method-test
  (testing "GET is idempotent"
    (is (some? (#'request/idempotent-method? :get))))

  (testing "PUT is idempotent"
    (is (some? (#'request/idempotent-method? :put))))

  (testing "DELETE is idempotent"
    (is (some? (#'request/idempotent-method? :delete))))

  (testing "POST is not idempotent"
    (is (nil? (#'request/idempotent-method? :post)))))

;; Tests for generate-idempotency-key

(deftest generate-idempotency-key-test
  (testing "generates UUID string"
    (let [key (request/generate-idempotency-key)]
      (is (string? key))
      (is (= 36 (count key)))  ; UUID format: 8-4-4-4-12
      (is (re-matches #"[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}" key))))

  (testing "generates unique keys"
    (let [keys (repeatedly 100 request/generate-idempotency-key)]
      (is (= 100 (count (set keys))) "All generated keys should be unique"))))
