(ns stripe-clojure.entitlements.active-entitlements
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-entitlements-active-entitlements-endpoint (config/stripe-endpoints :entitlements-active-entitlements))

(defn retrieve-active-entitlement
  "Retrieves an active entitlement.
   \nStripe API docs: https://stripe.com/docs/api/entitlements/active-entitlement/retrieve"
  ([stripe-client id]
   (retrieve-active-entitlement stripe-client id {}))
  ([stripe-client id opts]
   (request stripe-client :get
                (str stripe-entitlements-active-entitlements-endpoint "/" id)
                {}
                opts)))

(defn list-active-entitlements
  "Lists all active entitlements.
   \nStripe API docs: https://stripe.com/docs/api/entitlements/active-entitlement/list"
  ([stripe-client params]
   (list-active-entitlements stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-entitlements-active-entitlements-endpoint params opts))) 