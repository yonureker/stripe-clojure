(ns stripe-clojure.unit.http.retry-test
  (:require [clojure.test :refer [deftest is testing]]
            [stripe-clojure.http.retry :as retry]))

;; Tests for parse-retry-after-header

(deftest parse-retry-after-header-test
  (testing "parses valid integer header"
    (is (= 5000 (retry/parse-retry-after-header {:headers {"retry-after" "5"}})))
    (is (= 10000 (retry/parse-retry-after-header {:headers {"retry-after" "10"}})))
    (is (= 1000 (retry/parse-retry-after-header {:headers {"retry-after" "1"}}))))

  (testing "returns nil for missing header"
    (is (nil? (retry/parse-retry-after-header {:headers {}})))
    (is (nil? (retry/parse-retry-after-header {})))
    (is (nil? (retry/parse-retry-after-header nil))))

  (testing "returns nil for non-numeric header"
    (is (nil? (retry/parse-retry-after-header {:headers {"retry-after" "invalid"}})))
    (is (nil? (retry/parse-retry-after-header {:headers {"retry-after" ""}})))
    (is (nil? (retry/parse-retry-after-header {:headers {"retry-after" "1.5"}}))))

  (testing "handles string coercion"
    (is (= 5000 (retry/parse-retry-after-header {:headers {"retry-after" 5}})))))

;; Tests for exponential-backoff-delay

(deftest exponential-backoff-delay-test
  (testing "returns positive delay"
    (is (pos? (retry/exponential-backoff-delay 0)))
    (is (pos? (retry/exponential-backoff-delay 1)))
    (is (pos? (retry/exponential-backoff-delay 5))))

  (testing "delay increases with attempt number"
    ;; Run multiple times to account for jitter
    (let [delays-0 (repeatedly 10 #(retry/exponential-backoff-delay 0))
          delays-2 (repeatedly 10 #(retry/exponential-backoff-delay 2))
          avg-0 (/ (reduce + delays-0) 10)
          avg-2 (/ (reduce + delays-2) 10)]
      (is (< avg-0 avg-2) "Higher attempts should have higher average delays")))

  (testing "caps at max delay (20000ms)"
    (is (<= (retry/exponential-backoff-delay 100) 20000))
    (is (<= (retry/exponential-backoff-delay 50) 20000)))

  (testing "base delay for attempt 0 is around 250ms + jitter"
    (let [delays (repeatedly 20 #(retry/exponential-backoff-delay 0))]
      (is (every? #(>= % 250) delays) "Should be at least base delay")
      (is (every? #(<= % 500) delays) "Should not exceed base + max jitter"))))

;; Tests for calculate-retry-delay

(deftest calculate-retry-delay-test
  (testing "prefers Retry-After header when present"
    (is (= 10000 (retry/calculate-retry-delay {:headers {"retry-after" "10"}} 0)))
    (is (= 5000 (retry/calculate-retry-delay {:headers {"retry-after" "5"}} 5))))

  (testing "falls back to exponential backoff when no header"
    (let [delay (retry/calculate-retry-delay {:headers {}} 0)]
      (is (>= delay 250) "Should use exponential backoff base")
      (is (<= delay 500) "Should be within expected range")))

  (testing "falls back to exponential backoff for invalid header"
    (let [delay (retry/calculate-retry-delay {:headers {"retry-after" "invalid"}} 0)]
      (is (>= delay 250))
      (is (<= delay 500)))))

;; Tests for retryable-status?

(deftest retryable-status-test
  (testing "Retryable status codes"
    (is (true? (#'retry/retryable-status? 409)) "409 Conflict should be retryable")
    (is (true? (#'retry/retryable-status? 429)) "429 Too Many Requests should be retryable")
    (is (true? (#'retry/retryable-status? 500)) "500 Server Error should be retryable")
    (is (true? (#'retry/retryable-status? 503)) "503 Service Unavailable should be retryable"))

  (testing "Non-retryable status codes"
    (is (false? (#'retry/retryable-status? 200)) "200 OK should not be retryable")
    (is (false? (#'retry/retryable-status? 400)) "400 Bad Request should not be retryable")
    (is (false? (#'retry/retryable-status? 401)) "401 Unauthorized should not be retryable")
    (is (false? (#'retry/retryable-status? 404)) "404 Not Found should not be retryable")))

;; Tests for with-retry

(deftest with-retry-success-test
  (testing "Successful request on first attempt"
    (let [call-count (atom 0)
          test-fn (fn []
                    (swap! call-count inc)
                    {:status 200 :body "success"})
          retry-fn (retry/with-retry test-fn 3)
          result (retry-fn)]
      (is (= 1 @call-count) "Should only call function once on success")
      (is (= {:status 200 :body "success"} result))))

  (testing "Non-retryable error on first attempt"
    (let [call-count (atom 0)
          test-fn (fn []
                    (swap! call-count inc)
                    {:status 400 :body "bad request"})
          retry-fn (retry/with-retry test-fn 3)
          result (retry-fn)]
      (is (= 1 @call-count) "Should only call function once for non-retryable error")
      (is (= {:status 400 :body "bad request"} result)))))

(deftest with-retry-uses-retry-after-header-test
  (testing "Respects Retry-After header for delay"
    (let [call-count (atom 0)
          call-times (atom [])
          test-fn (fn []
                    (swap! call-times conj (System/currentTimeMillis))
                    (let [count (swap! call-count inc)]
                      (if (< count 2)
                        {:status 429
                         :headers {"retry-after" "1"}  ; 1 second
                         :body "rate limited"}
                        {:status 200 :body "success"})))
          retry-fn (retry/with-retry test-fn 3)
          result (retry-fn)
          times @call-times
          delay-between-calls (- (second times) (first times))]
      (is (= 2 @call-count))
      (is (= {:status 200 :body "success"} result))
      ;; Should have waited ~1000ms (Retry-After: 1)
      (is (>= delay-between-calls 900) "Should respect Retry-After header"))))

(deftest with-retry-eventual-success-test
  (testing "Success after retryable failures"
    (let [call-count (atom 0)
          test-fn (fn []
                    (let [count (swap! call-count inc)]
                      (if (< count 3)
                        {:status 500 :body "server error"}
                        {:status 200 :body "success"})))
          retry-fn (retry/with-retry test-fn 5)
          result (retry-fn)]
      (is (= 3 @call-count) "Should call function 3 times (2 retries + 1 success)")
      (is (= {:status 200 :body "success"} result)))))

(deftest with-retry-max-retries-test
  (testing "Exhausts max retries and returns last failure"
    (let [call-count (atom 0)
          test-fn (fn []
                    (swap! call-count inc)
                    {:status 500 :body "persistent server error"})
          retry-fn (retry/with-retry test-fn 2)
          result (retry-fn)]
      (is (= 3 @call-count) "Should call function 3 times (1 initial + 2 retries)")
      (is (= {:status 500 :body "persistent server error"} result)))))

(deftest with-retry-zero-retries-test
  (testing "Zero max retries means no retries"
    (let [call-count (atom 0)
          test-fn (fn []
                    (swap! call-count inc)
                    {:status 500 :body "server error"})
          retry-fn (retry/with-retry test-fn 0)
          result (retry-fn)]
      (is (= 1 @call-count) "Should only call once with max-retries=0")
      (is (= {:status 500 :body "server error"} result)))))
