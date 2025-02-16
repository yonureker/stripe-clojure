(ns stripe-clojure.issuing.physical-bundles
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-issuing-physical-bundles-endpoint (config/stripe-endpoints :issuing-physical-bundles))

(defn retrieve-physical-bundle
  "Retrieves an issuing physical bundle.
   \nStripe API docs: https://stripe.com/docs/api/issuing/physical_bundles/retrieve"
  ([stripe-client bundle-id]
   (retrieve-physical-bundle stripe-client bundle-id {}))
  ([stripe-client bundle-id opts]
   (request stripe-client :get
                (str stripe-issuing-physical-bundles-endpoint "/" bundle-id)
                {}
                opts)))

(defn list-physical-bundles
  "Lists all issuing physical bundles.
   \nStripe API docs: https://stripe.com/docs/api/issuing/physical_bundles/list"
  ([stripe-client]
   (list-physical-bundles stripe-client {} {}))
  ([stripe-client params]
   (list-physical-bundles stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-issuing-physical-bundles-endpoint params opts))) 