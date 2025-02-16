(ns stripe-clojure.entitlements.features
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-entitlements-features-endpoint (config/stripe-endpoints :entitlements-features))

(defn create-feature
  "Creates a new feature.
   \nStripe API docs: https://stripe.com/docs/api/entitlements/feature/create"
  ([stripe-client params]
   (create-feature stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-entitlements-features-endpoint params opts)))

(defn update-feature
  "Updates a feature.
   \nStripe API docs: https://stripe.com/docs/api/entitlements/feature/update"
  ([stripe-client id params]
   (update-feature stripe-client id params {}))
  ([stripe-client id params opts]
   (request stripe-client :post
                (str stripe-entitlements-features-endpoint "/" id)
                params
                opts)))

(defn list-features
  "Lists all features.
   \nStripe API docs: https://stripe.com/docs/api/entitlements/feature/list"
  ([stripe-client]
   (list-features stripe-client {} {}))
  ([stripe-client params]
   (list-features stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-entitlements-features-endpoint params opts))) 