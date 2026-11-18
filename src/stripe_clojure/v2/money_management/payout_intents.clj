(ns stripe-clojure.v2.money-management.payout-intents
  "V2 Money Management Payout Intents API.

   A payout intent represents an intent to send funds from a financial
   account to a payout method.

   Docs: https://docs.stripe.com/api/v2/money-management/payout-intents"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-money-management-payout-intents config/stripe-v2-endpoints))

(defn create-payout-intent
  "Creates a payout intent.

   Docs: https://docs.stripe.com/api/v2/money-management/payout-intents/create"
  ([stripe-client params]
   (create-payout-intent stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))

(defn retrieve-payout-intent
  "Retrieves a payout intent by ID.

   Docs: https://docs.stripe.com/api/v2/money-management/payout-intents/retrieve"
  ([stripe-client payout-intent-id]
   (retrieve-payout-intent stripe-client payout-intent-id {}))
  ([stripe-client payout-intent-id opts]
   (request stripe-client :get (str endpoint "/" payout-intent-id) {} opts)))

(defn update-payout-intent
  "Updates an editable payout intent in the pending or requires_action
   state.

   Docs: https://docs.stripe.com/api/v2/money-management/payout-intents/update"
  ([stripe-client payout-intent-id params]
   (update-payout-intent stripe-client payout-intent-id params {}))
  ([stripe-client payout-intent-id params opts]
   (request stripe-client :post (str endpoint "/" payout-intent-id) params opts)))

(defn list-payout-intents
  "Lists all payout intents.

   Docs: https://docs.stripe.com/api/v2/money-management/payout-intents/list"
  ([stripe-client]
   (list-payout-intents stripe-client {}))
  ([stripe-client params]
   (list-payout-intents stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))

(defn cancel-payout-intent
  "Cancels a payout intent.

   Docs: https://docs.stripe.com/api/v2/money-management/payout-intents/cancel"
  ([stripe-client payout-intent-id]
   (cancel-payout-intent stripe-client payout-intent-id {}))
  ([stripe-client payout-intent-id opts]
   (request stripe-client :post (str endpoint "/" payout-intent-id "/cancel") {} opts)))

(defn confirm-payout-intent
  "Confirms a payout intent in the requires_action state, transitioning
   it to pending.

   Docs: https://docs.stripe.com/api/v2/money-management/payout-intents/confirm"
  ([stripe-client payout-intent-id]
   (confirm-payout-intent stripe-client payout-intent-id {}))
  ([stripe-client payout-intent-id opts]
   (request stripe-client :post (str endpoint "/" payout-intent-id "/confirm") {} opts)))

(defn refresh-fx-quote
  "Refreshes the FX quote for a payout intent.

   Docs: https://docs.stripe.com/api/v2/money-management/payout-intents/fx-quote"
  ([stripe-client payout-intent-id]
   (refresh-fx-quote stripe-client payout-intent-id {}))
  ([stripe-client payout-intent-id opts]
   (request stripe-client :post (str endpoint "/" payout-intent-id "/refresh_fx_quote") {} opts)))

(defn refresh-recipient-verification
  "Refreshes the recipient verification for a payout intent.

   Docs: https://docs.stripe.com/api/v2/money-management/payout-intents/refresh-recipient-verification"
  ([stripe-client payout-intent-id]
   (refresh-recipient-verification stripe-client payout-intent-id {}))
  ([stripe-client payout-intent-id opts]
   (request stripe-client :post (str endpoint "/" payout-intent-id "/refresh_recipient_verification") {} opts)))
