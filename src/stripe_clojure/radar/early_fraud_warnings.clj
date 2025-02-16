(ns stripe-clojure.radar.early-fraud-warnings
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-radar-early-fraud-warnings-endpoint (config/stripe-endpoints :radar-early-fraud-warnings))

(defn retrieve-early-fraud-warning
  "Retrieves a radar early fraud warning.
   \nStripe API docs: https://stripe.com/docs/api/radar/early_fraud_warnings/retrieve"
  ([stripe-client warning-id]
   (retrieve-early-fraud-warning stripe-client warning-id {}))
  ([stripe-client warning-id opts]
   (request stripe-client :get
                (str stripe-radar-early-fraud-warnings-endpoint "/" warning-id)
                {}
                opts)))

(defn list-early-fraud-warnings
  "Lists all radar early fraud warnings.
   \nStripe API docs: https://stripe.com/docs/api/radar/early_fraud_warnings/list"
  ([stripe-client]
   (list-early-fraud-warnings stripe-client {} {}))
  ([stripe-client params]
   (list-early-fraud-warnings stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-radar-early-fraud-warnings-endpoint params opts))) 