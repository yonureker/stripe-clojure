(ns stripe-clojure.v2.money-management.outbound-setup-intents
  "V2 Money Management Outbound Setup Intents API.

   Outbound setup intents create and set up payout methods that can be
   used for outbound money movement.

   Docs: https://docs.stripe.com/api/v2/money-management/outbound-setup-intents"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-money-management-outbound-setup-intents config/stripe-v2-endpoints))

(defn create-outbound-setup-intent
  "Creates an outbound setup intent.

   Docs: https://docs.stripe.com/api/v2/money-management/outbound-setup-intents/create"
  ([stripe-client params]
   (create-outbound-setup-intent stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))

(defn retrieve-outbound-setup-intent
  "Retrieves an outbound setup intent by ID.

   Docs: https://docs.stripe.com/api/v2/money-management/outbound-setup-intents/retrieve"
  ([stripe-client outbound-setup-intent-id]
   (retrieve-outbound-setup-intent stripe-client outbound-setup-intent-id {}))
  ([stripe-client outbound-setup-intent-id opts]
   (request stripe-client :get (str endpoint "/" outbound-setup-intent-id) {} opts)))

(defn update-outbound-setup-intent
  "Updates an outbound setup intent.

   Docs: https://docs.stripe.com/api/v2/money-management/outbound-setup-intents/update"
  ([stripe-client outbound-setup-intent-id params]
   (update-outbound-setup-intent stripe-client outbound-setup-intent-id params {}))
  ([stripe-client outbound-setup-intent-id params opts]
   (request stripe-client :post (str endpoint "/" outbound-setup-intent-id) params opts)))

(defn list-outbound-setup-intents
  "Lists all outbound setup intents.

   Docs: https://docs.stripe.com/api/v2/money-management/outbound-setup-intents/list"
  ([stripe-client]
   (list-outbound-setup-intents stripe-client {}))
  ([stripe-client params]
   (list-outbound-setup-intents stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))

(defn cancel-outbound-setup-intent
  "Cancels an outbound setup intent.

   Docs: https://docs.stripe.com/api/v2/money-management/outbound-setup-intents/cancel"
  ([stripe-client outbound-setup-intent-id]
   (cancel-outbound-setup-intent stripe-client outbound-setup-intent-id {}))
  ([stripe-client outbound-setup-intent-id opts]
   (request stripe-client :post (str endpoint "/" outbound-setup-intent-id "/cancel") {} opts)))
