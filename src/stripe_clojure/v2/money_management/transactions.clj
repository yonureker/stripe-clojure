(ns stripe-clojure.v2.money-management.transactions
  "V2 Money Management Transactions API.

   A transaction records how a money movement flow affects your
   financial account balance. A flow can create one or more transactions.

   Stripe docs: https://docs.stripe.com/api/v2/money-management/transactions"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-money-management-transactions config/stripe-v2-endpoints))

(defn retrieve-transaction
  "Retrieves a transaction by ID.

   Parameters:
   - stripe-client: The Stripe client instance
   - transaction-id: The ID of the transaction
   - opts: Optional request options

   Returns:
   The transaction object.

   Example:
   (retrieve-transaction client \"trxn_xxx\")"
  ([stripe-client transaction-id]
   (retrieve-transaction stripe-client transaction-id {}))
  ([stripe-client transaction-id opts]
   (request stripe-client :get (str endpoint "/" transaction-id) {} opts)))

(defn update-transaction
  "Updates a transaction. You can update its description and metadata.

   Parameters:
   - stripe-client: The Stripe client instance
   - transaction-id: The ID of the transaction to update
   - params: The parameters to update
     - :description - A description of the transaction, up to 100 characters
     - :metadata - Set of key-value pairs to attach to the transaction
   - opts: Optional request options

   Returns:
   The updated transaction object.

   Example:
   (update-transaction client \"trxn_xxx\"
     {:description \"Supplier payment\"})"
  ([stripe-client transaction-id params]
   (update-transaction stripe-client transaction-id params {}))
  ([stripe-client transaction-id params opts]
   (request stripe-client :post (str endpoint "/" transaction-id) params opts)))

(defn list-transactions
  "Lists all transactions.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: Optional query parameters
     - :created - Filter by creation time
     - :financial_account - Filter for transactions belonging to a specific financial account
     - :flow - Filter for transactions created by a specific flow object
     - :limit - The page limit
   - opts: Optional request options

   Returns:
   A list object containing transactions.

   Example:
   (list-transactions client {:financial_account \"fa_xxx\"})"
  ([stripe-client]
   (list-transactions stripe-client {}))
  ([stripe-client params]
   (list-transactions stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))

(defn refresh-regulatory-receipt
  "Creates a fresh hosted URL for a transaction's regulatory receipt.

   Parameters:
   - stripe-client: The Stripe client instance
   - transaction-id: The ID of the transaction
   - opts: Optional request options

   Returns:
   The transaction object.

   Example:
   (refresh-regulatory-receipt client \"trxn_xxx\")"
  ([stripe-client transaction-id]
   (refresh-regulatory-receipt stripe-client transaction-id {}))
  ([stripe-client transaction-id opts]
   (request stripe-client :post (str endpoint "/" transaction-id "/refresh_regulatory_receipt") {} opts)))
