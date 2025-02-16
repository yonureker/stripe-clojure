(ns stripe-clojure.billing.credit-grants
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-billing-credit-grants-endpoint (config/stripe-endpoints :billing-credit-grants))

(defn create-credit-grant
  "Creates a new credit grant.
   \nStripe API docs: https://stripe.com/docs/api/billing/credit-grant/create"
  ([stripe-client params]
   (create-credit-grant stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-billing-credit-grants-endpoint params opts)))

(defn retrieve-credit-grant
  "Retrieves a credit grant.
   \nStripe API docs: https://stripe.com/docs/api/billing/credit-grant/retrieve"
  ([stripe-client grant-id]
   (retrieve-credit-grant stripe-client grant-id {}))
  ([stripe-client grant-id opts]
   (request stripe-client :get
            (str stripe-billing-credit-grants-endpoint "/" grant-id)
            {}
            opts)))

(defn update-credit-grant
  "Updates a credit grant.
   \nStripe API docs: https://stripe.com/docs/api/billing/credit-grant/update"
  ([stripe-client grant-id params]
   (update-credit-grant stripe-client grant-id params {}))
  ([stripe-client grant-id params opts]
   (request stripe-client :post
            (str stripe-billing-credit-grants-endpoint "/" grant-id)
            params
            opts)))

(defn list-credit-grants
  "Lists all credit grants.
   \nStripe API docs: https://stripe.com/docs/api/billing/credit-grant/list"
  ([stripe-client]
   (list-credit-grants stripe-client {} {}))
  ([stripe-client params]
   (list-credit-grants stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-billing-credit-grants-endpoint params opts)))

(defn expire-credit-grant
  "Expires a credit grant.
   \nStripe API docs: https://stripe.com/docs/api/billing/credit-grant/expire"
  ([stripe-client grant-id]
   (expire-credit-grant stripe-client grant-id {}))
  ([stripe-client grant-id opts]
   (request stripe-client :post
            (str stripe-billing-credit-grants-endpoint "/" grant-id "/expire")
            {}
            opts)))

(defn void-credit-grant
  "Voids a credit grant.
   \nStripe API docs: https://stripe.com/docs/api/billing/credit-grant/void"
  ([stripe-client grant-id]
   (void-credit-grant stripe-client grant-id {}))
  ([stripe-client grant-id opts]
   (request stripe-client :post
            (str stripe-billing-credit-grants-endpoint "/" grant-id "/void")
            {}
            opts))) 