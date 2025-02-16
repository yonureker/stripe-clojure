(ns stripe-clojure.tax-rates
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-tax-rates-endpoint (:tax-rates config/stripe-endpoints))

(defn create-tax-rate
  "Creates a new tax rate.
   \nStripe API docs: https://stripe.com/docs/api/tax_rates/create"
  ([stripe-client params]
   (create-tax-rate stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-tax-rates-endpoint params opts)))

(defn retrieve-tax-rate
  "Retrieves a tax rate with the given ID.
   \nStripe API docs: https://stripe.com/docs/api/tax_rates/retrieve"
  ([stripe-client tax-rate-id]
   (retrieve-tax-rate stripe-client tax-rate-id {}))
  ([stripe-client tax-rate-id opts]
   (request stripe-client :get (str stripe-tax-rates-endpoint "/" tax-rate-id) {} opts)))

(defn update-tax-rate
  "Updates an existing tax rate.
   \nStripe API docs: https://stripe.com/docs/api/tax_rates/update"
  ([stripe-client tax-rate-id params]
   (update-tax-rate stripe-client tax-rate-id params {}))
  ([stripe-client tax-rate-id params opts]
   (request stripe-client :post (str stripe-tax-rates-endpoint "/" tax-rate-id) params opts)))

(defn list-tax-rates
  "Returns a list of your tax rates.
   \nStripe API docs: https://stripe.com/docs/api/tax_rates/list"
  ([stripe-client]
   (list-tax-rates stripe-client {}))
  ([stripe-client params]
   (list-tax-rates stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-tax-rates-endpoint params opts)))