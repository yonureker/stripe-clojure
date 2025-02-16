(ns stripe-clojure.checkout.sessions
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-checkout-sessions-endpoint (:checkout-sessions config/stripe-endpoints))

(defn create-session
  "Creates a Session object.
   \nStripe API docs: https://stripe.com/docs/api/checkout/sessions/create"
  ([stripe-client params]
   (create-session stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-checkout-sessions-endpoint params opts)))

(defn retrieve-session
  "Retrieves a Session object.
   \nStripe API docs: https://stripe.com/docs/api/checkout/sessions/retrieve"
  ([stripe-client session-id]
   (retrieve-session stripe-client session-id {}))
  ([stripe-client session-id opts]
   (request stripe-client :get (str stripe-checkout-sessions-endpoint "/" session-id) {} opts)))

(defn update-session
  "Updates a Session object.
   \nStripe API docs: https://stripe.com/docs/api/checkout/sessions/update"
  ([stripe-client session-id params]
   (update-session stripe-client session-id params {}))
  ([stripe-client session-id params opts]
   (request stripe-client :post (str stripe-checkout-sessions-endpoint "/" session-id) params opts)))

(defn list-sessions
  "Returns a list of Checkout Sessions.
   \nStripe API docs: https://stripe.com/docs/api/checkout/sessions/list"
  ([stripe-client]
   (list-sessions stripe-client {} {}))
  ([stripe-client params]
   (list-sessions stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-checkout-sessions-endpoint params opts)))

(defn expire-session
  "Expires a Session object.
   \nStripe API docs: https://stripe.com/docs/api/checkout/sessions/expire"
  ([stripe-client session-id]
   (expire-session stripe-client session-id {}))
  ([stripe-client session-id opts]
   (request stripe-client :post (str stripe-checkout-sessions-endpoint "/" session-id "/expire") {} opts)))

(defn list-line-items
  "Lists all Line Items for a Session.
   \nStripe API docs: https://stripe.com/docs/api/checkout/sessions/line_items"
  ([stripe-client session-id]
   (list-line-items stripe-client session-id {} {}))
  ([stripe-client session-id params]
   (list-line-items stripe-client session-id params {}))
  ([stripe-client session-id params opts]
   (request stripe-client :get (str stripe-checkout-sessions-endpoint "/" session-id "/line_items")
                params
                opts)))