(ns stripe-clojure.tax.settings
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-tax-settings-endpoint (config/stripe-endpoints :tax-settings))

(defn retrieve-settings
  "Retrieves tax settings.
   \nStripe API docs: https://stripe.com/docs/api/tax/settings/retrieve"
  ([stripe-client]
   (retrieve-settings stripe-client {}))
  ([stripe-client opts]
   (request stripe-client :get stripe-tax-settings-endpoint {} opts)))

(defn update-settings
  "Updates tax settings.
   \nStripe API docs: https://stripe.com/docs/api/tax/settings/update"
  ([stripe-client params]
   (update-settings stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-tax-settings-endpoint params opts))) 