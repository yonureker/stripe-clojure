(ns stripe-clojure.shipping-rates
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-shipping-rates-endpoint (:shipping-rates config/stripe-endpoints))

(defn create-shipping-rate
  "Creates a new shipping rate object.
   \nStripe API docs: https://stripe.com/docs/api/shipping_rates/create"
  ([stripe-client params]
   (create-shipping-rate stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-shipping-rates-endpoint params opts)))

(defn retrieve-shipping-rate
  "Returns the shipping rate object with the given ID.
   \nStripe API docs: https://stripe.com/docs/api/shipping_rates/retrieve"
  ([stripe-client shipping-rate-id]
   (retrieve-shipping-rate stripe-client shipping-rate-id {}))
  ([stripe-client shipping-rate-id opts]
   (request stripe-client :get (str stripe-shipping-rates-endpoint "/" shipping-rate-id) {} opts)))

(defn update-shipping-rate
  "Updates an existing shipping rate object.
   \nStripe API docs: https://stripe.com/docs/api/shipping_rates/update"
  ([stripe-client shipping-rate-id params]
   (update-shipping-rate stripe-client shipping-rate-id params {}))
  ([stripe-client shipping-rate-id params opts]
   (request stripe-client :post (str stripe-shipping-rates-endpoint "/" shipping-rate-id) params opts)))

(defn list-shipping-rates
  "Returns a list of your shipping rates.
   \nStripe API docs: https://stripe.com/docs/api/shipping_rates/list"
  ([stripe-client]
   (list-shipping-rates stripe-client {}))
  ([stripe-client params]
   (list-shipping-rates stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-shipping-rates-endpoint params opts)))