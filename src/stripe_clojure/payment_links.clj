(ns stripe-clojure.payment-links
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-payment-links-endpoint (:payment-links config/stripe-endpoints))

(defn create-payment-link
  "Creates a payment link.
   \nStripe API docs: https://stripe.com/docs/api/payment_links/create"
  ([stripe-client params]
   (create-payment-link stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-payment-links-endpoint params opts)))

(defn retrieve-payment-link
  "Retrieves a payment link.
   \nStripe API docs: https://stripe.com/docs/api/payment_links/retrieve"
  ([stripe-client payment-link-id]
   (retrieve-payment-link stripe-client payment-link-id {}))
  ([stripe-client payment-link-id opts]
   (request stripe-client :get (str stripe-payment-links-endpoint "/" payment-link-id) {} opts)))

(defn update-payment-link
  "Updates a payment link.
   \nStripe API docs: https://stripe.com/docs/api/payment_links/update"
  ([stripe-client payment-link-id params]
   (update-payment-link stripe-client payment-link-id params {}))
  ([stripe-client payment-link-id params opts]
   (request stripe-client :post (str stripe-payment-links-endpoint "/" payment-link-id) params opts)))

(defn list-payment-links
  "Lists all payment links.
   \nStripe API docs: https://stripe.com/docs/api/payment_links/list"
  ([stripe-client]
   (list-payment-links stripe-client {}))
  ([stripe-client params]
   (list-payment-links stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-payment-links-endpoint params opts)))

(defn list-line-items
  "Lists all line items of a payment link.
   \nStripe API docs: https://stripe.com/docs/api/payment_links/line_items"
  ([stripe-client payment-link-id]
   (list-line-items stripe-client payment-link-id {}))
  ([stripe-client payment-link-id params]
   (list-line-items stripe-client payment-link-id params {}))
  ([stripe-client payment-link-id params opts]
   (request stripe-client :get (str stripe-payment-links-endpoint "/" payment-link-id "/line_items")
            params
            opts)))