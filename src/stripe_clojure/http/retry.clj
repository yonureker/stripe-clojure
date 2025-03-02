(ns stripe-clojure.http.retry)

(defn- exponential-backoff-with-jitter
  "Pauses execution using an exponential backoff strategy enhanced with random jitter.

  The delay is calculated as follows:
  - `exponential-delay`: Grows exponentially as base-delay * (2^attempt). Here, base-delay is 250 ms.
  - `jitter`: A random number up to half of the exponential delay, to help distribute retries
    and avoid a thundering herd situation.
  - The `final-delay` is the sum of the exponential delay and jitter, capped by a maximum delay of 20000 ms.

  This function blocks the calling thread by sleeping for the calculated delay.

  Parameters:
  - attempt: The current retry attempt (zero-indexed).

  Returns:
  Nothing; it simply causes the current thread to sleep."
  [attempt]
  (let [base-delay 250
        max-delay 20000
        exponential-delay (* base-delay (Math/pow 2 attempt))
        ;; Compute a random jitter (up to half of the exponential delay).
        jitter (rand-int (int (/ exponential-delay 2)))
        ;; Ensure the delay does not exceed the maximum allowed.
        final-delay (min (+ exponential-delay jitter) max-delay)]
    (Thread/sleep final-delay)))

(defn- retryable-status?
  "Determines whether an HTTP response status code indicates a transient error,
  which is eligible for a retry.

  Retryable statuses include:
  - 409 Conflict: Often due to rare race conditions.
  - 429 Too Many Requests: Typically indicates rate limiting.
  - Any status code 500 or above: Generally indicates a server error.

  Parameters:
  - status: An HTTP status code.

  Returns:
  `true` if the status is considered retryable; otherwise, `false`."
  [status]
  (or (= status 409)    ; Conflict
      (= status 429)    ; Rate limit exceeded
      (>= status 500))) ; Server errors (e.g., 500, 503, etc.)

(defn with-retry
  "Wraps a function with retry logic that utilizes an exponential backoff with jitter.
  
  The provided function, `f`, is expected to return a map that includes a `:status` key.
  If the returned status is retryable (as determined by `retryable-status?`),
  the function will wait (using exponential backoff with jitter) and retry the operation.
  
  The process repeats until a non-retryable status is returned or the maximum number of
  retries (`max-retries`) is reached. On each iteration, the attempt count is incremented,
  which influences the delay calculation.

  Parameters:
  - f: A function to be executed that returns a map with at least a `:status` key.
  - max-retries: The maximum number of retry attempts (non-negative integer).

  Returns:
  A new function wrapping `f`. When called, it will:
    1. Execute `f` with the provided arguments.
    2. Check if the result contains a retryable status.
    3. If retryable and the attempt count is less than `max-retries`, wait using the
       exponential backoff with jitter and retry.
    4. Otherwise, return the most recent result."
  [f max-retries]
  (fn [& args]
    (loop [attempt 0]
      (let [result (apply f args)]
        (if (and (retryable-status? (:status result))
                 (< attempt max-retries))
          (do
            (exponential-backoff-with-jitter attempt)
            (recur (inc attempt)))
          result)))))