(ns stripe-clojure.setup-intents
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-setup-intents-endpoint (:setup-intents config/stripe-endpoints))

(defn create-setup-intent
  "Creates a new setup intent.
   \nStripe API docs: https://stripe.com/docs/api/setup_intents/create"
  ([stripe-client params]
   (create-setup-intent stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-setup-intents-endpoint params opts)))

(defn retrieve-setup-intent
  "Retrieves a setup intent by ID.
   \nStripe API docs: https://stripe.com/docs/api/setup_intents/retrieve"
  ([stripe-client setup-intent-id]
   (retrieve-setup-intent stripe-client setup-intent-id {}))
  ([stripe-client setup-intent-id opts]
   (request stripe-client :get (str stripe-setup-intents-endpoint "/" setup-intent-id) {} opts)))

(defn update-setup-intent
  "Updates a setup intent.
   \nStripe API docs: https://stripe.com/docs/api/setup_intents/update"
  ([stripe-client setup-intent-id params]
   (update-setup-intent stripe-client setup-intent-id params {}))
  ([stripe-client setup-intent-id params opts]
   (request stripe-client :post (str stripe-setup-intents-endpoint "/" setup-intent-id) params opts)))

(defn confirm-setup-intent
  "Confirms a setup intent.
   \nStripe API docs: https://stripe.com/docs/api/setup_intents/confirm"
  ([stripe-client setup-intent-id]
   (confirm-setup-intent stripe-client setup-intent-id {} {}))
  ([stripe-client setup-intent-id params]
   (confirm-setup-intent stripe-client setup-intent-id params {}))
  ([stripe-client setup-intent-id params opts]
   (request stripe-client :post (str stripe-setup-intents-endpoint "/" setup-intent-id "/confirm") params opts)))

(defn cancel-setup-intent
  "Cancels a setup intent.
   \nStripe API docs: https://stripe.com/docs/api/setup_intents/cancel"
  ([stripe-client setup-intent-id]
   (cancel-setup-intent stripe-client setup-intent-id {} {}))
  ([stripe-client setup-intent-id params]
   (cancel-setup-intent stripe-client setup-intent-id params {}))
  ([stripe-client setup-intent-id params opts]
   (request stripe-client :post (str stripe-setup-intents-endpoint "/" setup-intent-id "/cancel") params opts)))

(defn list-setup-intents
  "Lists all setup intents.
   \nStripe API docs: https://stripe.com/docs/api/setup_intents/list"
  ([stripe-client]
   (list-setup-intents stripe-client {}))
  ([stripe-client params]
   (list-setup-intents stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-setup-intents-endpoint params opts)))

(defn verify-microdeposits
  "Verifies microdeposits on a setup intent.
   \nStripe API docs: https://stripe.com/docs/api/setup_intents/verify_microdeposits"
  ([stripe-client setup-intent-id]
   (verify-microdeposits stripe-client setup-intent-id {} {}))
  ([stripe-client setup-intent-id params]
   (verify-microdeposits stripe-client setup-intent-id params {}))
  ([stripe-client setup-intent-id params opts]
   (request stripe-client :post (str stripe-setup-intents-endpoint "/" setup-intent-id "/verify_microdeposits") params opts)))
