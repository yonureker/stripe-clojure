(ns stripe-clojure.unit.http.client-test
  (:require [clojure.test :refer [deftest is testing]]
            [stripe-clojure.http.client :as client]
            [malli.core :as m]
            [stripe-clojure.schemas.client :as schema]))

;; Tests for ProxyConfig schema

(deftest proxy-config-schema-test
  (testing "validates correct proxy config with required fields"
    (is (m/validate schema/ProxyConfig {:host "proxy.example.com" :port 8080})))

  (testing "validates proxy config with all fields"
    (is (m/validate schema/ProxyConfig {:host "proxy.example.com"
                                         :port 8080
                                         :user "proxyuser"
                                         :password "proxypass"})))

  (testing "rejects proxy config missing host"
    (is (not (m/validate schema/ProxyConfig {:port 8080}))))

  (testing "rejects proxy config missing port"
    (is (not (m/validate schema/ProxyConfig {:host "proxy.example.com"}))))

  (testing "rejects proxy config with invalid port type"
    (is (not (m/validate schema/ProxyConfig {:host "proxy.example.com" :port "8080"}))))

  (testing "rejects proxy config with zero port"
    (is (not (m/validate schema/ProxyConfig {:host "proxy.example.com" :port 0}))))

  (testing "rejects proxy config with negative port"
    (is (not (m/validate schema/ProxyConfig {:host "proxy.example.com" :port -1})))))

;; Tests for create-http-client

(deftest create-http-client-test
  (testing "creates client without proxy when not specified"
    (let [client (#'client/create-http-client {:timeout 5000})]
      (is (some? client))))

  (testing "creates client with timeout from options"
    (let [client (#'client/create-http-client {:timeout 10000})]
      (is (some? client))))

  (testing "creates client with proxy when specified"
    (let [client (#'client/create-http-client {:timeout 5000
                                                :proxy {:host "proxy.example.com"
                                                        :port 8080}})]
      (is (some? client)))))

;; Tests for mask-client-config

(deftest mask-client-config-test
  (testing "masks API key in config"
    (let [config {:api-key "sk_test_1234567890abcdefghij"
                  :api-version "2025-12-15"
                  :host "api.stripe.com"}
          masked (client/mask-client-config config)]
      (is (not= "sk_test_1234567890abcdefghij" (:api-key masked)))
      (is (string? (:api-key masked)))
      (is (= "2025-12-15" (:api-version masked)))))

  (testing "removes http-client and throttler from config"
    (let [config {:api-key "sk_test_123"
                  :http-client :mock-client
                  :throttler :mock-throttler
                  :host "api.stripe.com"}
          masked (client/mask-client-config config)]
      (is (not (contains? masked :http-client)))
      (is (not (contains? masked :throttler))))))

;; Tests for merge-client-config

(deftest merge-client-config-test
  (testing "merges request opts into base config"
    (let [base {:api-key "sk_test_123"
                :api-version "2025-12-15"
                :timeout 80000}
          opts {:timeout 30000
                :stripe-account "acct_123"}
          merged (client/merge-client-config base opts)]
      (is (= 30000 (:timeout merged)))
      (is (= "acct_123" (:stripe-account merged)))
      (is (= "sk_test_123" (:api-key merged)))))

  (testing "only merges allowed keys"
    (let [base {:api-key "sk_test_123"}
          opts {:some-random-key "value"
                :timeout 30000}
          merged (client/merge-client-config base opts)]
      (is (not (contains? merged :some-random-key)))
      (is (= 30000 (:timeout merged)))))

  (testing "merges kebabify-keys? from request opts"
    (let [base {:api-key "sk_test_123" :kebabify-keys? false}
          opts {:kebabify-keys? true}
          merged (client/merge-client-config base opts)]
      (is (true? (:kebabify-keys? merged)))))

  (testing "merges full-response? from request opts"
    (let [base {:api-key "sk_test_123" :full-response? false}
          opts {:full-response? true}
          merged (client/merge-client-config base opts)]
      (is (true? (:full-response? merged))))))

;; Tests for create-instance

(deftest create-instance-test
  (testing "creates instance with valid config"
    (let [instance (client/create-instance {:api-key "sk_test_valid"})]
      (is (some? instance))
      (is (satisfies? client/StripeClient instance))))

  (testing "throws on missing API key"
    (is (thrown? clojure.lang.ExceptionInfo
                 (client/create-instance {}))))

  (testing "creates instance with proxy config"
    (let [instance (client/create-instance {:api-key "sk_test_valid"
                                            :proxy {:host "proxy.example.com"
                                                    :port 8080}})]
      (is (some? instance)))))

;; Tests for StripeClient protocol

(deftest stripe-client-protocol-test
  (testing "get-client-config returns masked config"
    (let [instance (client/create-instance {:api-key "sk_test_1234567890"})
          config (client/get-client-config instance)]
      (is (map? config))
      (is (not= "sk_test_1234567890" (:api-key config)))))

  (testing "shutdown returns nil"
    (let [instance (client/create-instance {:api-key "sk_test_valid"})
          result (client/shutdown instance)]
      (is (nil? result)))))
