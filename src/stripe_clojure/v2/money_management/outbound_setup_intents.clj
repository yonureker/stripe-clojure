(ns stripe-clojure.v2.money-management.outbound-setup-intents
  "V2 Money Management Outbound Setup Intents API.

   Outbound setup intents create and set up payout methods that can be
   used for outbound money movement.

   Stripe docs: https://docs.stripe.com/api/v2/money-management/outbound-setup-intents"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-money-management-outbound-setup-intents config/stripe-v2-endpoints))

(defn create-outbound-setup-intent
  "Creates an outbound setup intent.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: The outbound setup intent parameters
     - :payout_method - An existing payout method to link to this setup intent
     - :payout_method_data - Data used to create the payout method when none is provided
       - :type (required) - The type of payout method to create (\"bank_account\" or \"card\")
       - :bank_account - The bank account details
     - :usage_intent - The type of outbound money movement to set up (\"payment\" or \"transfer\")
   - opts: Optional request options

   Returns:
   The created outbound setup intent object.

   Example:
   (create-outbound-setup-intent client
     {:payout_method_data {:type \"bank_account\"
                           :bank_account {:country \"US\"
                                          :currency \"usd\"
                                          :account_number \"000123456789\"
                                          :routing_number \"110000000\"}}})"
  ([stripe-client params]
   (create-outbound-setup-intent stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))

(defn retrieve-outbound-setup-intent
  "Retrieves an outbound setup intent by ID.

   Parameters:
   - stripe-client: The Stripe client instance
   - outbound-setup-intent-id: The ID of the outbound setup intent
   - opts: Optional request options

   Returns:
   The outbound setup intent object.

   Example:
   (retrieve-outbound-setup-intent client \"osi_xxx\")"
  ([stripe-client outbound-setup-intent-id]
   (retrieve-outbound-setup-intent stripe-client outbound-setup-intent-id {}))
  ([stripe-client outbound-setup-intent-id opts]
   (request stripe-client :get (str endpoint "/" outbound-setup-intent-id) {} opts)))

(defn update-outbound-setup-intent
  "Updates an outbound setup intent.

   Parameters:
   - stripe-client: The Stripe client instance
   - outbound-setup-intent-id: The ID of the outbound setup intent to update
   - params: The parameters to update
     - :payout_method - An existing payout method to link to this setup intent
     - :payout_method_data - Data used to create the payout method when none is provided
   - opts: Optional request options

   Returns:
   The updated outbound setup intent object.

   Example:
   (update-outbound-setup-intent client \"osi_xxx\"
     {:payout_method \"usba_xxx\"})"
  ([stripe-client outbound-setup-intent-id params]
   (update-outbound-setup-intent stripe-client outbound-setup-intent-id params {}))
  ([stripe-client outbound-setup-intent-id params opts]
   (request stripe-client :post (str endpoint "/" outbound-setup-intent-id) params opts)))

(defn list-outbound-setup-intents
  "Lists all outbound setup intents.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: Optional query parameters
     - :limit - The page size
   - opts: Optional request options

   Returns:
   A list object containing outbound setup intents.

   Example:
   (list-outbound-setup-intents client {:limit 10})"
  ([stripe-client]
   (list-outbound-setup-intents stripe-client {}))
  ([stripe-client params]
   (list-outbound-setup-intents stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))

(defn cancel-outbound-setup-intent
  "Cancels an outbound setup intent.

   Parameters:
   - stripe-client: The Stripe client instance
   - outbound-setup-intent-id: The ID of the outbound setup intent to cancel
   - opts: Optional request options

   Returns:
   The canceled outbound setup intent object.

   Example:
   (cancel-outbound-setup-intent client \"osi_xxx\")"
  ([stripe-client outbound-setup-intent-id]
   (cancel-outbound-setup-intent stripe-client outbound-setup-intent-id {}))
  ([stripe-client outbound-setup-intent-id opts]
   (request stripe-client :post (str endpoint "/" outbound-setup-intent-id "/cancel") {} opts)))
