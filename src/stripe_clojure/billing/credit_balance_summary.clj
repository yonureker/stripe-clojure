(ns stripe-clojure.billing.credit-balance-summary
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-billing-credit-balance-summary-endpoint (config/stripe-endpoints :billing-credit-balance-summary))

(defn retrieve-credit-balance-summary
  "Retrieves a credit balance summary.
   \nStripe API docs: https://stripe.com/docs/api/billing/credit-balance-summary/retrieve"
  ([stripe-client params]
   (retrieve-credit-balance-summary stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-billing-credit-balance-summary-endpoint params opts))) 