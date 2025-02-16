(ns stripe-clojure.tax.calculations
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-tax-calculations-endpoint (config/stripe-endpoints :tax-calculations))

(defn create-calculation
  "Creates a new tax calculation.
   \nStripe API docs: https://stripe.com/docs/api/tax/calculations/create"
  ([stripe-client params]
   (create-calculation stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-tax-calculations-endpoint params opts)))

(defn retrieve-calculation
  "Retrieves a tax calculation.
   \nStripe API docs: https://stripe.com/docs/api/tax/calculations/retrieve"
  ([stripe-client calculation-id]
   (retrieve-calculation stripe-client calculation-id {}))
  ([stripe-client calculation-id opts]
   (request stripe-client :get
                (str stripe-tax-calculations-endpoint "/" calculation-id)
                {}
                opts)))

(defn list-line-items
  "Lists all line items for a tax calculation.
   \nStripe API docs: https://stripe.com/docs/api/tax/calculations/line_items"
  ([stripe-client calculation-id]
   (list-line-items stripe-client calculation-id {} {}))
  ([stripe-client calculation-id params]
   (list-line-items stripe-client calculation-id params {}))
  ([stripe-client calculation-id params opts]
   (request stripe-client :get
                (str stripe-tax-calculations-endpoint "/" calculation-id "/line_items")
                params
                opts))) 