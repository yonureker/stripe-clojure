(ns stripe-clojure.v2.money-management.transactions
  "V2 Money Management Transactions API.

   A transaction records how a money movement flow affects your
   financial account balance. A flow can create one or more transactions.

   Docs: https://docs.stripe.com/api/v2/money-management/transactions"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-money-management-transactions config/stripe-v2-endpoints))

(defn retrieve-transaction
  "Retrieves a transaction by ID.

   Docs: https://docs.stripe.com/api/v2/money-management/transactions/retrieve"
  ([stripe-client transaction-id]
   (retrieve-transaction stripe-client transaction-id {}))
  ([stripe-client transaction-id opts]
   (request stripe-client :get (str endpoint "/" transaction-id) {} opts)))

(defn update-transaction
  "Updates a transaction. You can update its description and metadata.

   Docs: https://docs.stripe.com/api/v2/money-management/transactions/update"
  ([stripe-client transaction-id params]
   (update-transaction stripe-client transaction-id params {}))
  ([stripe-client transaction-id params opts]
   (request stripe-client :post (str endpoint "/" transaction-id) params opts)))

(defn list-transactions
  "Lists all transactions.

   Docs: https://docs.stripe.com/api/v2/money-management/transactions/list"
  ([stripe-client]
   (list-transactions stripe-client {}))
  ([stripe-client params]
   (list-transactions stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))

(defn refresh-regulatory-receipt
  "Creates a fresh hosted URL for a transaction's regulatory receipt.

   Docs: https://docs.stripe.com/api/v2/money-management/transactions/refresh-regulatory-receipt"
  ([stripe-client transaction-id]
   (refresh-regulatory-receipt stripe-client transaction-id {}))
  ([stripe-client transaction-id opts]
   (request stripe-client :post (str endpoint "/" transaction-id "/refresh_regulatory_receipt") {} opts)))
