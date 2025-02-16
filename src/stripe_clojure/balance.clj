(ns stripe-clojure.balance
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-balance-endpoint (:balance config/stripe-endpoints))

(defn retrieve-balance
  "Retrieves the current account balance.
   \nStripe API docs: https://stripe.com/docs/api/balance/balance_retrieve"
  ([stripe-client]
   (retrieve-balance stripe-client {}))
  ([stripe-client opts]
   (request stripe-client :get stripe-balance-endpoint {} opts)))
