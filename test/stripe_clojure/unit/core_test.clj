(ns stripe-clojure.unit.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [stripe-clojure.core :as core]
            [stripe-clojure.http.client :as client]))

(deftest init-stripe-valid-test
  (testing "Successful initialization returns a config with proper settings"
    (let [instance (core/init-stripe {:api-key "sk_test_valid"
                                      :api-version "2024-12-18.acacia"
                                      :protocol "https"
                                      :host "api.stripe.com"
                                      :port 443
                                      :max-network-retries 5
                                      :timeout 7000
                                      :stripe-account "acct_test_valid"})
          config (core/get-config instance)]
      (is (map? config))
      (is (= "2024-12-18.acacia" (:api-version config)))
      (is (= "acct_test_valid" (:stripe-account config)))
      (is (= 5 (:max-network-retries config)))
      (is (= 7000 (:timeout config)))
      ;; We expect the raw API key to be present here.
      (is (not= (:api-key config) "sk_test_valid")))))

(deftest init-stripe-missing-api-key-test
  (testing "Initialization without API key should throw an exception"
    (is (thrown? clojure.lang.ExceptionInfo (core/init-stripe {})))))

(deftest init-stripe-additional-params-test
  (testing "Initialization returns a config with additional parameters correctly set"
    (let [instance (core/init-stripe {:api-key "sk_test_valid"
                                      :stripe-account "acct_test_valid"
                                      :max-network-retries 5
                                      :timeout 7000})
          config (core/get-config instance)]
      (is (= "acct_test_valid" (:stripe-account config)))
      (is (= 5 (:max-network-retries config)))
      (is (= 7000 (:timeout config))))))

(deftest get-client-config-test
  (testing "get-config returns a sanitized config with masked API key"
    (let [instance (core/init-stripe {:api-key "sk_test_valid"
                                    :api-version "2024-12-18.acacia"
                                    :protocol "https"
                                    :host "api.stripe.com"
                                    :port 443})
          display-config (core/get-config instance)]
      (is (map? display-config))
      (is (contains? display-config :api-key))
      (is (string? (:api-key display-config)))
      ;; The API key should be masked in the display config.
      (is (not (= (:api-key display-config) "sk_test_valid"))))))

(deftest shutdown-client-test
  (testing "shutdown-stripe-client! returns nil"
    (let [client (core/init-stripe {:api-key "sk_test_valid"
                                    :api-version "2024-12-18.acacia"
                                    :protocol "https"
                                    :host "api.stripe.com"
                                    :port 443
                                    :max-network-retries 5
                                    :timeout 7000})
          result (core/shutdown-stripe-client! client)]
      (is (nil? result)))))

(deftest raw-request-test
  (testing "raw-request delegates to execute-request with correct arguments"
    (let [captured-args (atom nil)
          mock-client (reify client/StripeClient
                        (execute-request [_ method endpoint params opts]
                          (reset! captured-args {:method method
                                                 :endpoint endpoint
                                                 :params params
                                                 :opts opts})
                          {:status 200 :body {:id "test"}}))]

      (testing "2-arity (client, method, endpoint)"
        (reset! captured-args nil)
        (core/raw-request mock-client :get "/v1/test")
        (is (= :get (:method @captured-args)))
        (is (= "/v1/test" (:endpoint @captured-args)))
        (is (= {} (:params @captured-args)))
        (is (= {} (:opts @captured-args))))

      (testing "3-arity (client, method, endpoint, params)"
        (reset! captured-args nil)
        (core/raw-request mock-client :post "/v1/test" {:foo "bar"})
        (is (= :post (:method @captured-args)))
        (is (= "/v1/test" (:endpoint @captured-args)))
        (is (= {:foo "bar"} (:params @captured-args)))
        (is (= {} (:opts @captured-args))))

      (testing "4-arity (client, method, endpoint, params, opts)"
        (reset! captured-args nil)
        (core/raw-request mock-client :delete "/v1/test/123" {} {:idempotency-key "key123"})
        (is (= :delete (:method @captured-args)))
        (is (= "/v1/test/123" (:endpoint @captured-args)))
        (is (= {} (:params @captured-args)))
        (is (= {:idempotency-key "key123"} (:opts @captured-args))))))

  (testing "raw-request returns the response from execute-request"
    (let [mock-client (reify client/StripeClient
                        (execute-request [_ _ _ _ _]
                          {:id "cus_123" :object "customer"}))]
      (is (= {:id "cus_123" :object "customer"}
             (core/raw-request mock-client :get "/v1/customers/cus_123"))))))