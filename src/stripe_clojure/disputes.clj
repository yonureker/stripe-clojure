(ns stripe-clojure.disputes
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-disputes-endpoint (:disputes config/stripe-endpoints))

(defn retrieve-dispute
  "Retrieves the dispute with the given ID.
   \nStripe API docs: https://stripe.com/docs/api/disputes/retrieve"
  ([stripe-client dispute-id]
   (retrieve-dispute stripe-client dispute-id {}))
  ([stripe-client dispute-id opts]
   (request stripe-client :get (str stripe-disputes-endpoint "/" dispute-id) {} opts)))

(defn update-dispute
  "Updates the specified dispute.
   \nStripe API docs: https://stripe.com/docs/api/disputes/update"
  ([stripe-client dispute-id params]
   (update-dispute stripe-client dispute-id params {}))
  ([stripe-client dispute-id params opts]
   (request stripe-client :post (str stripe-disputes-endpoint "/" dispute-id) params opts)))

(defn list-disputes
  "Returns a list of disputes.
   \nStripe API docs: https://stripe.com/docs/api/disputes/list"
  ([stripe-client]
   (list-disputes stripe-client {}))
  ([stripe-client params]
   (list-disputes stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-disputes-endpoint params opts)))

(defn close-dispute
  "Closes the dispute with the given ID.
   \nStripe API docs: https://stripe.com/docs/api/disputes/close"
  ([stripe-client dispute-id]
   (close-dispute stripe-client dispute-id {}))
  ([stripe-client dispute-id opts]
   (request stripe-client :post (str stripe-disputes-endpoint "/" dispute-id "/close") {} opts)))
