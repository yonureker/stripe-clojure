(ns stripe-clojure.v2.commerce.product-catalog.imports
  "V2 Commerce Product Catalog Imports API.

   A product catalog import tracks the long-running background process
   that handles uploading, processing and validating catalog data.

   Stripe docs: https://docs.stripe.com/api/v2/commerce/product-catalog/imports"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-commerce-product-catalog-imports config/stripe-v2-endpoints))

(defn create-import
  "Creates a product catalog import.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: The product catalog import parameters
     - :feed_type (required) - The type of catalog data to import (\"inventory\", \"pricing\", \"product\", \"promotion\")
     - :mode (required) - The strategy for handling existing catalog data (\"replace\" or \"upsert\")
     - :metadata (required) - Additional information about the import
   - opts: Optional request options

   Returns:
   The created product catalog import object.

   Example:
   (create-import client
     {:feed_type \"product\"
      :mode \"upsert\"
      :metadata {:source \"nightly-sync\"}})"
  ([stripe-client params]
   (create-import stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))

(defn retrieve-import
  "Retrieves a product catalog import by ID.

   Parameters:
   - stripe-client: The Stripe client instance
   - import-id: The ID of the product catalog import
   - opts: Optional request options

   Returns:
   The product catalog import object.

   Example:
   (retrieve-import client \"pci_xxx\")"
  ([stripe-client import-id]
   (retrieve-import stripe-client import-id {}))
  ([stripe-client import-id opts]
   (request stripe-client :get (str endpoint "/" import-id) {} opts)))

(defn list-imports
  "Lists all product catalog imports.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: Optional query parameters
     - :created - Filter for imports created at the specified timestamp
     - :created_gt - Filter for imports created after the specified timestamp
     - :created_gte - Filter for imports created on or after the specified timestamp
     - :created_lt - Filter for imports created before the specified timestamp
     - :created_lte - Filter for imports created on or before the specified timestamp
     - :feed_type - Filter by the type of feed data being imported
     - :limit - The maximum number of results per page
     - :status - Filter by import status
   - opts: Optional request options

   Returns:
   A list object containing product catalog imports.

   Example:
   (list-imports client {:feed_type \"product\"})"
  ([stripe-client]
   (list-imports stripe-client {}))
  ([stripe-client params]
   (list-imports stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))
