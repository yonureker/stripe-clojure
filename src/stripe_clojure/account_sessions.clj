(ns stripe-clojure.account-sessions
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-account-sessions-endpoint (:account-sessions config/stripe-endpoints))

(defn create-account-session
  "Creates a AccountSession object that includes a single-use token that the platform can use on their front-end to grant client-side API access.
   \nStripe API docs: https://stripe.com/docs/api/account_sessions/create"
  ([stripe-client params]
   (create-account-session stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-account-sessions-endpoint params opts)))
