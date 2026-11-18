(ns stripe-clojure.http.response
  (:require [stripe-clojure.http.util :as util]
            [cheshire.core :as json]))

(defn- parse-error-json
  "Parses a JSON error body, or describes the failure when it is not JSON."
  [text]
  (try
    (json/parse-string text true)
    (catch Exception e
      {:error {:message "Failed to parse error response body"
               :original-body text
               :parse-error (.getMessage e)}})))

(defn- parse-body
  "Parses an undecoded error body: a string, or a byte array when the request
   asked for `:as :byte-array`. Anything else is returned as-is."
  [body]
  (cond
    (string? body) (parse-error-json body)
    (bytes? body) (parse-error-json (slurp body))
    :else body))

(defn create-error-response
  "Creates a structured error response from the original response.
   When kebabify-keys? is true, the response body and headers are already
   transformed by process-response before this function is called."
  [response full-response? kebabify-keys?]
  (let [parsed-body (if kebabify-keys?
                      (:body response)
                      (parse-body (:body response)))
        error-map (get parsed-body :error parsed-body)
        request-id (or (get-in response [:headers "request-id"])
                       (get-in response [:headers :request-id]))
        simplified-error (merge error-map
                                {:status (:status response)
                                 :request-id request-id})]
    (if full-response?
      (assoc response :body parsed-body)
      simplified-error)))

(defn process-response
  "Process the response based on its status and the full-response flag.
   Optionally transforms keys to kebab-case based on the config.

   The originating request and HTTP client are removed first, since the
   request carries the Authorization header."
  [response full-response? kebabify-keys?]
  (let [error? (>= (:status response) 400)
        transformed-response
        (cond-> (dissoc response :request :http-client)
          ;; Only error bodies arrive undecoded.
          error? (update :body parse-body)
          kebabify-keys?
          (-> (update :headers #(util/transform-keys (into {} %)))
              (update :body util/transform-keys)))]
    (if error?
      (create-error-response transformed-response full-response? kebabify-keys?)
      (if full-response?
        transformed-response
        (:body transformed-response)))))