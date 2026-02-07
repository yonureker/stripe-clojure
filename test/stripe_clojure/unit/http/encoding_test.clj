(ns stripe-clojure.unit.http.encoding-test
  (:require [clojure.test :refer [deftest is testing]]
            [stripe-clojure.http.encoding :as encoding]
            [stripe-clojure.http.api-version :as api-version]
            [cheshire.core :as json]))

(deftest content-type-test
  (testing "v1 uses form-urlencoded"
    (is (= "application/x-www-form-urlencoded" (encoding/content-type :v1)))
    (is (= "application/x-www-form-urlencoded" (encoding/content-type api-version/V1))))

  (testing "v2 uses application/json"
    (is (= "application/json" (encoding/content-type :v2)))
    (is (= "application/json" (encoding/content-type api-version/V2))))

  (testing "default uses form-urlencoded"
    (is (= "application/x-www-form-urlencoded" (encoding/content-type :unknown)))))

(deftest encode-params-v1-test
  (testing "v1 flattens nested params with bracket notation"
    (let [result (encoding/encode-params :v1 {:customer {:name "John" :email "john@example.com"}})]
      (is (= {"customer[name]" "John" "customer[email]" "john@example.com"} result))))

  (testing "v1 converts kebab-case to snake_case"
    (let [result (encoding/encode-params :v1 {:first-name "John"})]
      (is (= {"first_name" "John"} result))))

  (testing "v1 handles arrays with indexed notation"
    (let [result (encoding/encode-params :v1 {:items [{:price "price_123"}]})]
      (is (= {"items[0][price]" "price_123"} result))))

  (testing "v1 returns nil for nil params"
    (is (nil? (encoding/encode-params :v1 nil))))

  (testing "v1 returns empty map for empty params"
    (is (= {} (encoding/encode-params :v1 {})))))

(deftest encode-params-v2-test
  (testing "v2 returns JSON string"
    (let [result (encoding/encode-params :v2 {:email "test@example.com"})]
      (is (string? result))
      (is (= {:email "test@example.com"} (json/parse-string result true)))))

  (testing "v2 converts kebab-case keys to snake_case"
    (let [result (encoding/encode-params :v2 {:first-name "John" :last-name "Doe"})
          parsed (json/parse-string result true)]
      (is (= {:first_name "John" :last_name "Doe"} parsed))))

  (testing "v2 handles nested maps"
    (let [result (encoding/encode-params :v2 {:payload {:stripe-customer-id "cus_123" :value 100}})
          parsed (json/parse-string result true)]
      (is (= {:payload {:stripe_customer_id "cus_123" :value 100}} parsed))))

  (testing "v2 returns nil for nil params"
    (is (nil? (encoding/encode-params :v2 nil))))

  (testing "v2 returns nil for empty params"
    (is (nil? (encoding/encode-params :v2 {})))))

(deftest format-expansion-v1-test
  (testing "v1 formats single expand field"
    (let [result (encoding/format-expansion :v1 "customer")]
      (is (= {"expand[0]" "customer"} result))))

  (testing "v1 formats multiple expand fields"
    (let [result (encoding/format-expansion :v1 ["customer" "invoice.subscription"])]
      (is (= {"expand[0]" "customer" "expand[1]" "invoice.subscription"} result))))

  (testing "v1 returns empty map for nil"
    (is (= {} (encoding/format-expansion :v1 nil))))

  (testing "v1 returns empty map for empty vector"
    (is (= {} (encoding/format-expansion :v1 [])))))

(deftest format-expansion-v2-test
  (testing "v2 formats single include field"
    (let [result (encoding/format-expansion :v2 "data.payload")]
      (is (= {"include[0]" "data.payload"} result))))

  (testing "v2 formats multiple include fields"
    (let [result (encoding/format-expansion :v2 ["data.payload" "data.detail"])]
      (is (= {"include[0]" "data.payload" "include[1]" "data.detail"} result))))

  (testing "v2 returns empty map for nil"
    (is (= {} (encoding/format-expansion :v2 nil))))

  (testing "v2 returns empty map for empty vector"
    (is (= {} (encoding/format-expansion :v2 [])))))

(deftest get-expansion-fields-test
  (testing "v1 prefers :expand option"
    (is (= ["customer"] (encoding/get-expansion-fields :v1 {:expand ["customer"]}))))

  (testing "v1 falls back to :include if :expand not provided"
    (is (= ["data.payload"] (encoding/get-expansion-fields :v1 {:include ["data.payload"]}))))

  (testing "v2 prefers :include option"
    (is (= ["data.payload"] (encoding/get-expansion-fields :v2 {:include ["data.payload"]}))))

  (testing "v2 falls back to :expand if :include not provided"
    (is (= ["customer"] (encoding/get-expansion-fields :v2 {:expand ["customer"]}))))

  (testing "returns nil when neither option provided"
    (is (nil? (encoding/get-expansion-fields :v1 {})))
    (is (nil? (encoding/get-expansion-fields :v2 {}))))

  (testing "v1 prefers :expand when both :expand and :include are provided"
    (is (= ["customer"] (encoding/get-expansion-fields :v1 {:expand ["customer"]
                                                             :include ["data.payload"]}))))

  (testing "v2 prefers :include when both :expand and :include are provided"
    (is (= ["data.payload"] (encoding/get-expansion-fields :v2 {:expand ["customer"]
                                                                 :include ["data.payload"]})))))

(deftest encode-params-default-test
  (testing "default dispatch uses form-urlencoded (same as v1)"
    (let [result (encoding/encode-params :unknown {:name "John"})]
      (is (= {"name" "John"} result)))))

(deftest format-expansion-default-test
  (testing "default dispatch uses expand format (same as v1)"
    (let [result (encoding/format-expansion :unknown "customer")]
      (is (= {"expand[0]" "customer"} result))))

  (testing "default dispatch returns empty map for nil"
    (is (= {} (encoding/format-expansion :unknown nil)))))

(deftest encode-params-v2-deep-nesting-test
  (testing "v2 handles deeply nested maps with arrays containing maps"
    (let [result (encoding/encode-params :v2 {:events [{:event-name "api_requests"
                                                         :payload {:stripe-customer-id "cus_123"
                                                                   :value 100}}]})
          parsed (json/parse-string result true)]
      (is (= {:events [{:event_name "api_requests"
                         :payload {:stripe_customer_id "cus_123"
                                   :value 100}}]}
             parsed)))))
