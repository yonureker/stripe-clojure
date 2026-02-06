(ns stripe-clojure.unit.throttle-test
  (:require [clojure.test :refer [deftest is testing]]
            [stripe-clojure.http.throttle :as throttle]
            [stripe-clojure.http.client :as client]
            [stripe-clojure.customers :refer [create-customer]]
            [stripe-clojure.config :as config]))

;; ===============================================================
;; Test 1: Read operation throttling (1 request per second)
;; This test verifies that read operations (GET requests) are properly
;; rate-limited according to the configured rate limit.
;; ===============================================================
(deftest test-throttler-read-rate-limiting
  (testing "A throttler with 1 read per second should delay subsequent read requests"
    ;; Configure a throttler with a strict rate limit of 1 read per second
    (let [rate-limit-config {:live {:default {:read 1 :write 1}}}
          throttler (throttle/create-throttler rate-limit-config)
          ;; Simple function that returns the current timestamp
          dummy-fn (fn [] (System/currentTimeMillis))
          ;; Capture the starting time
          t0 (System/currentTimeMillis)
          ;; First request should be immediate since the token bucket starts full
          t1 (throttle/with-throttling throttler :get "https://api.stripe.com/v1/test" "sk_live_123" dummy-fn)
          ;; Second request should be delayed since we've used our only token
          t2 (throttle/with-throttling throttler :get "https://api.stripe.com/v1/test" "sk_live_123" dummy-fn)]
      ;; First request should complete quickly (under 200ms)
      (is (< (- t1 t0) 200) "First read call should return quickly (<200ms)")
      ;; Second request should be delayed by approximately 1 second due to rate limiting
      (is (>= (- t2 t1) 900) "Second read call should be delayed by at least ~1 second"))))

;; ===============================================================
;; Test 2: Write operation throttling (1 request per second)
;; This test verifies that write operations (POST, PUT, DELETE) are
;; properly rate-limited according to the configured rate limit.
;; ===============================================================
(deftest test-throttler-write-rate-limiting
  (testing "A throttler with 1 write per second should delay subsequent write requests"
    ;; Configure a throttler with 10 reads/sec but only 1 write/sec
    (let [rate-limit-config {:live {:default {:read 10 :write 1}}}
          throttler (throttle/create-throttler rate-limit-config)
          dummy-fn (fn [] (System/currentTimeMillis))
          t0 (System/currentTimeMillis)
          ;; First write request should be immediate
          t1 (throttle/with-throttling throttler :post "https://api.stripe.com/v1/test" "sk_live_123" dummy-fn)
          ;; Second write request should be delayed
          t2 (throttle/with-throttling throttler :post "https://api.stripe.com/v1/test" "sk_live_123" dummy-fn)]
      (is (< (- t1 t0) 200) "First write call should return quickly (<200ms)")
      (is (>= (- t2 t1) 900) "Second write call should be delayed by at least ~1 second"))))

;; ===============================================================
;; Test 3: Burst Capacity
;; This test verifies that the token bucket algorithm correctly allows
;; for burst capacity - allowing multiple requests to be processed
;; immediately if the bucket has accumulated enough tokens.
;; ===============================================================
(deftest test-throttler-burst-capacity
  (testing "A throttler configured with burst capacity should allow burst calls immediately and delay subsequent ones"
    ;; Configure a throttler with 3 tokens per second (both read and write)
    (let [rate-limit-config {:live {:default {:read 3 :write 3}}}
          throttler (throttle/create-throttler rate-limit-config)
          dummy-fn (fn [] (System/currentTimeMillis))
          t0 (System/currentTimeMillis)
          ;; First three calls should be immediate since we have 3 tokens in the bucket
          t1 (throttle/with-throttling throttler :get "https://api.stripe.com/v1/test" "sk_live_123" dummy-fn)
          t2 (throttle/with-throttling throttler :get "https://api.stripe.com/v1/test" "sk_live_123" dummy-fn)
          t3 (throttle/with-throttling throttler :get "https://api.stripe.com/v1/test" "sk_live_123" dummy-fn)
          ;; Fourth call should be delayed since we've used all tokens
          t4 (throttle/with-throttling throttler :get "https://api.stripe.com/v1/test" "sk_live_123" dummy-fn)]
      (is (< (- t1 t0) 200) "1st burst call immediate")
      (is (< (- t2 t1) 200) "2nd burst call immediate")
      (is (< (- t3 t2) 200) "3rd burst call immediate")
      (is (>= (- t4 t3) 300) "4th call should be delayed due to burst cap exhaustion"))))

;; ===============================================================
;; Test 4: Per-request Rate Limit Override
;; This test verifies that rate limits can be overridden on a per-request
;; basis, allowing for dynamic adjustment of throttling behavior.
;; ===============================================================
(deftest test-per-request-rate-limit-override
  (testing "Per-request rate limit override creates a temporary throttler with the provided limits"
    (let [;; Instance throttler with lenient limits (high capacity)
          rate-limit-config-instance {:live {:default {:read 100 :write 100}}}
          instance-throttler (throttle/create-throttler rate-limit-config-instance)
          client-config {:throttler instance-throttler}
          dummy-fn (fn [] (System/currentTimeMillis))

          ;; First, without override, should be immediate due to high capacity
          t0 (System/currentTimeMillis)
          t1 (throttle/with-throttling (:throttler client-config)
               :get "https://api.stripe.com/v1/test" "sk_live_123" dummy-fn)

          ;; Now, override rate-limits per request with a stricter 1 req/sec limit
          ;; This creates a new throttler with merged configuration
          effective-throttler (throttle/create-throttler
                               (merge-with merge (-> client-config :throttler :rate-limit-config)
                                           {:live {:default {:read 1}}}))

          ;; First call with stricter throttler should still be immediate
          t2 (throttle/with-throttling effective-throttler
               :get "https://api.stripe.com/v1/test" "sk_live_123" dummy-fn)

          ;; Second call with stricter throttler should be delayed
          t3 (throttle/with-throttling effective-throttler
               :get "https://api.stripe.com/v1/test" "sk_live_123" dummy-fn)]

      (is (< (- t1 t0) 200) "Without override, a read call is immediate")
      (is (< (- t2 t1) 200) "First call with override immediate")
      (is (>= (- t3 t2) 900) "Second call with override should be delayed by ~1 second"))))

;; ===============================================================
;; Test 5: set-rate-limits! resets throttler state
;; This test verifies that the set-rate-limits! function correctly
;; resets the throttler state and applies new rate limits.
;; ===============================================================
(deftest test-different-rate-limits
  (testing "Different throttlers with different rate limits behave correctly"
    (let [;; Start with a strict rate limit
          strict-throttler (throttle/create-throttler {:live {:default {:read 1 :write 1}}})
          ;; Create a more generous throttler
          generous-throttler (throttle/create-throttler {:live {:default {:read 10 :write 10}}})
          dummy-fn (fn [] (System/currentTimeMillis))

          ;; Consume the only token from strict throttler
          t1 (throttle/with-throttling strict-throttler :get "https://api.stripe.com/v1/test" "sk_live_123" dummy-fn)

          ;; Immediately attempt a second call with strict throttler; should be delayed
          t2 (throttle/with-throttling strict-throttler :get "https://api.stripe.com/v1/test" "sk_live_123" dummy-fn)]

      (is (>= (- t2 t1) 900) "Second call with strict throttler is delayed due to exhausted token")

      ;; Use the generous throttler; should be immediate since it has more capacity
      (let [t3 (throttle/with-throttling generous-throttler :get "https://api.stripe.com/v1/test" "sk_live_123" dummy-fn)]
        (is (< (- t3 t2) 200) "Call with generous throttler should be immediate relative to previous call")))))

;; ===============================================================
;; Test 6: Concurrent Customer Creations with Throttling
;; This integration test verifies that multiple concurrent operations
;; are properly throttled according to the configured rate limits.
;; ===============================================================
(deftest test-10-concurrent-customer-creations-throttling
  (testing "10 concurrent customer creations in test mode with a write limit of 1 per second take at least 9 seconds"
    ;; Build the client using the test API key and an instance throttler that limits
    ;; write operations to 1 per second in test mode.
    (let [opts {:api-key (get config/api-keys :test)
                :rate-limits {:test {:default {:write 1}}}}
          ;; Create a Stripe client with the configured rate limits
          stripe-client (client/create-instance opts)

          ;; For each customer, we generate unique parameters to avoid duplicates
          unique-customer-params (fn [n]
                                   {:email (str "test-customer-" n "@example.com")
                                    :description (str "Test Customer " n)})

          ;; Record the start time for measuring total duration
          start-time (System/currentTimeMillis)

          ;; Create 10 futures that concurrently call create-customer
          ;; This simulates multiple threads making API calls simultaneously
          futures (doall
                   (for [n (range 10)]
                     (future
                       (create-customer stripe-client (unique-customer-params n)))))

          ;; Wait for all customer creation calls to complete
          _ (doseq [f futures] @f)

          ;; Calculate the total duration of all operations
          end-time (System/currentTimeMillis)
          total-duration (- end-time start-time)]

      ;; Assert that the overall duration is at least ~7 seconds due to throttling
      ;; (Slightly less than 9 seconds to account for potential timing variations)
      (is (>= total-duration 7000)
          (str "Expected total duration to be at least ~7000 ms, but got " total-duration " ms")))))