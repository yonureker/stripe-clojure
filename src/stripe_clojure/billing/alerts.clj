(ns stripe-clojure.billing.alerts
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-billing-alerts-endpoint (config/stripe-endpoints :billing-alerts))

(defn create-alert
  "Creates a new alert.
   \nStripe API docs: https://stripe.com/docs/api/billing/alert/create"
  ([stripe-client params]
   (create-alert stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-billing-alerts-endpoint params opts)))

(defn retrieve-alert
  "Retrieves an alert.
   \nStripe API docs: https://stripe.com/docs/api/billing/alert/retrieve"
  ([stripe-client alert-id]
   (retrieve-alert stripe-client alert-id {}))
  ([stripe-client alert-id opts]
   (request stripe-client :get
                (str stripe-billing-alerts-endpoint "/" alert-id)
                {}
                opts)))

(defn list-alerts
  "Lists all alerts.
   \nStripe API docs: https://stripe.com/docs/api/billing/alert/list"
  ([stripe-client]
   (list-alerts stripe-client {} {}))
  ([stripe-client params]
   (list-alerts stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-billing-alerts-endpoint params opts)))

(defn activate-alert
  "Activates an alert.
   \nStripe API docs: https://stripe.com/docs/api/billing/alert/activate"
  ([stripe-client alert-id]
   (activate-alert stripe-client alert-id {}))
  ([stripe-client alert-id opts]
   (request stripe-client :post
                (str stripe-billing-alerts-endpoint "/" alert-id "/activate")
                {}
                opts)))

(defn archive-alert
  "Archives an alert.
   \nStripe API docs: https://stripe.com/docs/api/billing/alert/archive"
  ([stripe-client alert-id]
   (archive-alert stripe-client alert-id {}))
  ([stripe-client alert-id opts]
   (request stripe-client :post
                (str stripe-billing-alerts-endpoint "/" alert-id "/archive")
                {}
                opts)))

(defn deactivate-alert
  "Deactivates an alert.
   \nStripe API docs: https://stripe.com/docs/api/billing/alert/deactivate"
  ([stripe-client alert-id]
   (deactivate-alert stripe-client alert-id {}))
  ([stripe-client alert-id opts]
   (request stripe-client :post
                (str stripe-billing-alerts-endpoint "/" alert-id "/deactivate")
                {}
                opts)))