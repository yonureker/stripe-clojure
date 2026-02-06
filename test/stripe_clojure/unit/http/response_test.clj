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

;; Tests for attach-response-metadata

(deftest attach-response-metadata-test
  (testing "attaches metadata to map results"
    (let [result (response/attach-response-metadata
                   {:id "cus_123"}
                   {:status 200
                    :headers {"request-id" "req_abc123"}})]
      (is (= "req_abc123" (:request-id (meta result))))
      (is (= 200 (:status (meta result))))))

  (testing "attaches stripe-account to metadata when present"
    (let [result (response/attach-response-metadata
                   {:id "cus_123"}
                   {:status 200
                    :headers {"request-id" "req_abc"
                              "stripe-account" "acct_123"}})]
      (is (= "acct_123" (:stripe-account (meta result))))))

  (testing "returns vectors with metadata"
    (let [result (response/attach-response-metadata
                   [{:id "item1"} {:id "item2"}]
                   {:status 200
                    :headers {"request-id" "req_xyz"}})]
      (is (= "req_xyz" (:request-id (meta result))))))

  (testing "returns nil unchanged (cannot attach meta)"
    (is (nil? (response/attach-response-metadata nil {:status 204}))))

  (testing "returns strings unchanged (cannot attach meta)"
    (is (= "plain-string"
           (response/attach-response-metadata "plain-string" {:status 200}))))

  (testing "handles missing headers gracefully"
    (let [result (response/attach-response-metadata
                   {:id "test"}
                   {:status 200 :headers {}})]
      (is (nil? (:request-id (meta result))))
      (is (= 200 (:status (meta result)))))))

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

(deftest process-response-metadata-test
  (testing "attaches metadata to successful response body"
    (let [response {:status 200
                    :headers {"request-id" "req_success123"}
                    :body {:id "cus_123" :object "customer"}}
          result (response/process-response response false false)]
      (is (= {:id "cus_123" :object "customer"} result))
      (is (= "req_success123" (:request-id (meta result))))
      (is (= 200 (:status (meta result))))))

  (testing "attaches metadata to error response"
    (let [response {:status 404
                    :headers {"request-id" "req_error456"}
                    :body "{\"error\":{\"message\":\"Not found\"}}"}
          result (response/process-response response false false)]
      (is (= "Not found" (:message result)))
      (is (= "req_error456" (:request-id (meta result))))
      (is (= 404 (:status (meta result))))))

  (testing "attaches metadata to full response"
    (let [response {:status 200
                    :headers {"request-id" "req_full789"}
                    :body {:id "test"}}
          result (response/process-response response true false)]
      (is (= "req_full789" (:request-id (meta result)))))))
