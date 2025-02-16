(ns stripe-clojure.products
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-products-endpoint (:products config/stripe-endpoints))

(defn create-product
  "Creates a new product.
   \nStripe API docs: https://stripe.com/docs/api/products/create"
  ([stripe-client params]
   (create-product stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-products-endpoint params opts)))

(defn retrieve-product
  "Retrieves the details of an existing product.
   \nStripe API docs: https://stripe.com/docs/api/products/retrieve"
  ([stripe-client product-id]
   (retrieve-product stripe-client product-id {}))
  ([stripe-client product-id opts]
   (request stripe-client :get (str stripe-products-endpoint "/" product-id) {} opts)))

(defn update-product
  "Updates the specified product.
   \nStripe API docs: https://stripe.com/docs/api/products/update"
  ([stripe-client product-id params]
   (update-product stripe-client product-id params {}))
  ([stripe-client product-id params opts]
   (request stripe-client :post (str stripe-products-endpoint "/" product-id) params opts)))

(defn list-products
  "Lists all products.
   \nStripe API docs: https://stripe.com/docs/api/products/list"
  ([stripe-client]
   (list-products stripe-client {}))
  ([stripe-client params]
   (list-products stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-products-endpoint params opts)))

(defn delete-product
  "Deletes a product.
   \nStripe API docs: https://stripe.com/docs/api/products/delete"
  ([stripe-client product-id]
   (delete-product stripe-client product-id {}))
  ([stripe-client product-id opts]
   (request stripe-client :delete (str stripe-products-endpoint "/" product-id) nil opts)))

(defn search-products
  "Searches for products.
   \nStripe API docs: https://stripe.com/docs/api/products/search"
  ([stripe-client params]
   (search-products stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get
                (str stripe-products-endpoint "/search")
                params
                opts)))

;; Features

(defn create-feature
  "Creates a new product feature.
   \nStripe API docs: https://stripe.com/docs/api/products/features/create"
  ([stripe-client product-id params]
   (create-feature stripe-client product-id params {}))
  ([stripe-client product-id params opts]
   (request stripe-client :post
                (str stripe-products-endpoint "/" product-id "/features")
                params
                opts)))

(defn retrieve-feature
  "Retrieves a product feature.
   \nStripe API docs: https://stripe.com/docs/api/products/features/retrieve"
  ([stripe-client product-id feature-id]
   (retrieve-feature stripe-client product-id feature-id {}))
  ([stripe-client product-id feature-id opts]
   (request stripe-client :get
                (str stripe-products-endpoint "/" product-id "/features/" feature-id)
                {}
                opts)))

(defn list-features
  "Lists all features for a product.
   \nStripe API docs: https://stripe.com/docs/api/products/features/list"
  ([stripe-client product-id]
   (list-features stripe-client product-id {}))
  ([stripe-client product-id params]
   (list-features stripe-client product-id params {}))
  ([stripe-client product-id params opts]
   (request stripe-client :get
                (str stripe-products-endpoint "/" product-id "/features")
                params
                opts)))

(defn delete-feature
  "Deletes a product feature.
   \nStripe API docs: https://stripe.com/docs/api/products/features/delete"
  ([stripe-client product-id feature-id]
   (delete-feature stripe-client product-id feature-id {}))
  ([stripe-client product-id feature-id opts]
   (request stripe-client :delete
                (str stripe-products-endpoint "/" product-id "/features/" feature-id)
                nil
                opts)))
