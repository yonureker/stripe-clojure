(ns stripe-clojure.http.response
  (:require [stripe-clojure.http.util :as util]
            [cheshire.core :as json]))

(defn- parse-body
  "Parses the error body if it's a string, otherwise returns it as-is."
  [body]
  (if (string? body)
    (try
      (json/parse-string body true)
      (catch Exception _
        {:error {:message "Failed to parse error response body"}}))
    body))


(defn create-error-response
  "Creates a structured error response from the original response.
   Optionally transforms keys to kebab-case based on the config."
  [response full-response? kebabify-keys?]
  (let [parsed-body (if kebabify-keys?
                      (util/transform-keys (parse-body (:body response)))
                      (parse-body (:body response)))
        error-map (get parsed-body :error parsed-body)
        simplified-error (merge error-map
                                {:status (:status response)
                                 :request-id (get-in response [:headers "request-id"])})]
    (if full-response?
      (if kebabify-keys?
        (assoc response :body parsed-body)
        (assoc response :body (parse-body (:body response))))
      (if kebabify-keys?
        (util/transform-keys simplified-error)
        simplified-error))))

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