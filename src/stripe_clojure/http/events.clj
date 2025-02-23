(ns stripe-clojure.http.events
  (:require [stripe-clojure.http.util :as util]
            [clojure.string :as str]))

(defn build-event-data
  "Builds the common event data structure.
   Optionally transforms keys to kebab-case based on the kebabify-keys? flag."
  [base-event additional-event kebabify-keys?]
  (let [combined-event (merge base-event additional-event)
        core-event-map {:api_version       (:api-version combined-event)
                        :method            (str/upper-case (name (:method combined-event)))
                        :path               (:url combined-event)
                        :request_start_time (:request-start-time combined-event)}
        complete-event-map (cond-> core-event-map
                             (:account combined-event)          (assoc :account (:account combined-event))
                             (:idempotency-key combined-event) (assoc :idempotency_key (:idempotency-key combined-event))
                             (:request-end-time combined-event) (assoc :request_end_time (:request-end-time combined-event))
                             (:elapsed combined-event)        (assoc :elapsed (:elapsed combined-event))
                             (:status combined-event)         (assoc :status (:status combined-event))
                             (:request-id combined-event)     (assoc :request_id (:request-id combined-event)))]
    (if kebabify-keys?
      (util/transform-keys complete-event-map)
      complete-event-map)))

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
            (catch Exception _)))))))