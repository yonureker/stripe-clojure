(ns stripe-clojure.v2.test-helpers.money-management.recipient-verifications
  "V2 Money Management Recipient Verifications test helpers.

   Test helpers create recipient verifications with a chosen match result
   in a sandbox environment.

   Stripe docs: https://docs.stripe.com/api/v2/money-management/recipient-verifications"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-test-helpers-money-management-recipient-verifications config/stripe-v2-endpoints))

(defn create-recipient-verification
  "Creates a recipient verification with a specified match result for
   testing in a sandbox environment.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: The recipient verification parameters
     - :match_result (required) - The expected match level (\"match\", \"close_match\", \"no_match\", \"unavailable\")
     - :payout_method (required) - The ID of the payout method
     - :recipient - The ID of the recipient account, required if the recipient differs from the sender
   - opts: Optional request options

   Returns:
   The created recipient verification object.

   Example:
   (create-recipient-verification client
     {:match_result \"match\"
      :payout_method \"usba_xxx\"})"
  ([stripe-client params]
   (create-recipient-verification stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))
