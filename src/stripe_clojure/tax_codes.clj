(ns stripe-clojure.tax-codes
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-tax-codes-endpoint (:tax-codes config/stripe-endpoints))

(defn retrieve-tax-code
  "Retrieves the details of an existing tax code.
   \nStripe API docs: https://stripe.com/docs/api/tax_codes/retrieve"
  ([stripe-client tax-code-id]
   (retrieve-tax-code stripe-client tax-code-id {}))
  ([stripe-client tax-code-id opts]
   (request stripe-client :get (str stripe-tax-codes-endpoint "/" tax-code-id) {} opts)))

(defn list-tax-codes
  "Lists all tax codes.
   \nStripe API docs: https://stripe.com/docs/api/tax_codes/list"
  ([stripe-client]
   (list-tax-codes stripe-client {}))
  ([stripe-client params]
   (list-tax-codes stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-tax-codes-endpoint params opts)))