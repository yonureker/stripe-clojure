(ns stripe-clojure.customer-sessions
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-customer-sessions-endpoint (:customer-sessions config/stripe-endpoints))

(defn create-customer-session
  "Creates a Customer Session.
   \nStripe API docs: https://stripe.com/docs/api/customer_sessions/create"
  ([stripe-client params]
   (create-customer-session stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-customer-sessions-endpoint params opts)))
