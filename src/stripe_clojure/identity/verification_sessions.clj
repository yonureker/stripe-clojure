(ns stripe-clojure.identity.verification-sessions
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-identity-verification-sessions-endpoint (config/stripe-endpoints :identity-verification-sessions))

(defn create-verification-session
  "Creates a new verification session.
   \nStripe API docs: https://stripe.com/docs/api/identity/verification_sessions/create"
  ([stripe-client params]
   (create-verification-session stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-identity-verification-sessions-endpoint params opts)))

(defn retrieve-verification-session
  "Retrieves a verification session.
   \nStripe API docs: https://stripe.com/docs/api/identity/verification_sessions/retrieve"
  ([stripe-client session-id]
   (retrieve-verification-session stripe-client session-id {}))
  ([stripe-client session-id opts]
   (request stripe-client :get
                (str stripe-identity-verification-sessions-endpoint "/" session-id)
                {}
                opts)))

(defn update-verification-session
  "Updates a verification session.
   \nStripe API docs: https://stripe.com/docs/api/identity/verification_sessions/update"
  ([stripe-client session-id params]
   (update-verification-session stripe-client session-id params {}))
  ([stripe-client session-id params opts]
   (request stripe-client :post
                (str stripe-identity-verification-sessions-endpoint "/" session-id)
                params
                opts)))

(defn list-verification-sessions
  "Lists all verification sessions.
   \nStripe API docs: https://stripe.com/docs/api/identity/verification_sessions/list"
  ([stripe-client]
   (list-verification-sessions stripe-client {} {}))
  ([stripe-client params]
   (list-verification-sessions stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-identity-verification-sessions-endpoint params opts)))

(defn cancel-verification-session
  "Cancels a verification session.
   \nStripe API docs: https://stripe.com/docs/api/identity/verification_sessions/cancel"
  ([stripe-client session-id]
   (cancel-verification-session stripe-client session-id {}))
  ([stripe-client session-id opts]
   (request stripe-client :post
                (str stripe-identity-verification-sessions-endpoint "/" session-id "/cancel")
                {}
                opts)))

(defn redact-verification-session
  "Redacts a verification session.
   \nStripe API docs: https://stripe.com/docs/api/identity/verification_sessions/redact"
  ([stripe-client session-id]
   (redact-verification-session stripe-client session-id {}))
  ([stripe-client session-id opts]
   (request stripe-client :post
                (str stripe-identity-verification-sessions-endpoint "/" session-id "/redact")
                {}
                opts))) 