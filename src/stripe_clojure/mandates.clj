(ns stripe-clojure.mandates
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-mandates-endpoint (:mandates config/stripe-endpoints))

(defn retrieve-mandate
  "Retrieves a mandate by ID.
   \nStripe API docs: https://stripe.com/docs/api/mandates/retrieve"
  ([stripe-client mandate-id]
   (retrieve-mandate stripe-client mandate-id {}))
  ([stripe-client mandate-id opts]
   (request stripe-client :get (str stripe-mandates-endpoint "/" mandate-id) {} opts)))
