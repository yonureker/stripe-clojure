(ns stripe-clojure.http.throttle
  (:require [stripe-clojure.config :as config]
            [clojure.string :as str]))

;; A record to hold per-instance throttler state.
(defrecord Throttler [rate-limit-config limiters])

;; Create a new throttler instance with its own (local) limiters atom.
(defn create-throttler
  "Creates a per-instance throttler with the given rate limits."
  [rate-limits]
  (let [merged-limits (merge-with merge config/default-rate-limit-config (or rate-limits {}))]
    (->Throttler merged-limits
                 (atom {}))))

;; Token Bucket Implementation for each rate limiter.
(defrecord RateLimiter [bucket-size refill-rate last-refill tokens])

(defn- make-rate-limiter
  "Creates a new rate limiter with a full bucket."
  [rate]
  (->RateLimiter rate rate (System/currentTimeMillis) rate))

(defn- refill-tokens
  "Refills tokens based on the elapsed time."
  [{:keys [bucket-size refill-rate last-refill tokens] :as limiter}]
  (let [now (System/currentTimeMillis)
        elapsed (- now last-refill)
        new-tokens (min bucket-size (+ tokens (* refill-rate (/ elapsed 1000.0))))]
    (assoc limiter :tokens new-tokens :last-refill now)))

(defn- get-or-create-limiter
  "Retrieves (or creates) a rate limiter for the given mode/resource/operation on the throttler."
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
  "Tries to acquire one token atomically from the given limiter key in throttler state."
  [^Throttler throttler limiter-key]
  (loop [attempts 0]
    (when (< attempts 100)
      (let [limiters (:limiters throttler)
            current-limiter (get @limiters limiter-key)
            refilled (refill-tokens current-limiter)
            updated-limiter (update refilled :tokens dec)]
        (if (>= (:tokens refilled) 1)
          (if (compare-and-set! limiters @limiters (assoc @limiters limiter-key updated-limiter))
            true
            (recur (inc attempts)))
          false)))))

(defn- acquire!
  "Blocks until a token is acquired, using the throttler."
  [^Throttler throttler mode resource operation]
  (let [limiter-key [mode resource operation]]
    (get-or-create-limiter throttler mode resource operation)
    (loop []
      (when-not (try-acquire! throttler limiter-key)
        (Thread/sleep 50)
        (recur)))))

(defn- get-resource-type
  "Determines a resource type from the URL."
  [url]
  (let [pattern #"^(?:https?://)?[^/]+(/v1/[^?]+)"
        [_ path] (re-find pattern url)]
    (cond
      (re-find #"/v1/files" path) :files
      (re-find #"/v1/search" path) :search
      (re-find #"/v1/meter" path)  :meter
      :else :default)))

(defn with-throttling
  "Wraps the function f with throttling logic using the per-instance throttler.
   Expects the caller to pass the throttler (as part of the client instance).
   Parameters:
     - throttler: a Throttler instance (created per client)
     - method: HTTP method keyword (:get, :post, etc.)
     - url: full URL string for the API endpoint
     - api-key: the API key in use (to determine live vs. test mode)
     - f: the function to execute once throttling permits."
  [^Throttler throttler method url api-key f]
  (let [mode (if (or (nil? api-key) (str/starts-with? api-key "sk_test"))
               :test
               :live)
        resource (get-resource-type url)
        operation (if (= method :get) :read :write)]
    (acquire! throttler mode resource operation)
    (f)))

(defn set-rate-limits!
  "Updates the throttler's rate limit configuration with custom limits.
   Resets the current limiter state."
  [^Throttler throttler custom-limits]
  (reset! (:limiters throttler) {}) ; Reset limiters for the new config.
  (assoc throttler :rate-limit-config (merge-with merge config/default-rate-limit-config custom-limits)))