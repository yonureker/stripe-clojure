(ns stripe-clojure.webhooks
  "Provides functions for verifying and constructing Stripe webhook events.

  The `construct-event` function accepts three parameters:
    - payload: the raw webhook payload (as a string, exactly as received)
    - signature: the 'Stripe-Signature' header value from the request
    - endpoint-secret: the webhook secret used to sign the payload

  See https://docs.stripe.com/webhooks for more details.
  NOTE: Pass the raw request body exactly as received."
  (:require [clojure.string :as str]
            [cheshire.core :as json])
  (:import (javax.crypto Mac)
           (javax.crypto.spec SecretKeySpec)
           (org.apache.commons.codec.binary Hex)))

(def ^:private default-tolerance-in-seconds 300) ; 5 minutes

(defn compute-signature
  "Computes the HMAC SHA-256 signature of the given payload using the provided secret."
  [^String payload ^String secret]
  (let [key (SecretKeySpec. (.getBytes secret "UTF-8") "HmacSHA256")
        mac (doto (Mac/getInstance "HmacSHA256")
              (.init key))]
    (Hex/encodeHexString (.doFinal mac (.getBytes payload "UTF-8")))))

(defn- parse-signature
  "Parses the Stripe webhook signature header into a map.
   Expected format is \"t=<timestamp>,v1=<signature>\".
   Throws an ex-info exception if the format is invalid."
  [signature]
  (let [pairs (str/split signature #",")]
    (reduce (fn [acc pair-string]
              (let [parts (str/split pair-string #"=")]
                (if (= (count parts) 2)
                  (assoc acc (first parts) (second parts))
                  (throw (ex-info "Invalid signature format" {:type :invalid-signature-format})))))
            {}
            pairs)))

;; Constant-time string equality to mitigate timing attacks.
(defn- constant-time-equals?
  "Performs a constant-time comparison of two strings."
  [^String a ^String b]
  (if (not= (count a) (count b))
    false
    (zero? (reduce bit-or 0 (map bit-xor (.getBytes a "UTF-8") (.getBytes b "UTF-8"))))))

(defn verify-signature
  "Verifies the signature of a Stripe webhook event.

  Parameters:
    - payload: raw request body as a string.
    - signature: the Stripe-Signature header value.
    - webhook-secret: the endpoint secret used to sign the payload.
    - opts (optional): a map of options such as {:tolerance <seconds>}, default is 300 sec.

  Returns true if the signature is valid, otherwise throws an ex-info exception."
  [payload signature webhook-secret & [{:keys [tolerance] :or {tolerance default-tolerance-in-seconds}}]]
  (when (str/blank? payload)
    (throw (ex-info "Payload is missing or empty" {:type :missing-payload})))
  (when (str/blank? signature)
    (throw (ex-info "Signature header is missing or empty" {:type :missing-signature})))
  (when (str/blank? webhook-secret)
    (throw (ex-info "Webhook secret is missing or empty" {:type :missing-webhook-secret})))
  (let [signature-parts (parse-signature signature)]
    (when-not (and (get signature-parts "t") (get signature-parts "v1"))
      (throw (ex-info "Invalid signature format" {:type :invalid-signature-format})))
    (let [timestamp (try
                      (Long/parseLong (get signature-parts "t"))
                      (catch NumberFormatException _
                        (throw (ex-info "Invalid timestamp format" {:type :invalid-timestamp-format}))))
          current-timestamp (long (/ (System/currentTimeMillis) 1000))
          time-diff (Math/abs (- current-timestamp timestamp))]
      (when (> time-diff tolerance)
        (throw (ex-info "Timestamp outside tolerance window"
                        {:type :timestamp-mismatch
                         :tolerance tolerance
                         :difference time-diff
                         :webhook-timestamp timestamp
                         :current-timestamp current-timestamp})))
      (let [signed-payload (str timestamp "." payload)
            computed-signature (compute-signature signed-payload webhook-secret)]
        (if (constant-time-equals? (get signature-parts "v1") computed-signature)
          true
          (throw (ex-info "Signature mismatch" {:type :invalid-signature})))))))

(defn construct-event
  "Constructs and verifies a Stripe event from the raw webhook payload.

  This function accepts three required parameters:
    - payload: the raw webhook payload (string, exactly as received)
    - signature: the 'Stripe-Signature' header value from the request
    - endpoint-secret: the secret used to sign the webhook payload
  An optional fourth parameter (opts) may be provided for configuration (e.g., {:tolerance 300}).

  Returns a Clojure map representing the parsed Stripe event if verification succeeds.
  Throws an ex-info exception with specific messages if verification fails."
  [payload signature endpoint-secret & [opts]]
  (try
    (if (verify-signature payload signature endpoint-secret opts)
      (json/parse-string payload true)
      (throw (ex-info "Unexpected error during signature verification"
                      {:type :verification-error})))
    (catch Exception e
      (let [error-type (:type (ex-data e))]
        (cond
          (= error-type :timestamp-mismatch)
          (throw (ex-info "Timestamp outside tolerance window" (ex-data e)))
          (= error-type :invalid-signature)
          (throw (ex-info "Signature mismatch" (ex-data e)))
          :else (throw e))))))

(defn generate-test-header-string
  "Generates a test header string for mocking Stripe webhook events.

  Options (provided as a map):
    - :payload (string): JSON string of the webhook event payload.
    - :secret (string): the webhook secret.
    - :timestamp (optional, number): a specific timestamp (in seconds). Defaults to current time.
    - :signature (optional, string): a precomputed signature to use instead of computing it.

  Returns a header string formatted as \"t=<timestamp>,v1=<signature>\"."
  [{:keys [payload secret timestamp signature]}]
  (let [ts (or timestamp (quot (System/currentTimeMillis) 1000))
        signed-payload (str ts "." payload)
        sig (or signature (compute-signature signed-payload secret))]
    (str "t=" ts ",v1=" sig)))
