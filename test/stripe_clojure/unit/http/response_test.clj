(ns stripe-clojure.unit.http.response-test
  (:require [clojure.test :refer [deftest is testing]]
            [stripe-clojure.http.response :as response]))

(deftest parse-body-test
  (testing "Valid JSON parsing"
    (is (= {:message "test"}
           (#'response/parse-body "{\"message\":\"test\"}"))))

  (testing "Invalid JSON handling"
    (let [result (#'response/parse-body "invalid-json")]
      (is (contains? result :error))
      (is (= "Failed to parse error response body" (get-in result [:error :message])))
      (is (= "invalid-json" (get-in result [:error :original-body])))))

  (testing "Non-string input passthrough"
    (is (= {:already "parsed"}
           (#'response/parse-body {:already "parsed"})))))

;; Tests for process-response

(deftest process-response-test
  (testing "Success response processing"
    (let [response {:status 200 :body {:id "test_123"}}]
      (is (= {:id "test_123"}
             (response/process-response response false false)))))

  (testing "Error response processing"
    (let [error-response {:status 400 :body "{\"error\":{\"message\":\"Bad request\"}}"}]
      (let [result (response/process-response error-response false false)]
        (is (= 400 (:status result)))
        (is (= "Bad request" (:message result))))))

  (testing "Full response mode"
    (let [response {:status 200 :headers {"content-type" "application/json"} :body {:id "test_123"}}]
      (let [result (response/process-response response true false)]
        (is (= 200 (:status result)))
        (is (contains? result :headers))
        (is (= {:id "test_123"} (:body result)))))))

(deftest process-response-request-id-in-errors-test
  (testing "error responses include request-id directly in the map"
    (let [response {:status 404
                    :headers {"request-id" "req_error456"}
                    :body "{\"error\":{\"message\":\"Not found\"}}"}
          result (response/process-response response false false)]
      (is (= "Not found" (:message result)))
      (is (= "req_error456" (:request-id result)))
      (is (= 404 (:status result)))))

  (testing "full response mode includes request-id in headers"
    (let [response {:status 200
                    :headers {"request-id" "req_full789"}
                    :body {:id "test"}}
          result (response/process-response response true false)]
      (is (= "req_full789" (get-in result [:headers "request-id"]))))))

(deftest kebabify-keys-error-response-test
  (testing "error response with kebabify-keys does not double-transform"
    (let [response {:status 400
                    :headers {"request-id" "req_123"}
                    :body "{\"error\":{\"message\":\"Bad request\",\"decline_code\":\"insufficient_funds\"}}"}
          result (response/process-response response false true)]
      (is (= 400 (:status result)))
      (is (= "Bad request" (:message result)))
      (is (= "insufficient_funds" (:decline-code result))
          "decline_code should be transformed to decline-code exactly once")
      (is (= "req_123" (:request-id result))
          "request-id should be present even when headers are kebabified")))

  (testing "error response with kebabify-keys in full-response mode"
    (let [response {:status 404
                    :headers {"request-id" "req_456"}
                    :body "{\"error\":{\"message\":\"Not found\",\"request_log_url\":\"https://...\"}}"}
          result (response/process-response response true true)]
      (is (= 404 (:status result)))
      (is (map? (:body result)))
      (is (= "Not found" (get-in result [:body :error :message])))
      (is (some? (get-in result [:body :error :request-log-url]))
          "nested error keys should be kebabified exactly once")))

  (testing "success response with kebabify-keys transforms body once"
    (let [response {:status 200
                    :headers {"request-id" "req_789"}
                    :body {:payment_intent "pi_123" :customer_name "Jane"}}
          result (response/process-response response false true)]
      (is (= "pi_123" (:payment-intent result)))
      (is (= "Jane" (:customer-name result)))))

  (testing "success response without kebabify-keys preserves snake_case"
    (let [response {:status 200
                    :headers {"request-id" "req_abc"}
                    :body {:payment_intent "pi_123" :customer_name "Jane"}}
          result (response/process-response response false false)]
      (is (= "pi_123" (:payment_intent result)))
      (is (= "Jane" (:customer_name result))))))

(deftest network-error-response-test
  (testing "network error preserves diagnostic info through process-response"
    (let [response {:status 500
                    :headers {}
                    :body {:error {:message "Connection refused"
                                   :type "network_error"}}}
          result (response/process-response response false false)]
      (is (= 500 (:status result)))
      (is (= "Connection refused" (:message result)))
      (is (= "network_error" (:type result)))))

  (testing "network error with kebabify-keys preserves diagnostic info"
    (let [response {:status 500
                    :headers {}
                    :body {:error {:message "Connection refused"
                                   :type "network_error"}}}
          result (response/process-response response false true)]
      (is (= 500 (:status result)))
      (is (= "Connection refused" (:message result)))
      (is (= "network_error" (:type result)))))

  (testing "network error in full-response mode"
    (let [response {:status 500
                    :headers {}
                    :body {:error {:message "Connection refused"
                                   :type "network_error"}}}
          result (response/process-response response true false)]
      (is (= 500 (:status result)))
      (is (= "Connection refused" (get-in result [:body :error :message]))))))
