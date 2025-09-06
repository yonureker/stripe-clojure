(ns stripe-clojure.http.events
  (:require [stripe-clojure.http.util :as util]
            [clojure.string :as str]))

(defn build-event-data
  "Builds the common event data structure.
   Optionally transforms keys to kebab-case based on the kebabify-keys? flag."
  [base-event additional-event kebabify-keys?]
  (-> (merge base-event additional-event)
      (select-keys [:api-version :method :url :request-start-time
                    :account :idempotency-key :request-end-time
                    :elapsed :status :request-id])
      (->> (reduce-kv (fn [m k v]
                        (case k
                          :api-version        (assoc m :api_version v)
                          :method             (assoc m :method (-> v name str/upper-case))
                          :url                (assoc m :path v)
                          :request-start-time (assoc m :request_start_time v)
                          :idempotency-key    (assoc m :idempotency_key v)
                          :request-end-time   (assoc m :request_end_time v)
                          :request-id         (assoc m :request_id v)
                          (assoc m k v)))
                      {}))
      (#(if kebabify-keys? (update-keys % util/underscore-to-kebab) %))))

(def valid-event-types
  "Set of supported event types."
  #{:request :response})

(defn add-listener
   "Adds an event listener to the client's configuration.
  
  Example:
      (add-listener client :request my-handler)"
   [client event-type handler]
   (when-not (valid-event-types event-type)
     (throw (ex-info "Unsupported event type"
                     {:event-type event-type
                      :valid-types valid-event-types})))
   (swap! (-> client :config :listeners)
          conj {:type event-type :handler handler}))

 (defn remove-listener
   "Removes an event listener from the client's configuration."
   [client event-type handler]
   (when-not (valid-event-types event-type)
     (throw (ex-info "Unsupported event type"
                     {:event-type event-type
                      :valid-types valid-event-types})))
   (swap! (-> client :config :listeners)
          (fn [listeners]
            (vec (remove #(and (= (:type %) event-type)
                               (= (:handler %) handler))
                         listeners)))))

(defn emit-event
  "Emits an event to all registered listeners of the specified type.
   Optionally transforms keys to kebab-case based on the kebabify-keys? flag."
  [listeners event-type base-data additional-data kebabify-keys?]
  (when (and listeners (seq @listeners))
    (let [event-data (build-event-data base-data additional-data kebabify-keys?)]
      (doseq [{:keys [type handler]} @listeners]
        (when (= type event-type)
          (try
            (handler event-data)
            (catch Exception e
              ;; Silently ignore listener failures - don't let them break the request
              ;; Applications can add their own error handling in listeners if needed
              nil)))))))