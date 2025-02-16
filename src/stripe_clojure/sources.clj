(ns stripe-clojure.sources
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-sources-endpoint (:sources config/stripe-endpoints))

(defn create-source
  "Creates a new source object.
   \nStripe API docs: https://stripe.com/docs/api/sources/create"
  ([stripe-client params]
   (create-source stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-sources-endpoint params opts)))

(defn retrieve-source
  "Retrieves an existing source object.
   \nStripe API docs: https://stripe.com/docs/api/sources/retrieve"
  ([stripe-client source-id]
   (retrieve-source stripe-client source-id {}))
  ([stripe-client source-id opts]
   (request stripe-client :get (str stripe-sources-endpoint "/" source-id) {} opts)))

(defn update-source
  "Updates the specified source by setting the values of the parameters passed.
   \nStripe API docs: https://stripe.com/docs/api/sources/update"
  ([stripe-client source-id params]
   (update-source stripe-client source-id params {}))
  ([stripe-client source-id params opts]
   (request stripe-client :post (str stripe-sources-endpoint "/" source-id) params opts)))