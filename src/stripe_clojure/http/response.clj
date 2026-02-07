(ns stripe-clojure.http.response
  (:require [stripe-clojure.http.util :as util]
            [cheshire.core :as json]))

(defn- parse-body
  "Parses the error body if it's a string, otherwise returns it as-is."
  [body]
  (if (string? body)
    (try
      (json/parse-string body true)
      (catch Exception e
        {:error {:message "Failed to parse error response body"
                 :original-body body
                 :parse-error (.getMessage e)}}))
    body))

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
   Optionally transforms keys to kebab-case based on the config."
  [response full-response? kebabify-keys?]
  (let [transformed-response
        (cond-> response
          kebabify-keys?
          (-> (update :headers #(util/transform-keys (into {} %)))
              (update :body #(-> % parse-body util/transform-keys))))]
    (if (>= (:status transformed-response) 400)
      (create-error-response transformed-response full-response? kebabify-keys?)
      (if full-response?
        transformed-response
        (:body transformed-response)))))