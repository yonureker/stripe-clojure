(ns stripe-clojure.climate.suppliers
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-climate-suppliers-endpoint (config/stripe-endpoints :climate-suppliers))

(defn retrieve-supplier
  "Retrieves a climate supplier.
   \nStripe API docs: https://stripe.com/docs/api/climate/supplier/retrieve"
  ([stripe-client supplier-id]
   (retrieve-supplier stripe-client supplier-id {}))
  ([stripe-client supplier-id opts]
   (request stripe-client :get
            (str stripe-climate-suppliers-endpoint "/" supplier-id)
            {}
            opts)))

(defn list-suppliers
  "Lists all climate suppliers.
   \nStripe API docs: https://stripe.com/docs/api/climate/supplier/list"
  ([stripe-client]
   (list-suppliers stripe-client {} {}))
  ([stripe-client params]
   (list-suppliers stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-climate-suppliers-endpoint params opts))) 