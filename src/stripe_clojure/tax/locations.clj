(ns stripe-clojure.tax.locations
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-tax-locations-endpoint (config/stripe-endpoints :tax-locations))

(defn create-location
  "Creates a new tax location.
   \nStripe API docs: https://stripe.com/docs/api/tax/locations/create"
  ([stripe-client params]
   (create-location stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-tax-locations-endpoint params opts)))

(defn retrieve-location
  "Retrieves a tax location.
   \nStripe API docs: https://stripe.com/docs/api/tax/locations/retrieve"
  ([stripe-client location-id]
   (retrieve-location stripe-client location-id {}))
  ([stripe-client location-id opts]
   (request stripe-client :get
            (str stripe-tax-locations-endpoint "/" location-id)
            {}
            opts)))

(defn list-locations
  "Lists all tax locations.
   \nStripe API docs: https://stripe.com/docs/api/tax/locations/list"
  ([stripe-client]
   (list-locations stripe-client {} {}))
  ([stripe-client params]
   (list-locations stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-tax-locations-endpoint params opts)))
