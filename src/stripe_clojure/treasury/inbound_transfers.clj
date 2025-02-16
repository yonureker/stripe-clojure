(ns stripe-clojure.treasury.inbound-transfers
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-treasury-inbound-transfers-endpoint (config/stripe-endpoints :treasury-inbound-transfers))

(defn create-inbound-transfer
  "Creates a new treasury inbound transfer.
   \nStripe API docs: https://stripe.com/docs/api/treasury/inbound_transfers/create"
  ([stripe-client params]
   (create-inbound-transfer stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-treasury-inbound-transfers-endpoint params opts)))

(defn retrieve-inbound-transfer
  "Retrieves a treasury inbound transfer.
   \nStripe API docs: https://stripe.com/docs/api/treasury/inbound_transfers/retrieve"
  ([stripe-client inbound-transfer-id]
   (retrieve-inbound-transfer stripe-client inbound-transfer-id {}))
  ([stripe-client inbound-transfer-id opts]
   (request stripe-client :get
            (str stripe-treasury-inbound-transfers-endpoint "/" inbound-transfer-id)
            {}
            opts)))

(defn list-inbound-transfers
  "Lists all treasury inbound transfers.
   \nStripe API docs: https://stripe.com/docs/api/treasury/inbound_transfers/list"
  ([stripe-client params]
   (list-inbound-transfers stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-treasury-inbound-transfers-endpoint params opts)))

(defn cancel-inbound-transfer
  "Cancels a treasury inbound transfer.
   \nStripe API docs: https://stripe.com/docs/api/treasury/inbound_transfers/cancel"
  ([stripe-client inbound-transfer-id]
   (cancel-inbound-transfer stripe-client inbound-transfer-id {}))
  ([stripe-client inbound-transfer-id opts]
   (request stripe-client :post
            (str stripe-treasury-inbound-transfers-endpoint "/" inbound-transfer-id "/cancel")
            {}
            opts))) 