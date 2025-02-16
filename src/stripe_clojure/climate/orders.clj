(ns stripe-clojure.climate.orders
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-climate-orders-endpoint (config/stripe-endpoints :climate-orders))

(defn create-order
  "Creates a new climate order.
   \nStripe API docs: https://stripe.com/docs/api/climate/order/create"
  ([stripe-client params]
   (create-order stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-climate-orders-endpoint params opts)))

(defn retrieve-order
  "Retrieves a climate order.
   \nStripe API docs: https://stripe.com/docs/api/climate/order/retrieve"
  ([stripe-client order-id]
   (retrieve-order stripe-client order-id {}))
  ([stripe-client order-id opts]
   (request stripe-client :get
                (str stripe-climate-orders-endpoint "/" order-id)
                {}
                opts)))

(defn update-order
  "Updates a climate order.
   \nStripe API docs: https://stripe.com/docs/api/climate/order/update"
  ([stripe-client order-id params]
   (update-order stripe-client order-id params {}))
  ([stripe-client order-id params opts]
   (request stripe-client :post
                (str stripe-climate-orders-endpoint "/" order-id)
                params
                opts)))

(defn list-orders
  "Lists all climate orders.
   \nStripe API docs: https://stripe.com/docs/api/climate/order/list"
  ([stripe-client]
   (list-orders stripe-client {} {}))
  ([stripe-client params]
   (list-orders stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-climate-orders-endpoint params opts)))

(defn cancel-order
  "Cancels a climate order.
   \nStripe API docs: https://stripe.com/docs/api/climate/order/cancel"
  ([stripe-client order-id]
   (cancel-order stripe-client order-id {}))
  ([stripe-client order-id opts]
   (request stripe-client :post
                (str stripe-climate-orders-endpoint "/" order-id "/cancel")
                {}
                opts))) 