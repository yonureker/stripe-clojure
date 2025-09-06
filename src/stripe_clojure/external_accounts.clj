(ns stripe-clojure.external-accounts
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-external-accounts-endpoint (:external-accounts config/stripe-endpoints))

(defn retrieve-external-account
  "Retrieve a specified external account for a given account.
   \nStripe API docs: https://stripe.com/docs/api/external_accounts/retrieve"
  ([stripe-client external-account-id]
   (retrieve-external-account stripe-client external-account-id {}))
  ([stripe-client external-account-id opts]
   (request stripe-client :get (str stripe-external-accounts-endpoint "/" external-account-id) {} opts)))