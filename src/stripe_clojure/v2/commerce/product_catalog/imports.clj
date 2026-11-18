(ns stripe-clojure.v2.commerce.product-catalog.imports
  "V2 Commerce Product Catalog Imports API.

   A product catalog import tracks the long-running background process
   that handles uploading, processing and validating catalog data.

   Docs: https://docs.stripe.com/api/v2/commerce/product-catalog-imports"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-commerce-product-catalog-imports config/stripe-v2-endpoints))

(defn create-import
  "Creates a product catalog import.

   Docs: https://docs.stripe.com/api/v2/commerce/product-catalog-imports/create"
  ([stripe-client params]
   (create-import stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))

(defn retrieve-import
  "Retrieves a product catalog import by ID.

   Docs: https://docs.stripe.com/api/v2/commerce/product-catalog-imports/retrieve"
  ([stripe-client import-id]
   (retrieve-import stripe-client import-id {}))
  ([stripe-client import-id opts]
   (request stripe-client :get (str endpoint "/" import-id) {} opts)))

(defn list-imports
  "Lists all product catalog imports.

   Docs: https://docs.stripe.com/api/v2/commerce/product-catalog-imports/list"
  ([stripe-client]
   (list-imports stripe-client {}))
  ([stripe-client params]
   (list-imports stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))
