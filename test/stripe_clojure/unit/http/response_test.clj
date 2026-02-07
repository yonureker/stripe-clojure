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
