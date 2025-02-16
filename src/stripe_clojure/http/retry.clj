(ns stripe-clojure.http.retry)

(defn- exponential-backoff-with-jitter [attempt]
  (let [base-delay 250
        max-delay 20000
        exponential-delay (* base-delay (Math/pow 2 attempt))
        jitter (rand-int (int (/ exponential-delay 2)))
        final-delay (min (+ exponential-delay jitter) max-delay)]
    (Thread/sleep final-delay)))

(defn- retryable-status? [status]
  (or (= status 409)    ; Conflict - rare race conditions
      (= status 429)    ; Rate limit exceeded
      (>= status 500))) ; Server errors (includes 500, 503, etc.

(defn with-retry
  "Creates a retry wrapper for the given function.
  
  Parameters:
  - f: The function to be retried. It should return a map containing a :status key.
  - max-retries: The maximum number of retry attempts.

  Returns:
  A new function that will retry the original function up to max-retries times
  if it encounters a retryable status code (429 or >= 500).

  The returned function will:
  1. Execute the original function.
  2. If the result has a retryable status, it will wait using exponential backoff with jitter.
  3. Retry up to max-retries times.
  4. Return the body of the last attempt, whether successful or not."
  [f max-retries]
  (fn [& args]
    (loop [attempt 0]
      (let [result (apply f args)]
        (if (and (retryable-status? (:status result)) (< attempt max-retries))
          (do
            (exponential-backoff-with-jitter attempt)
            (recur (inc attempt)))
          result)))))
