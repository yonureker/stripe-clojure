(ns stripe-clojure.invoice-items
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-invoice-items-endpoint (:invoice-items config/stripe-endpoints))

(defn create-invoice-item
  "Creates a new invoice item.
   \nStripe API docs: https://stripe.com/docs/api/invoiceitems/create"
  ([stripe-client params]
   (create-invoice-item stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-invoice-items-endpoint params opts)))

(defn retrieve-invoice-item
  "Retrieves an invoice item.
   \nStripe API docs: https://stripe.com/docs/api/invoiceitems/retrieve"
  ([stripe-client invoice-item-id]
   (retrieve-invoice-item stripe-client invoice-item-id {}))
  ([stripe-client invoice-item-id opts]
   (request stripe-client :get (str stripe-invoice-items-endpoint "/" invoice-item-id) {} opts)))

(defn update-invoice-item
  "Updates an invoice item.
   \nStripe API docs: https://stripe.com/docs/api/invoiceitems/update"
  ([stripe-client invoice-item-id params]
   (update-invoice-item stripe-client invoice-item-id params {}))
  ([stripe-client invoice-item-id params opts]
   (request stripe-client :post (str stripe-invoice-items-endpoint "/" invoice-item-id) params opts)))

(defn delete-invoice-item
  "Deletes an invoice item.
   \nStripe API docs: https://stripe.com/docs/api/invoiceitems/delete"
  ([stripe-client invoice-item-id]
   (delete-invoice-item stripe-client invoice-item-id {}))
  ([stripe-client invoice-item-id opts]
   (request stripe-client :delete (str stripe-invoice-items-endpoint "/" invoice-item-id) nil opts)))

(defn list-invoice-items
  "Lists all invoice items.
   \nStripe API docs: https://stripe.com/docs/api/invoiceitems/list"
  ([stripe-client]
   (list-invoice-items stripe-client {}))
  ([stripe-client params]
   (list-invoice-items stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-invoice-items-endpoint params opts)))
