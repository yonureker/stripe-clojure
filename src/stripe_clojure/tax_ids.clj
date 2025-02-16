(ns stripe-clojure.tax-ids
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-tax-ids-endpoint (config/stripe-endpoints :tax-ids))

(defn create-tax-id
  "Creates a new tax ID.
   \nStripe API docs: https://stripe.com/docs/api/tax_ids/create"
  ([stripe-client params]
   (create-tax-id stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-tax-ids-endpoint params opts)))

(defn retrieve-tax-id
  "Retrieves the tax ID with the given ID.
   \nStripe API docs: https://stripe.com/docs/api/tax_ids/retrieve"
  ([stripe-client tax-id]
   (retrieve-tax-id stripe-client tax-id {}))
  ([stripe-client tax-id opts]
   (request stripe-client :get
            (str stripe-tax-ids-endpoint "/" tax-id)
            {}
            opts)))

(defn list-tax-ids
  "Returns a list of tax IDs.
   \nStripe API docs: https://stripe.com/docs/api/tax_ids/list"
  ([stripe-client]
   (list-tax-ids stripe-client {}))
  ([stripe-client params]
   (list-tax-ids stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-tax-ids-endpoint params opts)))

(defn delete-tax-id
  "Deletes an existing tax ID.
   \nStripe API docs: https://stripe.com/docs/api/tax_ids/delete"
  ([stripe-client tax-id]
   (delete-tax-id stripe-client tax-id {}))
  ([stripe-client tax-id opts]
   (request stripe-client :delete
            (str stripe-tax-ids-endpoint "/" tax-id)
            nil
            opts)))