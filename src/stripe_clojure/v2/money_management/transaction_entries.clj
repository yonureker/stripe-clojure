(ns stripe-clojure.v2.money-management.transaction-entries
  "V2 Money Management Transaction Entries API.

   Transaction entries model funds moving between availability states
   for a given transaction.

   Stripe docs: https://docs.stripe.com/api/v2/money-management/transaction-entries"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-money-management-transaction-entries config/stripe-v2-endpoints))

(defn retrieve-transaction-entry
  "Retrieves a transaction entry by ID.

   Parameters:
   - stripe-client: The Stripe client instance
   - transaction-entry-id: The ID of the transaction entry
   - opts: Optional request options

   Returns:
   The transaction entry object.

   Example:
   (retrieve-transaction-entry client \"trxne_xxx\")"
  ([stripe-client transaction-entry-id]
   (retrieve-transaction-entry stripe-client transaction-entry-id {}))
  ([stripe-client transaction-entry-id opts]
   (request stripe-client :get (str endpoint "/" transaction-entry-id) {} opts)))

(defn list-transaction-entries
  "Lists all transaction entries.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: Optional query parameters
     - :created - Filter by creation time
     - :limit - The page limit
     - :transaction - Filter for entries belonging to a specific transaction
   - opts: Optional request options

   Returns:
   A list object containing transaction entries.

   Example:
   (list-transaction-entries client {:transaction \"trxn_xxx\"})"
  ([stripe-client]
   (list-transaction-entries stripe-client {}))
  ([stripe-client params]
   (list-transaction-entries stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))
