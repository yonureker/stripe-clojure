(ns stripe-clojure.sigma.saved-queries
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-sigma-saved-queries-endpoint (config/stripe-endpoints :sigma-saved-queries))

(defn update-saved-query
  "Updates a sigma saved query.
   \nStripe API docs: https://stripe.com/docs/api/sigma/saved_queries/update"
  ([stripe-client saved-query-id params]
   (update-saved-query stripe-client saved-query-id params {}))
  ([stripe-client saved-query-id params opts]
   (request stripe-client :post
                (str stripe-sigma-saved-queries-endpoint "/" saved-query-id)
                params
                opts)))
