(ns stripe-clojure.balance-settings
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-balance-settings-endpoint (config/stripe-endpoints :balance-settings))

(defn retrieve-balance-settings
  "Retrieves balance settings for a given connected account.
   \nStripe API docs: https://docs.stripe.com/api/balance-settings/retrieve"
  ([stripe-client]
   (retrieve-balance-settings stripe-client {}))
  ([stripe-client opts]
   (request stripe-client :get stripe-balance-settings-endpoint {} opts)))

(defn update-balance-settings
  "Updates balance settings for a given connected account.
   \nStripe API docs: https://docs.stripe.com/api/balance-settings/update"
  ([stripe-client params]
   (update-balance-settings stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-balance-settings-endpoint params opts)))
