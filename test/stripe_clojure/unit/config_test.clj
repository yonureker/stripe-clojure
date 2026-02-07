(ns stripe-clojure.unit.config-test
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.string :as str]
            [stripe-clojure.config :as config]))

;; -----------------------------------------------------------------------------
;; mask-api-key
;; -----------------------------------------------------------------------------

(deftest mask-api-key-test
  (testing "masks middle portion of a standard test key"
    (is (= "sk_test...glkf" (config/mask-api-key "sk_test_asdasdasadsad9Rglkf"))))

  (testing "masks middle portion of a live key"
    (is (= "sk_live...abcd" (config/mask-api-key "sk_live_longkeyvalueabcd"))))

  (testing "preserves first 7 characters"
    (let [result (config/mask-api-key "sk_test_abcdefghijklmnop")]
      (is (str/starts-with? result "sk_test"))))

  (testing "preserves last 4 characters"
    (let [result (config/mask-api-key "sk_test_abcdefghijklmnop")]
      (is (str/ends-with? result "mnop"))))

  (testing "contains ellipsis separator"
    (is (str/includes? (config/mask-api-key "sk_test_abcdefghijklmnop") "...")))

  (testing "returns nil for nil input"
    (is (nil? (config/mask-api-key nil)))))

;; -----------------------------------------------------------------------------
;; V1 endpoint configuration
;; -----------------------------------------------------------------------------

(deftest stripe-endpoints-test
  (testing "all v1 endpoints start with /v1/"
    (doseq [[k v] config/stripe-endpoints]
      (is (str/starts-with? v "/v1/")
          (str "Endpoint " k " should start with /v1/, got: " v))))

  (testing "all v1 endpoint values are strings"
    (doseq [[k v] config/stripe-endpoints]
      (is (string? v) (str "Endpoint " k " should be a string"))))

  (testing "v1 endpoints map is non-empty"
    (is (pos? (count config/stripe-endpoints)))))

;; -----------------------------------------------------------------------------
;; V2 endpoint configuration
;; -----------------------------------------------------------------------------

(deftest stripe-v2-endpoints-test
  (testing "all v2 endpoints start with /v2/"
    (doseq [[k v] config/stripe-v2-endpoints]
      (is (str/starts-with? v "/v2/")
          (str "Endpoint " k " should start with /v2/, got: " v))))

  (testing "all v2 endpoint values are strings"
    (doseq [[k v] config/stripe-v2-endpoints]
      (is (string? v) (str "Endpoint " k " should be a string"))))

  (testing "all v2 endpoint keys are namespaced with v2- prefix"
    (doseq [[k _] config/stripe-v2-endpoints]
      (is (str/starts-with? (name k) "v2-")
          (str "V2 endpoint key " k " should start with v2-"))))

  (testing "contains expected core endpoints"
    (is (contains? config/stripe-v2-endpoints :v2-core-accounts))
    (is (contains? config/stripe-v2-endpoints :v2-core-events))
    (is (contains? config/stripe-v2-endpoints :v2-core-event-destinations)))

  (testing "contains expected billing endpoints"
    (is (contains? config/stripe-v2-endpoints :v2-billing-meter-events))
    (is (contains? config/stripe-v2-endpoints :v2-billing-meter-event-session))
    (is (contains? config/stripe-v2-endpoints :v2-billing-meter-event-stream))
    (is (contains? config/stripe-v2-endpoints :v2-billing-meter-event-adjustments))))

;; -----------------------------------------------------------------------------
;; default-client-config
;; -----------------------------------------------------------------------------

(deftest default-client-config-test
  (testing "contains all required keys"
    (is (contains? config/default-client-config :protocol))
    (is (contains? config/default-client-config :host))
    (is (contains? config/default-client-config :port))
    (is (contains? config/default-client-config :api-version))
    (is (contains? config/default-client-config :max-network-retries))
    (is (contains? config/default-client-config :timeout))
    (is (contains? config/default-client-config :full-response?))
    (is (contains? config/default-client-config :kebabify-keys?)))

  (testing "has sensible defaults"
    (is (= "https" (:protocol config/default-client-config)))
    (is (= "api.stripe.com" (:host config/default-client-config)))
    (is (= 443 (:port config/default-client-config)))
    (is (pos-int? (:timeout config/default-client-config)))
    (is (pos-int? (:max-network-retries config/default-client-config)))
    (is (false? (:full-response? config/default-client-config))))

  (testing "api-version matches base-api-version"
    (is (= config/base-api-version (:api-version config/default-client-config)))))

;; -----------------------------------------------------------------------------
;; base-api-version
;; -----------------------------------------------------------------------------

(deftest base-api-version-test
  (testing "is a non-empty string"
    (is (string? config/base-api-version))
    (is (pos? (count config/base-api-version)))))

;; -----------------------------------------------------------------------------
;; stripe-server-rate-limits
;; -----------------------------------------------------------------------------

(deftest stripe-server-rate-limits-test
  (testing "has live and test modes"
    (is (contains? config/stripe-server-rate-limits :live))
    (is (contains? config/stripe-server-rate-limits :test)))

  (testing "live mode has expected rate limit keys"
    (let [live (:live config/stripe-server-rate-limits)]
      (is (pos-int? (:global live)))
      (is (pos-int? (:endpoint-default live)))))

  (testing "test mode global limit is <= live mode"
    (is (<= (get-in config/stripe-server-rate-limits [:test :global])
            (get-in config/stripe-server-rate-limits [:live :global])))))

;; -----------------------------------------------------------------------------
;; mock-mode
;; -----------------------------------------------------------------------------

(deftest mock-mode-test
  (testing "uses http protocol"
    (is (= "http" (:protocol config/mock-mode))))

  (testing "has required keys"
    (is (contains? config/mock-mode :protocol))
    (is (contains? config/mock-mode :host))
    (is (contains? config/mock-mode :port))
    (is (contains? config/mock-mode :api-key)))

  (testing "port is an integer"
    (is (int? (:port config/mock-mode)))))
