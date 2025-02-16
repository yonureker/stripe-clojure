(ns stripe-clojure.tax.registrations
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-tax-registrations-endpoint (config/stripe-endpoints :tax-registrations))

(defn create-registration
  "Creates a new tax registration.
   \nStripe API docs: https://stripe.com/docs/api/tax/registrations/create"
  ([stripe-client params]
   (create-registration stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-tax-registrations-endpoint params opts)))

(defn retrieve-registration
  "Retrieves a tax registration.
   \nStripe API docs: https://stripe.com/docs/api/tax/registrations/retrieve"
  ([stripe-client registration-id]
   (retrieve-registration stripe-client registration-id {}))
  ([stripe-client registration-id opts]
   (request stripe-client :get
                (str stripe-tax-registrations-endpoint "/" registration-id)
                {}
                opts)))

(defn update-registration
  "Updates a tax registration.
   \nStripe API docs: https://stripe.com/docs/api/tax/registrations/update"
  ([stripe-client registration-id params]
   (update-registration stripe-client registration-id params {}))
  ([stripe-client registration-id params opts]
   (request stripe-client :post
                (str stripe-tax-registrations-endpoint "/" registration-id)
                params
                opts)))

(defn list-registrations
  "Lists all tax registrations.
   \nStripe API docs: https://stripe.com/docs/api/tax/registrations/all"
  ([stripe-client]
   (list-registrations stripe-client {} {}))
  ([stripe-client params]
   (list-registrations stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-tax-registrations-endpoint params opts))) 