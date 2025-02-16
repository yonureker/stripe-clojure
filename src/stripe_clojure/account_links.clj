(ns stripe-clojure.account-links
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-account-links-endpoint (config/stripe-endpoints :account-links))

(defn create-account-link
  "Creates a single-use account link for an Express or Custom account to onboard with Stripe.
   \nStripe API docs: https://stripe.com/docs/api/account_links/create"
  ([stripe-client params]
   (create-account-link stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-account-links-endpoint params opts)))