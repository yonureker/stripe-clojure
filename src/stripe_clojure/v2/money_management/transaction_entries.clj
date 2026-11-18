(ns stripe-clojure.v2.money-management.transaction-entries
  "V2 Money Management Transaction Entries API.

   Transaction entries model funds moving between availability states
   for a given transaction.

   Docs: https://docs.stripe.com/api/v2/money-management/transaction-entries"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-money-management-transaction-entries config/stripe-v2-endpoints))

(defn retrieve-transaction-entry
  "Retrieves a transaction entry by ID.

   Docs: https://docs.stripe.com/api/v2/money-management/transaction-entries/retrieve"
  ([stripe-client transaction-entry-id]
   (retrieve-transaction-entry stripe-client transaction-entry-id {}))
  ([stripe-client transaction-entry-id opts]
   (request stripe-client :get (str endpoint "/" transaction-entry-id) {} opts)))

(defn list-transaction-entries
  "Lists all transaction entries.

   Docs: https://docs.stripe.com/api/v2/money-management/transaction-entries/list"
  ([stripe-client]
   (list-transaction-entries stripe-client {}))
  ([stripe-client params]
   (list-transaction-entries stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))
