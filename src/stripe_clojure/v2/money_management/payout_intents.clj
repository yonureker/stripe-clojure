(ns stripe-clojure.v2.money-management.payout-intents
  "V2 Money Management Payout Intents API.

   A payout intent represents an intent to send funds from a financial
   account to a payout method.

   Stripe docs: https://docs.stripe.com/api/v2/money-management/payout-intents"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-money-management-payout-intents config/stripe-v2-endpoints))

(defn create-payout-intent
  "Creates a payout intent.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: The payout intent parameters
     - :amount (required) - The monetary amount to be sent
       - :value (required) - The amount in the smallest currency unit
       - :currency (required) - The currency of the amount
     - :from (required) - The financial account that funds are pulled from
       - :financial_account (required) - The ID of the financial account
       - :currency (required) - The currency to pull funds in
     - :to (required) - The payout method the payout is sent to
       - :recipient - The ID of the recipient
       - :payout_method - The ID of the payout method
       - :currency - The currency to send
     - :confirmation_method - Controls whether the payout intent requires explicit confirmation
     - :description - An arbitrary string attached to the payout intent
     - :metadata - Set of key-value pairs to attach to the payout intent
     - :recipient_notification - Notification settings for the recipient
     - :schedule_options - Scheduling options for the payout
     - :statement_descriptor - The description that appears on the receiving end
   - opts: Optional request options

   Returns:
   The created payout intent object.

   Example:
   (create-payout-intent client
     {:amount {:value 1000 :currency \"usd\"}
      :from {:financial_account \"fa_xxx\" :currency \"usd\"}
      :to {:recipient \"acct_xxx\" :payout_method \"usba_xxx\"}})"
  ([stripe-client params]
   (create-payout-intent stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))

(defn retrieve-payout-intent
  "Retrieves a payout intent by ID.

   Parameters:
   - stripe-client: The Stripe client instance
   - payout-intent-id: The ID of the payout intent
   - opts: Optional request options

   Returns:
   The payout intent object.

   Example:
   (retrieve-payout-intent client \"poi_xxx\")"
  ([stripe-client payout-intent-id]
   (retrieve-payout-intent stripe-client payout-intent-id {}))
  ([stripe-client payout-intent-id opts]
   (request stripe-client :get (str endpoint "/" payout-intent-id) {} opts)))

(defn update-payout-intent
  "Updates an editable payout intent in the pending or requires_action
   state.

   Parameters:
   - stripe-client: The Stripe client instance
   - payout-intent-id: The ID of the payout intent to update
   - params: The parameters to update
     - :description - An arbitrary string attached to the payout intent
     - :metadata - Set of key-value pairs to attach to the payout intent
     - :recipient_notification - Notification settings for the recipient
     - :schedule_options - Scheduling options for the payout
     - :statement_descriptor - The description that appears on the receiving end
     - :to - The payout method the payout is sent to
   - opts: Optional request options

   Returns:
   The updated payout intent object.

   Example:
   (update-payout-intent client \"poi_xxx\"
     {:description \"Supplier payout\"})"
  ([stripe-client payout-intent-id params]
   (update-payout-intent stripe-client payout-intent-id params {}))
  ([stripe-client payout-intent-id params opts]
   (request stripe-client :post (str endpoint "/" payout-intent-id) params opts)))

(defn list-payout-intents
  "Lists all payout intents.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: Optional query parameters
     - :limit - Maximum number of objects to return
   - opts: Optional request options

   Returns:
   A list object containing payout intents.

   Example:
   (list-payout-intents client {:limit 10})"
  ([stripe-client]
   (list-payout-intents stripe-client {}))
  ([stripe-client params]
   (list-payout-intents stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))

(defn cancel-payout-intent
  "Cancels a payout intent.

   Parameters:
   - stripe-client: The Stripe client instance
   - payout-intent-id: The ID of the payout intent to cancel
   - opts: Optional request options

   Returns:
   The canceled payout intent object.

   Example:
   (cancel-payout-intent client \"poi_xxx\")"
  ([stripe-client payout-intent-id]
   (cancel-payout-intent stripe-client payout-intent-id {}))
  ([stripe-client payout-intent-id opts]
   (request stripe-client :post (str endpoint "/" payout-intent-id "/cancel") {} opts)))

(defn confirm-payout-intent
  "Confirms a payout intent in the requires_action state, transitioning
   it to pending.

   Parameters:
   - stripe-client: The Stripe client instance
   - payout-intent-id: The ID of the payout intent to confirm
   - opts: Optional request options

   Returns:
   The confirmed payout intent object.

   Example:
   (confirm-payout-intent client \"poi_xxx\")"
  ([stripe-client payout-intent-id]
   (confirm-payout-intent stripe-client payout-intent-id {}))
  ([stripe-client payout-intent-id opts]
   (request stripe-client :post (str endpoint "/" payout-intent-id "/confirm") {} opts)))

(defn refresh-fx-quote
  "Refreshes the FX quote for a payout intent.

   Parameters:
   - stripe-client: The Stripe client instance
   - payout-intent-id: The ID of the payout intent
   - opts: Optional request options

   Returns:
   The payout intent object.

   Example:
   (refresh-fx-quote client \"poi_xxx\")"
  ([stripe-client payout-intent-id]
   (refresh-fx-quote stripe-client payout-intent-id {}))
  ([stripe-client payout-intent-id opts]
   (request stripe-client :post (str endpoint "/" payout-intent-id "/refresh_fx_quote") {} opts)))

(defn refresh-recipient-verification
  "Refreshes the recipient verification for a payout intent.

   Parameters:
   - stripe-client: The Stripe client instance
   - payout-intent-id: The ID of the payout intent
   - opts: Optional request options

   Returns:
   The payout intent object.

   Example:
   (refresh-recipient-verification client \"poi_xxx\")"
  ([stripe-client payout-intent-id]
   (refresh-recipient-verification stripe-client payout-intent-id {}))
  ([stripe-client payout-intent-id opts]
   (request stripe-client :post (str endpoint "/" payout-intent-id "/refresh_recipient_verification") {} opts)))
