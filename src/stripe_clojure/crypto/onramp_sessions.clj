(ns stripe-clojure.crypto.onramp-sessions
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-crypto-onramp-sessions-endpoint (config/stripe-endpoints :crypto-onramp-sessions))

(defn create-onramp-session
  "Creates a new crypto onramp session.
   \nStripe API docs: https://stripe.com/docs/api/crypto/onramp_sessions/create"
  ([stripe-client params]
   (create-onramp-session stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-crypto-onramp-sessions-endpoint params opts)))

(defn retrieve-onramp-session
  "Retrieves a crypto onramp session.
   \nStripe API docs: https://stripe.com/docs/api/crypto/onramp_sessions/retrieve"
  ([stripe-client session-id]
   (retrieve-onramp-session stripe-client session-id {}))
  ([stripe-client session-id opts]
   (request stripe-client :get
            (str stripe-crypto-onramp-sessions-endpoint "/" session-id)
            {}
            opts)))

(defn list-onramp-sessions
  "Lists all crypto onramp sessions.
   \nStripe API docs: https://stripe.com/docs/api/crypto/onramp_sessions/list"
  ([stripe-client]
   (list-onramp-sessions stripe-client {} {}))
  ([stripe-client params]
   (list-onramp-sessions stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-crypto-onramp-sessions-endpoint params opts))) 