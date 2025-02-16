(ns stripe-clojure.billing-portal.sessions
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-billing-portal-sessions-endpoint (:billing-portal-sessions config/stripe-endpoints))

(defn create-session
  "Creates a session for a customer to manage their subscription and billing details.
   \nStripe API docs: https://stripe.com/docs/api/customer_portal/create"
  ([stripe-client params]
   (create-session stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-billing-portal-sessions-endpoint params opts)))