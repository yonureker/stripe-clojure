(ns stripe-clojure.financial-connections.sessions
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-financial-connections-sessions-endpoint (config/stripe-endpoints :financial-connections-sessions))

(defn create-session
  "Creates a new financial connections session.
   \nStripe API docs: https://stripe.com/docs/api/financial_connections/sessions/create"
  ([stripe-client params]
   (create-session stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-financial-connections-sessions-endpoint params opts)))

(defn retrieve-session
  "Retrieves a financial connections session.
   \nStripe API docs: https://stripe.com/docs/api/financial_connections/sessions/retrieve"
  ([stripe-client session-id]
   (retrieve-session stripe-client session-id {}))
  ([stripe-client session-id opts]
   (request stripe-client :get
            (str stripe-financial-connections-sessions-endpoint "/" session-id)
            {}
            opts))) 