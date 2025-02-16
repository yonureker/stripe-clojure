(ns stripe-clojure.transfers
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-transfers-endpoint (config/stripe-endpoints :transfers))

(defn create-transfer
  "Creates a new transfer.
   \nStripe API docs: https://stripe.com/docs/api/transfers/create"
  ([stripe-client params]
   (create-transfer stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-transfers-endpoint params opts)))

(defn retrieve-transfer
  "Retrieves the details of an existing transfer.
   \nStripe API docs: https://stripe.com/docs/api/transfers/retrieve"
  ([stripe-client transfer-id]
   (retrieve-transfer stripe-client transfer-id {}))
  ([stripe-client transfer-id opts]
   (request stripe-client :get
                (str stripe-transfers-endpoint "/" transfer-id)
                {}
                opts)))

(defn update-transfer
  "Updates the specified transfer by setting the values of the parameters passed.
   \nStripe API docs: https://stripe.com/docs/api/transfers/update"
  ([stripe-client transfer-id params]
   (update-transfer stripe-client transfer-id params {}))
  ([stripe-client transfer-id params opts]
   (request stripe-client :post
                (str stripe-transfers-endpoint "/" transfer-id)
                params
                opts)))

(defn list-transfers
  "Returns a list of existing transfers.
   \nStripe API docs: https://stripe.com/docs/api/transfers/list"
  ([stripe-client]
   (list-transfers stripe-client {}))
  ([stripe-client params]
   (list-transfers stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-transfers-endpoint params opts)))

;; Reversals

(defn create-reversal
  "Creates a new reversal for a transfer.
   \nStripe API docs: https://stripe.com/docs/api/transfer_reversals/create"
  ([stripe-client transfer-id]
   (create-reversal stripe-client transfer-id {}))
  ([stripe-client transfer-id params]
   (create-reversal stripe-client transfer-id params {}))
  ([stripe-client transfer-id params opts]
   (request stripe-client :post
                (str stripe-transfers-endpoint "/" transfer-id "/reversals")
                params
                opts)))

(defn retrieve-reversal
  "Retrieves the details of an existing transfer reversal.
   \nStripe API docs: https://stripe.com/docs/api/transfer_reversals/retrieve"
  ([stripe-client transfer-id reversal-id]
   (retrieve-reversal stripe-client transfer-id reversal-id {}))
  ([stripe-client transfer-id reversal-id opts]
   (request stripe-client :get
                (str stripe-transfers-endpoint "/" transfer-id "/reversals/" reversal-id)
                {}
                opts)))

(defn update-reversal
  "Updates the specified transfer reversal.
   \nStripe API docs: https://stripe.com/docs/api/transfer_reversals/update"
  ([stripe-client transfer-id reversal-id params]
   (update-reversal stripe-client transfer-id reversal-id params {}))
  ([stripe-client transfer-id reversal-id params opts]
   (request stripe-client :post
                (str stripe-transfers-endpoint "/" transfer-id "/reversals/" reversal-id)
                params
                opts)))

(defn list-reversals
  "Returns a list of transfer reversals.
   \nStripe API docs: https://stripe.com/docs/api/transfer_reversals/list"
  ([stripe-client transfer-id]
   (list-reversals stripe-client transfer-id {}))
  ([stripe-client transfer-id params]
   (list-reversals stripe-client transfer-id params {}))
  ([stripe-client transfer-id params opts]
   (request stripe-client :get
                (str stripe-transfers-endpoint "/" transfer-id "/reversals")
                params
                opts)))