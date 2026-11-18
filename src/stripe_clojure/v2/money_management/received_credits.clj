(ns stripe-clojure.v2.money-management.received-credits
  "V2 Money Management Received Credits API.

   Received credits describe when, where, and how funds are sent into
   your financial account.

   Stripe docs: https://docs.stripe.com/api/v2/money-management/received-credits"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-money-management-received-credits config/stripe-v2-endpoints))

(defn retrieve-received-credit
  "Retrieves a received credit by ID.

   Parameters:
   - stripe-client: The Stripe client instance
   - received-credit-id: The ID of the received credit
   - opts: Optional request options

   Returns:
   The received credit object.

   Example:
   (retrieve-received-credit client \"rc_xxx\")"
  ([stripe-client received-credit-id]
   (retrieve-received-credit stripe-client received-credit-id {}))
  ([stripe-client received-credit-id opts]
   (request stripe-client :get (str endpoint "/" received-credit-id) {} opts)))

(defn list-received-credits
  "Lists all received credits.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: Optional query parameters
     - :created - Filter by creation time
     - :limit - The page limit
   - opts: Optional request options

   Returns:
   A list object containing received credits.

   Example:
   (list-received-credits client {:limit 10})"
  ([stripe-client]
   (list-received-credits stripe-client {}))
  ([stripe-client params]
   (list-received-credits stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))
