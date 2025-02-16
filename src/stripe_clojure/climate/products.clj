(ns stripe-clojure.climate.products
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-climate-products-endpoint (config/stripe-endpoints :climate-products))

(defn retrieve-product
  "Retrieves a climate product.
   \nStripe API docs: https://stripe.com/docs/api/climate/product/retrieve"
  ([stripe-client product-id]
   (retrieve-product stripe-client product-id {}))
  ([stripe-client product-id opts]
   (request stripe-client :get
                (str stripe-climate-products-endpoint "/" product-id)
                {}
                opts)))

(defn list-products
  "Lists all climate products.
   \nStripe API docs: https://stripe.com/docs/api/climate/product/list"
  ([stripe-client]
   (list-products stripe-client {} {}))
  ([stripe-client params]
   (list-products stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-climate-products-endpoint params opts))) 