(ns stripe-clojure.unit.schemas.request-test
  (:require [clojure.test :refer [deftest is testing]]
            [malli.core :as m]
            [stripe-clojure.schemas.request :as request-schema]))

;; Helper to validate opts against RequestOpts schema
(defn- valid? [opts]
  (m/validate request-schema/RequestOpts opts))

(defn- invalid? [opts]
  (not (valid? opts)))

;; -----------------------------------------------------------------------------
;; Empty / minimal opts
;; -----------------------------------------------------------------------------

(deftest empty-opts-test
  (testing "empty map is valid (all fields optional)"
    (is (valid? {}))))

;; -----------------------------------------------------------------------------
;; SharedOptions fields (inherited from common schema)
;; -----------------------------------------------------------------------------

(deftest shared-options-test
  (testing "api-version accepts string"
    (is (valid? {:api-version "2026-01-28.clover"})))

  (testing "api-version rejects non-string"
    (is (invalid? {:api-version 123})))

  (testing "stripe-account accepts string"
    (is (valid? {:stripe-account "acct_123"})))

  (testing "max-network-retries accepts non-negative int"
    (is (valid? {:max-network-retries 0}))
    (is (valid? {:max-network-retries 3})))

  (testing "max-network-retries rejects negative int"
    (is (invalid? {:max-network-retries -1})))

  (testing "max-network-retries rejects non-int"
    (is (invalid? {:max-network-retries 1.5})))

  (testing "timeout accepts non-negative int"
    (is (valid? {:timeout 0}))
    (is (valid? {:timeout 80000})))

  (testing "timeout rejects negative int"
    (is (invalid? {:timeout -100})))

  (testing "full-response? accepts boolean"
    (is (valid? {:full-response? true}))
    (is (valid? {:full-response? false})))

  (testing "full-response? rejects non-boolean"
    (is (invalid? {:full-response? "yes"}))))

;; -----------------------------------------------------------------------------
;; V1 expand field
;; -----------------------------------------------------------------------------

(deftest expand-test
  (testing "expand accepts single string"
    (is (valid? {:expand "customer"})))

  (testing "expand accepts vector of strings"
    (is (valid? {:expand ["customer" "invoice.subscription"]})))

  (testing "expand rejects non-string values in vector"
    (is (invalid? {:expand [123]})))

  (testing "expand rejects integer"
    (is (invalid? {:expand 123}))))

;; -----------------------------------------------------------------------------
;; V2 include field
;; -----------------------------------------------------------------------------

(deftest include-test
  (testing "include accepts single string"
    (is (valid? {:include "data.payload"})))

  (testing "include accepts vector of strings"
    (is (valid? {:include ["data.payload" "data.detail"]})))

  (testing "include rejects non-string values in vector"
    (is (invalid? {:include [123]})))

  (testing "include rejects integer"
    (is (invalid? {:include 42}))))

;; -----------------------------------------------------------------------------
;; auto-paginate?
;; -----------------------------------------------------------------------------

(deftest auto-paginate-test
  (testing "auto-paginate? accepts boolean"
    (is (valid? {:auto-paginate? true}))
    (is (valid? {:auto-paginate? false})))

  (testing "auto-paginate? rejects non-boolean"
    (is (invalid? {:auto-paginate? "true"}))))

;; -----------------------------------------------------------------------------
;; idempotency-key
;; -----------------------------------------------------------------------------

(deftest idempotency-key-test
  (testing "idempotency-key accepts string"
    (is (valid? {:idempotency-key "idem_abc123"})))

  (testing "idempotency-key rejects non-string"
    (is (invalid? {:idempotency-key 123}))))

;; -----------------------------------------------------------------------------
;; test-clock
;; -----------------------------------------------------------------------------

(deftest test-clock-test
  (testing "test-clock accepts string"
    (is (valid? {:test-clock "clock_123"})))

  (testing "test-clock rejects non-string"
    (is (invalid? {:test-clock 123}))))

;; -----------------------------------------------------------------------------
;; stripe-beta
;; -----------------------------------------------------------------------------

(deftest stripe-beta-test
  (testing "stripe-beta accepts string"
    (is (valid? {:stripe-beta "feature_v1=true"})))

  (testing "stripe-beta rejects non-string"
    (is (invalid? {:stripe-beta true}))))

;; -----------------------------------------------------------------------------
;; custom-headers
;; -----------------------------------------------------------------------------

(deftest custom-headers-test
  (testing "custom-headers accepts map of string to string"
    (is (valid? {:custom-headers {"Authorization" "Bearer tok_123"}}))
    (is (valid? {:custom-headers {"X-Custom" "value" "X-Other" "value2"}})))

  (testing "custom-headers accepts empty map"
    (is (valid? {:custom-headers {}})))

  (testing "custom-headers rejects keyword keys"
    (is (invalid? {:custom-headers {:auth "Bearer tok"}})))

  (testing "custom-headers rejects non-string values"
    (is (invalid? {:custom-headers {"X-Custom" 123}}))))

;; -----------------------------------------------------------------------------
;; base-url
;; -----------------------------------------------------------------------------

(deftest base-url-test
  (testing "base-url accepts string"
    (is (valid? {:base-url "https://custom.stripe.com"})))

  (testing "base-url rejects non-string"
    (is (invalid? {:base-url 123}))))

;; -----------------------------------------------------------------------------
;; multipart
;; -----------------------------------------------------------------------------

(deftest multipart-test
  (testing "multipart accepts valid parts with required fields"
    (is (valid? {:multipart [{:name "file" :content "data"}]})))

  (testing "multipart accepts parts with all optional fields"
    (is (valid? {:multipart [{:name "file"
                              :content "data"
                              :content-type "application/pdf"
                              :file-name "doc.pdf"}]})))

  (testing "multipart accepts multiple parts"
    (is (valid? {:multipart [{:name "purpose" :content "dispute_evidence"}
                             {:name "file" :content "binary-data"
                              :content-type "image/png"}]})))

  (testing "multipart rejects parts missing :name"
    (is (invalid? {:multipart [{:content "data"}]})))

  (testing "multipart rejects parts missing :content"
    (is (invalid? {:multipart [{:name "file"}]})))

  (testing "multipart rejects non-vector"
    (is (invalid? {:multipart {:name "file" :content "data"}}))))

;; -----------------------------------------------------------------------------
;; Closed schema - unknown keys rejected
;; -----------------------------------------------------------------------------

(deftest closed-schema-test
  (testing "rejects unknown keys"
    (is (invalid? {:unknown-key "value"}))
    (is (invalid? {:api-version "v1" :not-a-real-key true}))))

;; -----------------------------------------------------------------------------
;; Combined options
;; -----------------------------------------------------------------------------

(deftest combined-options-test
  (testing "accepts all valid v1 options together"
    (is (valid? {:api-version "2026-01-28.clover"
                 :stripe-account "acct_123"
                 :max-network-retries 2
                 :timeout 30000
                 :full-response? true
                 :idempotency-key "idem_123"
                 :expand ["customer" "invoice"]
                 :auto-paginate? false
                 :test-clock "clock_123"
                 :stripe-beta "feature=v1"
                 :custom-headers {"X-Request-Id" "req_123"}
                 :base-url "https://api.stripe.com"})))

  (testing "accepts all valid v2 options together"
    (is (valid? {:api-version "2026-01-28.clover"
                 :include ["data.payload" "data.detail"]
                 :auto-paginate? true
                 :idempotency-key "idem_456"
                 :full-response? false})))

  (testing "accepts multipart with other options"
    (is (valid? {:stripe-account "acct_123"
                 :multipart [{:name "file"
                              :content "data"
                              :content-type "application/pdf"
                              :file-name "report.pdf"}
                             {:name "purpose"
                              :content "dispute_evidence"}]}))))
