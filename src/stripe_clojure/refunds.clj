(ns stripe-clojure.refunds
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-refunds-endpoint (:refunds config/stripe-endpoints))

(defn create-refund
  "Creates a refund for a charge, with optional partial amount.
   \nStripe API docs: https://stripe.com/docs/api/refunds/create"
  ([stripe-client params]
   (create-refund stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-refunds-endpoint params opts)))

(defn retrieve-refund
  "Retrieves the details of an existing refund.
   \nStripe API docs: https://stripe.com/docs/api/refunds/retrieve"
  ([stripe-client refund-id]
   (retrieve-refund stripe-client refund-id {}))
  ([stripe-client refund-id opts]
   (request stripe-client :get (str stripe-refunds-endpoint "/" refund-id) {} opts)))

(defn update-refund
  "Updates the specified refund by setting the values of the parameters passed.
   \nStripe API docs: https://stripe.com/docs/api/refunds/update"
  ([stripe-client refund-id params]
   (update-refund stripe-client refund-id params {}))
  ([stripe-client refund-id params opts]
   (request stripe-client :post (str stripe-refunds-endpoint "/" refund-id) params opts)))

(defn list-refunds
  "Returns a list of all refunds you've previously created.
   \nStripe API docs: https://stripe.com/docs/api/refunds/list"
  ([stripe-client]
   (list-refunds stripe-client {}))
  ([stripe-client params]
   (list-refunds stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-refunds-endpoint params opts)))

(defn cancel-refund
  "Cancels a refund with a status of requires_action.
   \nStripe API docs: https://stripe.com/docs/api/refunds/cancel"
  ([stripe-client refund-id]
   (cancel-refund stripe-client refund-id {}))
  ([stripe-client refund-id opts]
   (request stripe-client :post (str stripe-refunds-endpoint "/" refund-id "/cancel") {} opts)))