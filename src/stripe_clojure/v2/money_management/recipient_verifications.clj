(ns stripe-clojure.v2.money-management.recipient-verifications
  "V2 Money Management Recipient Verifications API.

   A recipient verification verifies the recipient you intend to send
   funds to.

   Stripe docs: https://docs.stripe.com/api/v2/money-management/recipient-verifications"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-money-management-recipient-verifications config/stripe-v2-endpoints))

(defn create-recipient-verification
  "Creates a recipient verification to verify the recipient you intend
   to send funds to.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: The recipient verification parameters
     - :payout_method (required) - The ID of the payout method
     - :recipient - The ID of the recipient account, required if the recipient differs from the sender
   - opts: Optional request options

   Returns:
   The created recipient verification object.

   Example:
   (create-recipient-verification client
     {:payout_method \"usba_xxx\"
      :recipient \"acct_xxx\"})"
  ([stripe-client params]
   (create-recipient-verification stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))

(defn retrieve-recipient-verification
  "Retrieves a recipient verification by ID.

   Parameters:
   - stripe-client: The Stripe client instance
   - recipient-verification-id: The ID of the recipient verification
   - opts: Optional request options

   Returns:
   The recipient verification object.

   Example:
   (retrieve-recipient-verification client \"rv_xxx\")"
  ([stripe-client recipient-verification-id]
   (retrieve-recipient-verification stripe-client recipient-verification-id {}))
  ([stripe-client recipient-verification-id opts]
   (request stripe-client :get (str endpoint "/" recipient-verification-id) {} opts)))

(defn acknowledge-recipient-verification
  "Acknowledges a recipient verification in the awaiting_acknowledgement
   state.

   Parameters:
   - stripe-client: The Stripe client instance
   - recipient-verification-id: The ID of the recipient verification to acknowledge
   - opts: Optional request options

   Returns:
   The acknowledged recipient verification object.

   Example:
   (acknowledge-recipient-verification client \"rv_xxx\")"
  ([stripe-client recipient-verification-id]
   (acknowledge-recipient-verification stripe-client recipient-verification-id {}))
  ([stripe-client recipient-verification-id opts]
   (request stripe-client :post (str endpoint "/" recipient-verification-id "/acknowledge") {} opts)))
