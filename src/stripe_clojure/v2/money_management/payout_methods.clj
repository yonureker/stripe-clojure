(ns stripe-clojure.v2.money-management.payout-methods
  "V2 Money Management Payout Methods API.

   Payout methods are the destinations, such as bank accounts, that
   outbound payments and transfers can be sent to.

   Stripe docs: https://docs.stripe.com/api/v2/money-management/payout-methods"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-money-management-payout-methods config/stripe-v2-endpoints))

(defn retrieve-payout-method
  "Retrieves a payout method by ID.

   Parameters:
   - stripe-client: The Stripe client instance
   - payout-method-id: The ID of the payout method
   - opts: Optional request options

   Returns:
   The payout method object.

   Example:
   (retrieve-payout-method client \"usba_xxx\")"
  ([stripe-client payout-method-id]
   (retrieve-payout-method stripe-client payout-method-id {}))
  ([stripe-client payout-method-id opts]
   (request stripe-client :get (str endpoint "/" payout-method-id) {} opts)))

(defn list-payout-methods
  "Lists all payout methods.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: Optional query parameters
     - :limit - The page size
     - :usage_status - Usage status filter
   - opts: Optional request options

   Returns:
   A list object containing payout methods.

   Example:
   (list-payout-methods client {:limit 10})"
  ([stripe-client]
   (list-payout-methods stripe-client {}))
  ([stripe-client params]
   (list-payout-methods stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))

(defn archive-payout-method
  "Archives a payout method. Archiving prevents the payout method from
   being used for outbound payments or transfers and omits it from
   normal list results.

   Parameters:
   - stripe-client: The Stripe client instance
   - payout-method-id: The ID of the payout method to archive
   - opts: Optional request options

   Returns:
   The archived payout method object.

   Example:
   (archive-payout-method client \"usba_xxx\")"
  ([stripe-client payout-method-id]
   (archive-payout-method stripe-client payout-method-id {}))
  ([stripe-client payout-method-id opts]
   (request stripe-client :post (str endpoint "/" payout-method-id "/archive") {} opts)))

(defn unarchive-payout-method
  "Unarchives a payout method, restoring it to normal list results.

   Parameters:
   - stripe-client: The Stripe client instance
   - payout-method-id: The ID of the payout method to unarchive
   - opts: Optional request options

   Returns:
   The unarchived payout method object.

   Example:
   (unarchive-payout-method client \"usba_xxx\")"
  ([stripe-client payout-method-id]
   (unarchive-payout-method stripe-client payout-method-id {}))
  ([stripe-client payout-method-id opts]
   (request stripe-client :post (str endpoint "/" payout-method-id "/unarchive") {} opts)))
