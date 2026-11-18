(ns stripe-clojure.v2.money-management.adjustments
  "V2 Money Management Adjustments API.

   An adjustment records a credit or debit that Stripe makes to your
   financial account balance, for example to correct an error.

   Stripe docs: https://docs.stripe.com/api/v2/money-management/adjustments"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-money-management-adjustments config/stripe-v2-endpoints))

(defn retrieve-adjustment
  "Retrieves an adjustment by ID.

   Parameters:
   - stripe-client: The Stripe client instance
   - adjustment-id: The ID of the adjustment
   - opts: Optional request options

   Returns:
   The adjustment object.

   Example:
   (retrieve-adjustment client \"adj_xxx\")"
  ([stripe-client adjustment-id]
   (retrieve-adjustment stripe-client adjustment-id {}))
  ([stripe-client adjustment-id opts]
   (request stripe-client :get (str endpoint "/" adjustment-id) {} opts)))

(defn list-adjustments
  "Lists all adjustments.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: Optional query parameters
     - :adjusted_flow - Filter for adjustments linked to a specific flow object
     - :created - Filter by creation time using gt, gte, lt, or lte
     - :limit - The page limit
   - opts: Optional request options

   Returns:
   A list object containing adjustments.

   Example:
   (list-adjustments client {:limit 10})"
  ([stripe-client]
   (list-adjustments stripe-client {}))
  ([stripe-client params]
   (list-adjustments stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))
