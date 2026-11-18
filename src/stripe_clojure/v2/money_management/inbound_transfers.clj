(ns stripe-clojure.v2.money-management.inbound-transfers
  "V2 Money Management Inbound Transfers API.

   An inbound transfer pulls funds from a bank account you own into
   one of your financial accounts.

   Docs: https://docs.stripe.com/api/v2/money-management/inbound-transfers"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-money-management-inbound-transfers config/stripe-v2-endpoints))

(defn create-inbound-transfer
  "Creates an inbound transfer to pull funds from a bank account into
   a financial account.

   Docs: https://docs.stripe.com/api/v2/money-management/inbound-transfers/create"
  ([stripe-client params]
   (create-inbound-transfer stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))

(defn retrieve-inbound-transfer
  "Retrieves an inbound transfer by ID.

   Docs: https://docs.stripe.com/api/v2/money-management/inbound-transfers/retrieve"
  ([stripe-client inbound-transfer-id]
   (retrieve-inbound-transfer stripe-client inbound-transfer-id {}))
  ([stripe-client inbound-transfer-id opts]
   (request stripe-client :get (str endpoint "/" inbound-transfer-id) {} opts)))

(defn list-inbound-transfers
  "Lists all inbound transfers.

   Docs: https://docs.stripe.com/api/v2/money-management/inbound-transfers/list"
  ([stripe-client]
   (list-inbound-transfers stripe-client {}))
  ([stripe-client params]
   (list-inbound-transfers stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))
