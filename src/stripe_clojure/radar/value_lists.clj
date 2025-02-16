(ns stripe-clojure.radar.value-lists
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-radar-value-lists-endpoint (config/stripe-endpoints :radar-value-lists))

(defn create-value-list
  "Creates a new radar value list.
   \nStripe API docs: https://stripe.com/docs/api/radar/value_lists/create"
  ([stripe-client params]
   (create-value-list stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-radar-value-lists-endpoint params opts)))

(defn retrieve-value-list
  "Retrieves a radar value list.
   \nStripe API docs: https://stripe.com/docs/api/radar/value_lists/retrieve"
  ([stripe-client list-id]
   (retrieve-value-list stripe-client list-id {}))
  ([stripe-client list-id opts]
   (request stripe-client :get
                (str stripe-radar-value-lists-endpoint "/" list-id)
                {}
                opts)))

(defn update-value-list
  "Updates a radar value list.
   \nStripe API docs: https://stripe.com/docs/api/radar/value_lists/update"
  ([stripe-client list-id params]
   (update-value-list stripe-client list-id params {}))
  ([stripe-client list-id params opts]
   (request stripe-client :post
                (str stripe-radar-value-lists-endpoint "/" list-id)
                params
                opts)))

(defn list-value-lists
  "Lists all radar value lists.
   \nStripe API docs: https://stripe.com/docs/api/radar/value_lists/list"
  ([stripe-client]
   (list-value-lists stripe-client {} {}))
  ([stripe-client params]
   (list-value-lists stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-radar-value-lists-endpoint params opts)))

(defn delete-value-list
  "Deletes a radar value list.
   \nStripe API docs: https://stripe.com/docs/api/radar/value_lists/delete"
  ([stripe-client list-id]
   (delete-value-list stripe-client list-id {}))
  ([stripe-client list-id opts]
   (request stripe-client :delete
                (str stripe-radar-value-lists-endpoint "/" list-id)
                nil
                opts))) 