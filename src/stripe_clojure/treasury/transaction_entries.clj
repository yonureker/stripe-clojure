(ns stripe-clojure.treasury.transaction-entries
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-treasury-transaction-entries-endpoint (config/stripe-endpoints :treasury-transaction-entries))

(defn retrieve-transaction-entry
  "Retrieves a treasury transaction entry.
   \nStripe API docs: https://stripe.com/docs/api/treasury/transaction_entries/retrieve"
  ([stripe-client transaction-entry-id]
   (retrieve-transaction-entry stripe-client transaction-entry-id {}))
  ([stripe-client transaction-entry-id opts]
   (request stripe-client :get
                (str stripe-treasury-transaction-entries-endpoint "/" transaction-entry-id)
                {}
                opts)))

(defn list-transaction-entries
  "Lists all treasury transaction entries.
   \nStripe API docs: https://stripe.com/docs/api/treasury/transaction_entries/list"
  ([stripe-client]
   (list-transaction-entries stripe-client {} {}))
  ([stripe-client params]
   (list-transaction-entries stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-treasury-transaction-entries-endpoint params opts))) 