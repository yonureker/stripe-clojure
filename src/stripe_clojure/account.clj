(ns stripe-clojure.account
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-account-endpoint (:account config/stripe-endpoints))

(defn retrieve-account
  "Retrieves the details of an account.
   \nStripe API docs: https://stripe.com/docs/api/account/retrieve"
  ([stripe-client]
   (retrieve-account stripe-client {}))
  ([stripe-client opts]
   (request stripe-client :get stripe-account-endpoint {} opts)))