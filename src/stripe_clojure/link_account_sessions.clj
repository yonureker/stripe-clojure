(ns stripe-clojure.link-account-sessions
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-link-account-sessions-endpoint (:link-account-sessions config/stripe-endpoints))

(defn create-link-account-session
  "To launch the hosted Link account onboarding process, create a Link account session.
   \nStripe API docs: https://stripe.com/docs/api/link_account_sessions/create"
  ([stripe-client params]
   (create-link-account-session stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-link-account-sessions-endpoint params opts)))

(defn retrieve-link-account-session
  "Retrieves a Link account session.
   \nStripe API docs: https://stripe.com/docs/api/link_account_sessions/retrieve"
  ([stripe-client session-id]
   (retrieve-link-account-session stripe-client session-id {}))
  ([stripe-client session-id opts]
   (request stripe-client :get (str stripe-link-account-sessions-endpoint "/" session-id) {} opts)))