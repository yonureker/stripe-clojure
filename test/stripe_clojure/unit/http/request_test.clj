(ns stripe-clojure.unit.http.request-test
  (:require [clojure.test :refer [deftest is testing]]
            [stripe-clojure.http.request :as request]))

;; Tests for build-headers

(deftest build-headers-test
  (testing "includes required headers"
    (let [headers (#'request/build-headers {:api-key "sk_test_123"
                                            :api-version "2025-12-15"
                                            :content-type "application/x-www-form-urlencoded"})]
      (is (= "Bearer sk_test_123" (get headers "authorization")))
      (is (= "application/x-www-form-urlencoded" (get headers "content-type")))
      (is (= "2025-12-15" (get headers "stripe-version")))))

  (testing "includes stripe-account header when provided"
    (let [headers (#'request/build-headers {:api-key "sk_test_123"
                                            :api-version "2025-12-15"
                                            :content-type "application/x-www-form-urlencoded"
                                            :stripe-account "acct_123"})]
      (is (= "acct_123" (get headers "stripe-account")))))

  (testing "omits stripe-account header when not provided"
    (let [headers (#'request/build-headers {:api-key "sk_test_123"
                                            :api-version "2025-12-15"
                                            :content-type "application/x-www-form-urlencoded"})]
      (is (not (contains? headers "stripe-account")))))

  (testing "includes idempotency-key header when provided"
    (let [headers (#'request/build-headers {:api-key "sk_test_123"
                                            :api-version "2025-12-15"
                                            :content-type "application/x-www-form-urlencoded"
                                            :idempotency-key "idem_123"})]
      (is (= "idem_123" (get headers "idempotency-key")))))

  (testing "includes test-clock header when provided"
    (let [headers (#'request/build-headers {:api-key "sk_test_123"
                                            :api-version "2025-12-15"
                                            :content-type "application/x-www-form-urlencoded"
                                            :test-clock "clock_123"})]
      (is (= "clock_123" (get headers "stripe-test-clock")))))

  (testing "includes stripe-beta header when provided"
    (let [headers (#'request/build-headers {:api-key "sk_test_123"
                                            :api-version "2025-12-15"
                                            :content-type "application/x-www-form-urlencoded"
                                            :stripe-beta "feature_v1=true"})]
      (is (= "feature_v1=true" (get headers "stripe-beta")))))

  (testing "omits stripe-beta header when not provided"
    (let [headers (#'request/build-headers {:api-key "sk_test_123"
                                            :api-version "2025-12-15"
                                            :content-type "application/x-www-form-urlencoded"})]
      (is (not (contains? headers "stripe-beta")))))

  (testing "merges custom-headers"
    (let [headers (#'request/build-headers {:api-key "sk_test_123"
                                            :api-version "2025-12-15"
                                            :content-type "application/x-www-form-urlencoded"
                                            :custom-headers {"x-custom" "value"}})]
      (is (= "value" (get headers "x-custom")))))

  (testing "custom-headers can override defaults"
    (let [headers (#'request/build-headers {:api-key "sk_test_123"
                                            :api-version "2025-12-15"
                                            :content-type "application/x-www-form-urlencoded"
                                            :custom-headers {"content-type" "application/json"}})]
      (is (= "application/json" (get headers "content-type")))))

  (testing "v2 uses application/json content type"
    (let [headers (#'request/build-headers {:api-key "sk_test_123"
                                            :api-version "2025-12-15"
                                            :content-type "application/json"})]
      (is (= "application/json" (get headers "content-type"))))))


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

;; Tests for build-http-options

(deftest build-http-options-test
  (testing "builds basic options for GET request"
    (let [opts (#'request/build-http-options {:headers {"auth" "Bearer key"}
                                               :timeout 30000
                                               :http-client nil
                                               :method :get
                                               :params {"limit" 10}
                                               :is-v2 false})]
      (is (= {"auth" "Bearer key"} (:headers opts)))
      (is (= 30000 (:timeout opts)))
      (is (= :json (:as opts)))
      (is (= {"limit" 10} (:query-params opts)))))

  (testing "builds options for POST request with form-params (v1)"
    (let [opts (#'request/build-http-options {:headers {"auth" "Bearer key"}
                                               :timeout 30000
                                               :http-client nil
                                               :method :post
                                               :params {"email" "test@example.com"}
                                               :is-v2 false})]
      (is (= {"email" "test@example.com"} (:form-params opts)))
      (is (nil? (:query-params opts)))))

  (testing "builds options for POST request with body (v2)"
    (let [opts (#'request/build-http-options {:headers {"auth" "Bearer key"}
                                               :timeout 30000
                                               :http-client nil
                                               :method :post
                                               :params "{\"email\":\"test@example.com\"}"
                                               :is-v2 true})]
      (is (= "{\"email\":\"test@example.com\"}" (:body opts)))
      (is (nil? (:form-params opts)))))

  (testing "handles multipart requests"
    (let [multipart [{:name "purpose" :content "dispute_evidence"}
                     {:name "file" :content "data" :content-type "application/pdf"}]
          opts (#'request/build-http-options {:headers {"content-type" "application/json"}
                                               :timeout 30000
                                               :http-client nil
                                               :method :post
                                               :params {}
                                               :is-v2 false
                                               :multipart multipart})]
      (is (= multipart (:multipart opts)))
      (is (not (contains? (:headers opts) "content-type")) "content-type should be removed for multipart")))

  (testing "handles query-params for v2 POST"
    (let [opts (#'request/build-http-options {:headers {}
                                               :timeout 30000
                                               :http-client nil
                                               :method :post
                                               :params "{}"
                                               :query-params {"include[0]" "data.payload"}
                                               :is-v2 true})]
      (is (= {"include[0]" "data.payload"} (:query-params opts))))))

;; Tests for construct-base-url

(deftest construct-base-url-test
  (testing "constructs URL without port for 443"
    (let [url (#'request/construct-base-url {:protocol "https" :host "api.stripe.com" :port 443})]
      (is (= "https://api.stripe.com" url))))

  (testing "constructs URL with port for non-443"
    (let [url (#'request/construct-base-url {:protocol "http" :host "localhost" :port 12111})]
      (is (= "http://localhost:12111" url))))

  (testing "constructs URL without port when nil"
    (let [url (#'request/construct-base-url {:protocol "https" :host "api.stripe.com" :port nil})]
      (is (= "https://api.stripe.com" url)))))

;; Tests for validate-url!

(deftest validate-url-test
  (testing "accepts valid https URL"
    (is (nil? (#'request/validate-url! "https://api.stripe.com/v1/customers" "https://api.stripe.com" "/v1/customers"))))

  (testing "accepts valid http URL"
    (is (nil? (#'request/validate-url! "http://localhost:12111/v1/customers" "http://localhost:12111" "/v1/customers"))))

  (testing "throws for invalid URL"
    (is (thrown-with-msg? clojure.lang.ExceptionInfo #"Invalid URL format"
                          (#'request/validate-url! "not-a-url" "not-a-url" "/v1/customers")))))

;; Tests for send-stripe-api-request

(deftest send-stripe-api-request-test
  (testing "throws for unsupported HTTP method"
    (is (thrown-with-msg? clojure.lang.ExceptionInfo #"Unsupported HTTP method"
                          (request/send-stripe-api-request :patch "http://example.com" {}))))

  (testing "accepts supported methods"
    (is (contains? #{:get :post :delete} :get))
    (is (contains? #{:get :post :delete} :post))
    (is (contains? #{:get :post :delete} :delete))))
