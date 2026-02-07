(ns stripe-clojure.http.retry)

(def ^:private base-delay-ms
  "Base delay for exponential backoff in milliseconds."
  250)

(def ^:private max-delay-ms
  "Maximum delay cap for retries in milliseconds."
  20000)

(defn exponential-backoff-delay
  "Calculates exponential backoff delay with jitter (pure function).

  The delay is calculated as follows:
  - `exponential-delay`: Grows exponentially as base-delay * (2^attempt).
  - `jitter`: A random number up to half of the exponential delay, to help distribute retries
    and avoid a thundering herd situation.
  - The final delay is capped at max-delay-ms (20000 ms).

  Parameters:
  - attempt: The current retry attempt (zero-indexed).

  Returns:
  The calculated delay in milliseconds."
  [attempt]
  (let [;; Cap the exponential calculation to avoid overflow
        capped-attempt (min attempt 10)  ; 2^10 * 250 = 256000, well above max-delay
        exponential-delay (long (* base-delay-ms (Math/pow 2 capped-attempt)))
        ;; Cap before calculating jitter to avoid overflow
        capped-delay (min exponential-delay max-delay-ms)
        jitter (rand-int (int (max 1 (/ capped-delay 2))))]
    (min (+ capped-delay jitter) max-delay-ms)))

(defn parse-retry-after-header
  "Parses Retry-After header value to milliseconds.

  Stripe sends the Retry-After header as seconds (integer).

  Parameters:
  - response: The HTTP response map with :headers.

  Returns:
  The retry delay in milliseconds, or nil if header is missing or unparseable."
  [response]
  (when-let [retry-after (get-in response [:headers "retry-after"])]
    (try
      (* 1000 (Long/parseLong (str retry-after)))
      (catch NumberFormatException _ nil))))

(defn calculate-retry-delay
  "Calculates retry delay, preferring Stripe's Retry-After header if present.
  Falls back to exponential backoff with jitter.

  Parameters:
  - response: The HTTP response map (may contain Retry-After header).
  - attempt: The current retry attempt (zero-indexed).

  Returns:
  The delay in milliseconds before retrying."
  [response attempt]
  (or (parse-retry-after-header response)
      (exponential-backoff-delay attempt)))

(defn- wait-before-retry
  "Sleeps for the calculated retry delay.

  Parameters:
  - response: The HTTP response map.
  - attempt: The current retry attempt (zero-indexed)."
  [response attempt]
  (Thread/sleep (calculate-retry-delay response attempt)))

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
  "Wraps a function with retry logic that respects Stripe's Retry-After header.

  The provided function, `f`, is expected to return a map that includes a `:status` key.
  If the returned status is retryable (as determined by `retryable-status?`),
  the function will wait and retry the operation.

  Retry delay is determined by:
  1. Stripe's Retry-After header (if present) - converted from seconds to milliseconds
  2. Exponential backoff with jitter (fallback)

  The process repeats until a non-retryable status is returned or the maximum number of
  retries (`max-retries`) is reached.

  Parameters:
  - f: A function to be executed that returns a map with at least a `:status` key.
  - max-retries: The maximum number of retry attempts (non-negative integer).

  Returns:
  A new function wrapping `f`. When called, it will:
    1. Execute `f` with the provided arguments.
    2. Check if the result contains a retryable status.
    3. If retryable and the attempt count is less than `max-retries`, wait using
       Retry-After header or exponential backoff, then retry.
    4. Otherwise, return the most recent result."
  [f max-retries]
  (fn [& args]
    (loop [attempt 0]
      (let [result (apply f args)]
        (if (and (retryable-status? (:status result))
                 (< attempt max-retries))
          (do
            (wait-before-retry result attempt)
            (recur (inc attempt)))
          result)))))