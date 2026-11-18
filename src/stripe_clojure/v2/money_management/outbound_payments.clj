(ns stripe-clojure.v2.money-management.outbound-payments
  "V2 Money Management Outbound Payments API.

   An outbound payment is a single money movement from a financial
   account you own to a payout method someone else owns.

   Stripe docs: https://docs.stripe.com/api/v2/money-management/outbound-payments"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-money-management-outbound-payments config/stripe-v2-endpoints))

(defn create-outbound-payment
  "Creates an outbound payment.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: The outbound payment parameters
     - :amount (required) - The requested amount and currency
       - :value (required) - The amount in the smallest currency unit
       - :currency (required) - The currency of the amount
     - :from (required) - The financial account to pull funds from
       - :financial_account (required) - The ID of the financial account
       - :currency (required) - The currency to pull funds in
     - :to (required) - The payout method to send the outbound payment to
       - :recipient (required) - The ID of the recipient
       - :payout_method - The ID of the payout method
       - :currency - The currency to send
     - :description - An arbitrary string attached to the outbound payment
     - :metadata - Set of key-value pairs to attach to the outbound payment
     - :recipient_notification - Notification settings for the recipient
     - :recipient_verification - The ID of the recipient verification for this outbound payment
     - :statement_descriptor - The description that appears on the receiving end
   - opts: Optional request options

   Returns:
   The created outbound payment object.

   Example:
   (create-outbound-payment client
     {:amount {:value 1000 :currency \"usd\"}
      :from {:financial_account \"fa_xxx\" :currency \"usd\"}
      :to {:recipient \"acct_xxx\" :payout_method \"usba_xxx\"}})"
  ([stripe-client params]
   (create-outbound-payment stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))

(defn retrieve-outbound-payment
  "Retrieves an outbound payment by ID.

   Parameters:
   - stripe-client: The Stripe client instance
   - outbound-payment-id: The ID of the outbound payment
   - opts: Optional request options

   Returns:
   The outbound payment object.

   Example:
   (retrieve-outbound-payment client \"obp_xxx\")"
  ([stripe-client outbound-payment-id]
   (retrieve-outbound-payment stripe-client outbound-payment-id {}))
  ([stripe-client outbound-payment-id opts]
   (request stripe-client :get (str endpoint "/" outbound-payment-id) {} opts)))

(defn list-outbound-payments
  "Lists all outbound payments.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: Optional query parameters
     - :created - Filter by creation time
     - :limit - The maximum number of results to return
     - :recipient - Only return outbound payments sent to this recipient
     - :status - Only return outbound payments with this status
   - opts: Optional request options

   Returns:
   A list object containing outbound payments.

   Example:
   (list-outbound-payments client {:limit 10})"
  ([stripe-client]
   (list-outbound-payments stripe-client {}))
  ([stripe-client params]
   (list-outbound-payments stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))

(defn cancel-outbound-payment
  "Cancels an outbound payment. Only processing outbound payments can
   be canceled.

   Parameters:
   - stripe-client: The Stripe client instance
   - outbound-payment-id: The ID of the outbound payment to cancel
   - opts: Optional request options

   Returns:
   The canceled outbound payment object.

   Example:
   (cancel-outbound-payment client \"obp_xxx\")"
  ([stripe-client outbound-payment-id]
   (cancel-outbound-payment stripe-client outbound-payment-id {}))
  ([stripe-client outbound-payment-id opts]
   (request stripe-client :post (str endpoint "/" outbound-payment-id "/cancel") {} opts)))
