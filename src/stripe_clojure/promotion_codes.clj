(ns stripe-clojure.promotion-codes
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-promotion-codes-endpoint (:promotion-codes config/stripe-endpoints))

(defn create-promotion-code
  "Creates a new promotion code.
   \nStripe API docs: https://stripe.com/docs/api/promotion_codes/create"
  ([stripe-client params]
   (create-promotion-code stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-promotion-codes-endpoint params opts)))

(defn retrieve-promotion-code
  "Retrieves the promotion code with the given ID.
   \nStripe API docs: https://stripe.com/docs/api/promotion_codes/retrieve"
  ([stripe-client promotion-code-id]
   (retrieve-promotion-code stripe-client promotion-code-id {}))
  ([stripe-client promotion-code-id opts]
   (request stripe-client :get (str stripe-promotion-codes-endpoint "/" promotion-code-id) {} opts)))

(defn update-promotion-code
  "Updates the specified promotion code by setting the values of the parameters passed.
   \nStripe API docs: https://stripe.com/docs/api/promotion_codes/update"
  ([stripe-client promotion-code-id params]
   (update-promotion-code stripe-client promotion-code-id params {}))
  ([stripe-client promotion-code-id params opts]
   (request stripe-client :post (str stripe-promotion-codes-endpoint "/" promotion-code-id) params opts)))

(defn list-promotion-codes
  "Returns a list of your promotion codes.
   \nStripe API docs: https://stripe.com/docs/api/promotion_codes/list"
  ([stripe-client]
   (list-promotion-codes stripe-client {}))
  ([stripe-client params]
   (list-promotion-codes stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-promotion-codes-endpoint params opts)))