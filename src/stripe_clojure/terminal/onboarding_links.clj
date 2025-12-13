(ns stripe-clojure.terminal.onboarding-links
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-terminal-onboarding-links-endpoint (config/stripe-endpoints :terminal-onboarding-links))

(defn create-onboarding-link
  "Creates a new onboarding link for Tap to Pay on iPhone.
   \nStripe API docs: https://docs.stripe.com/api/terminal/onboarding-link/create"
  ([stripe-client params]
   (create-onboarding-link stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-terminal-onboarding-links-endpoint params opts)))
