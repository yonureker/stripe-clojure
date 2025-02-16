(ns stripe-clojure.treasury.outbound-transfers
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-treasury-outbound-transfers-endpoint (config/stripe-endpoints :treasury-outbound-transfers))

(defn create-outbound-transfer
  "Creates a new treasury outbound transfer.
   \nStripe API docs: https://stripe.com/docs/api/treasury/outbound_transfers/create"
  ([stripe-client params]
   (create-outbound-transfer stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-treasury-outbound-transfers-endpoint params opts)))

(defn retrieve-outbound-transfer
  "Retrieves a treasury outbound transfer.
   \nStripe API docs: https://stripe.com/docs/api/treasury/outbound_transfers/retrieve"
  ([stripe-client outbound-transfer-id]
   (retrieve-outbound-transfer stripe-client outbound-transfer-id {}))
  ([stripe-client outbound-transfer-id opts]
   (request stripe-client :get
                (str stripe-treasury-outbound-transfers-endpoint "/" outbound-transfer-id)
                {}
                opts)))

(defn list-outbound-transfers
  "Lists all treasury outbound transfers.
   \nStripe API docs: https://stripe.com/docs/api/treasury/outbound_transfers/list"
  ([stripe-client params]
   (list-outbound-transfers stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-treasury-outbound-transfers-endpoint params opts)))

(defn cancel-outbound-transfer
  "Cancels a treasury outbound transfer.
   \nStripe API docs: https://stripe.com/docs/api/treasury/outbound_transfers/cancel"
  ([stripe-client outbound-transfer-id]
   (cancel-outbound-transfer stripe-client outbound-transfer-id {}))
  ([stripe-client outbound-transfer-id opts]
   (request stripe-client :post
                (str stripe-treasury-outbound-transfers-endpoint "/" outbound-transfer-id "/cancel")
                {}
                opts))) 