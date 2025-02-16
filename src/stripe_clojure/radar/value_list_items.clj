(ns stripe-clojure.radar.value-list-items
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-radar-value-list-items-endpoint (config/stripe-endpoints :radar-value-list-items))

(defn create-value-list-item
  "Creates a new radar value list item.
   \nStripe API docs: https://stripe.com/docs/api/radar/value_list_items/create"
  ([stripe-client params]
   (create-value-list-item stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-radar-value-list-items-endpoint params opts)))

(defn retrieve-value-list-item
  "Retrieves a radar value list item.
   \nStripe API docs: https://stripe.com/docs/api/radar/value_list_items/retrieve"
  ([stripe-client item-id]
   (retrieve-value-list-item stripe-client item-id {}))
  ([stripe-client item-id opts]
   (request stripe-client :get
                (str stripe-radar-value-list-items-endpoint "/" item-id)
                {}
                opts)))

(defn list-value-list-items
  "Lists all radar value list items.
   \nStripe API docs: https://stripe.com/docs/api/radar/value_list_items/list"
  ([stripe-client params]
   (list-value-list-items stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-radar-value-list-items-endpoint params opts)))

(defn delete-value-list-item
  "Deletes a radar value list item.
   \nStripe API docs: https://stripe.com/docs/api/radar/value_list_items/delete"
  ([stripe-client item-id]
   (delete-value-list-item stripe-client item-id {}))
  ([stripe-client item-id opts]
   (request stripe-client :delete
                (str stripe-radar-value-list-items-endpoint "/" item-id)
                nil
                opts))) 