(ns stripe-clojure.v2.money-management.inbound-transfers
  "V2 Money Management Inbound Transfers API.

   An inbound transfer pulls funds from a bank account you own into
   one of your financial accounts.

   Stripe docs: https://docs.stripe.com/api/v2/money-management/inbound-transfers"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-money-management-inbound-transfers config/stripe-v2-endpoints))

(defn create-inbound-transfer
  "Creates an inbound transfer to pull funds from a bank account into
   a financial account.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: The inbound transfer parameters
     - :amount (required) - The amount and currency to credit to the financial account
       - :value (required) - The amount in the smallest currency unit
       - :currency (required) - The currency of the amount
     - :from (required) - The verified bank account credential to debit
       - :payment_method (required) - The ID of the payment method to debit
       - :currency - The currency to debit
     - :to (required) - The financial account and currency to credit
       - :financial_account (required) - The ID of the financial account to credit
       - :currency (required) - The currency to credit
     - :description - An optional, freeform description
   - opts: Optional request options

   Returns:
   The created inbound transfer object.

   Example:
   (create-inbound-transfer client
     {:amount {:value 1000 :currency \"usd\"}
      :from {:payment_method \"usba_xxx\"}
      :to {:financial_account \"fa_xxx\" :currency \"usd\"}})"
  ([stripe-client params]
   (create-inbound-transfer stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))

(defn retrieve-inbound-transfer
  "Retrieves an inbound transfer by ID.

   Parameters:
   - stripe-client: The Stripe client instance
   - inbound-transfer-id: The ID of the inbound transfer
   - opts: Optional request options

   Returns:
   The inbound transfer object.

   Example:
   (retrieve-inbound-transfer client \"ibt_xxx\")"
  ([stripe-client inbound-transfer-id]
   (retrieve-inbound-transfer stripe-client inbound-transfer-id {}))
  ([stripe-client inbound-transfer-id opts]
   (request stripe-client :get (str endpoint "/" inbound-transfer-id) {} opts)))

(defn list-inbound-transfers
  "Lists all inbound transfers.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: Optional query parameters
     - :created - Filter by creation time
     - :limit - The page limit
   - opts: Optional request options

   Returns:
   A list object containing inbound transfers.

   Example:
   (list-inbound-transfers client {:limit 10})"
  ([stripe-client]
   (list-inbound-transfers stripe-client {}))
  ([stripe-client params]
   (list-inbound-transfers stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))
