(ns stripe-clojure.http.throttle
  (:require [stripe-clojure.config :as config]
            [clojure.string :as str]))

;; -----------------------------------------------------------
;; Throttler and RateLimiter Records
;; -----------------------------------------------------------

(defrecord Throttler [rate-limit-config limiters])
(defrecord RateLimiter [bucket-size refill-rate last-refill tokens])

;; -----------------------------------------------------------
;; Throttler Initialization
;; -----------------------------------------------------------

(defn create-throttler
  "Creates a per-instance throttler with the given rate limits.
  
  Merges the provided `rate-limits` with the default configuration.
  
  Parameters:
  - rate-limits: A map of custom rate limits
  
  Returns:
  A Throttler record with a merged rate-limit configuration and an atom for limiter state."
  [rate-limits]
  (let [merged-limits (merge-with merge config/default-rate-limit-config (or rate-limits {}))]
    (->Throttler merged-limits (atom {}))))

(defn- make-rate-limiter
  "Creates a new rate limiter with a full token bucket.
  
  Parameters:
  - rate: The rate limit value (tokens per second).
  
  Returns:
  A RateLimiter record with the bucket initialized to be full."
  [rate]
  (->RateLimiter rate rate (System/currentTimeMillis) rate))

;; -----------------------------------------------------------
;; Token Bucket Utilities
;; -----------------------------------------------------------

(defn- refill-tokens
  "Refills tokens in a rate limiter based on elapsed time.
  
  The tokens are increased based on the elapsed time since `last-refill` multiplied by the refill rate,
  but never exceed the bucket size.
  
  Parameters:
  - limiter: A RateLimiter record.
  
  Returns:
  A new RateLimiter record with updated :tokens and :last-refill."
  [{:keys [bucket-size refill-rate last-refill tokens] :as limiter}]
  (let [now (System/currentTimeMillis)
        elapsed (- now last-refill)
        new-tokens (min bucket-size (+ tokens (* refill-rate (/ elapsed 1000.0))))]
    (assoc limiter :tokens new-tokens :last-refill now)))

(defn- compute-wait-time
  "Computes a dynamic wait time (in milliseconds) until at least one token is available.
  
  It calculates the deficit (1 - current tokens) and uses the refill rate
  to determine how many milliseconds are needed for a token to be refilled.
  
  A minimum wait of 10 ms is enforced to avoid busy loops.
  
  Parameters:
  - limiter: A RateLimiter record.
  
  Returns:
  The number of milliseconds to wait before attempting again."
  [^RateLimiter limiter]
  (let [{:keys [tokens refill-rate]} limiter
        missing (max 0 (- 1 tokens))
        time-ms (if (zero? refill-rate)
                  1000
                  (long (max 10 (* (/ missing refill-rate) 1000))))]
    time-ms))

;; -----------------------------------------------------------
;; Internal acquisition logic
;; -----------------------------------------------------------

(defn- get-or-create-limiter
  "Retrieves (or creates) a RateLimiter for the given mode/resource/operation.
  
  The rate is fetched by trying:
    1. [mode resource operation]
    2. [mode :default operation]
    3. Default value of 1 if none is found.
    
  The new limiter is created only if one was not already stored in the throttler.
  
  Parameters:
  - throttler: A Throttler record.
  - mode, resource, operation: Keys that determine the rate limit.
  
  Returns:
  The RateLimiter associated with the computed key."
  [^Throttler throttler mode resource operation]
  (let [rate-limit (or (get-in (:rate-limit-config throttler) [mode resource operation])
                       (get-in (:rate-limit-config throttler) [mode :default operation])
                       1)
        limiter-key [mode resource operation]
        limiters (:limiters throttler)]
    (swap! limiters
           (fn [lims]
             (if (contains? lims limiter-key)
               lims
               (assoc lims limiter-key (make-rate-limiter rate-limit)))))
    (get @limiters limiter-key)))

(defn- try-acquire!
  "Attempts to acquire one token from the rate limiter atomically.
  
  This function refills the tokens and checks if at least one token is available.
  If a token is available, it attempts to atomically decrement the token count
  using a compare-and-set loop. If the update succeeds, it returns true; otherwise,
  it retries up to 100 times.
  
  Parameters:
  - throttler: The Throttler instance.
  - limiter-key: The key for the specific RateLimiter in the throttler's atom.
  
  Returns:
  true if a token was acquired; false otherwise."
  [^Throttler throttler limiter-key]
  (let [limiters (:limiters throttler)]
    (loop [attempts 0]
      (when (< attempts 100)
        (let [current-state @limiters
              current-limiter (get current-state limiter-key)
              refilled (refill-tokens current-limiter)]
          (if (>= (:tokens refilled) 1)
            (let [updated-limiter (update refilled :tokens dec)
                  new-state (assoc current-state limiter-key updated-limiter)]
              (if (compare-and-set! limiters current-state new-state)
                true
                (recur (inc attempts))))
            false))))))

(defn- acquire!
  "Blocks until a token is acquired from the specified rate limiter.
  
  Instead of using a fixed sleep time between attempts, this function computes 
  a dynamic wait time using `compute-wait-time` based on the current token bucket state.
  
  Parameters:
  - throttler: A Throttler instance.
  - mode, resource, operation: Identifiers used to select the appropriate limiter.
  
  Returns:
  nil (it blocks until successful acquisition)."
  [^Throttler throttler mode resource operation]
  (let [limiter-key [mode resource operation]]
    (get-or-create-limiter throttler mode resource operation)
    (loop []
      (if (try-acquire! throttler limiter-key)
        nil
        (let [current-limiter (get @(-> throttler :limiters) limiter-key)
              wait-time (compute-wait-time current-limiter)]
          (Thread/sleep wait-time)
          (recur))))))

;; -----------------------------------------------------------
;; Public Throttling API
;; -----------------------------------------------------------

(defn with-throttling
  "Wraps the provided function `f` with throttling logic using the per-instance throttler.
  
  The throttling is based on a rate-limit configuration that distinguishes between test and live modes,
  different resources (using the URL), and HTTP methods (read for GET, write for others).
  
  Parameters:
  - throttler: A Throttler instance.
  - method: HTTP method keyword (:get, :post, etc.).
  - url: The full URL for the API endpoint.
  - api-key: Used to determine whether the requests are in test or live mode.
  - f: The function to execute once a token is acquired.
  
  Returns:
  The result of invoking `f` after a token is successfully acquired."
  [^Throttler throttler method url api-key f]
  (let [mode (if (or (nil? api-key) (str/starts-with? api-key "sk_test"))
               :test
               :live)
        resource (let [pattern #"^(?:https?://)?[^/]+(/v1/[^?]+)"
                       [_ path] (re-find pattern url)]
                   (cond
                     (re-find #"/v1/files" path) :files
                     (re-find #"/v1/search" path) :search
                     (re-find #"/v1/meter" path)  :meter
                     :else :default))
        operation (if (= method :get) :read :write)]
    (acquire! throttler mode resource operation)
    (f)))

(defn set-rate-limits!
  "Updates the throttler's rate-limit configuration with custom limits.
  
  Resets the current limiter state and merges the new custom limits
  with the default configuration.
  
  Parameters:
  - throttler: The Throttler instance.
  - custom-limits: A map of custom rate limits.
  
  Returns:
  An updated throttler with new rate-limit configuration."
  [^Throttler throttler custom-limits]
  (reset! (:limiters throttler) {}) ; Reset limiters for the new config.
  (assoc throttler :rate-limit-config (merge-with merge config/default-rate-limit-config custom-limits)))