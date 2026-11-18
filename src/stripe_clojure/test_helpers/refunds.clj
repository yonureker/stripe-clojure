(ns stripe-clojure.test-helpers.refunds
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-test-helpers-refunds-endpoint (:test-helpers-refunds config/stripe-endpoints))

(defn expire-refund
  "Expires a refund with a status of requires_action.
   \nStripe API docs: https://stripe.com/docs/api/refunds"
  ([stripe-client refund-id]
   (expire-refund stripe-client refund-id {}))
  ([stripe-client refund-id opts]
   (request stripe-client :post
            (str stripe-test-helpers-refunds-endpoint "/" refund-id "/expire")
            {}
            opts)))
