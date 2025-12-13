(ns stripe-clojure.payment-intents
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-payment-intents-endpoint (:payment-intents config/stripe-endpoints))

(defn create-payment-intent
  "Creates a new payment intent.
   \nStripe API docs: https://stripe.com/docs/api/payment_intents/create"
  ([stripe-client params]
   (create-payment-intent stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-payment-intents-endpoint params opts)))

(defn retrieve-payment-intent
  "Retrieves a payment intent by ID.
   \nStripe API docs: https://stripe.com/docs/api/payment_intents/retrieve"
  ([stripe-client payment-intent-id]
   (retrieve-payment-intent stripe-client payment-intent-id {}))
  ([stripe-client payment-intent-id opts]
   (request stripe-client :get
            (str stripe-payment-intents-endpoint "/" payment-intent-id)
            {}
            opts)))

(defn update-payment-intent
  "Updates a payment intent.
   \nStripe API docs: https://stripe.com/docs/api/payment_intents/update"
  ([stripe-client payment-intent-id params]
   (update-payment-intent stripe-client payment-intent-id params {}))
  ([stripe-client payment-intent-id params opts]
   (request stripe-client :post
            (str stripe-payment-intents-endpoint "/" payment-intent-id)
            params
            opts)))

(defn confirm-payment-intent
  "Confirms a payment intent.
   \nStripe API docs: https://stripe.com/docs/api/payment_intents/confirm"
  ([stripe-client payment-intent-id]
   (confirm-payment-intent stripe-client payment-intent-id {} {}))
  ([stripe-client payment-intent-id params]
   (confirm-payment-intent stripe-client payment-intent-id params {}))
  ([stripe-client payment-intent-id params opts]
   (request stripe-client :post
                (str stripe-payment-intents-endpoint "/" payment-intent-id "/confirm")
                params
                opts)))

(defn capture-payment-intent
  "Captures a payment intent.
   \nStripe API docs: https://stripe.com/docs/api/payment_intents/capture"
  ([stripe-client payment-intent-id]
   (capture-payment-intent stripe-client payment-intent-id {} {}))
  ([stripe-client payment-intent-id params]
   (capture-payment-intent stripe-client payment-intent-id params {}))
  ([stripe-client payment-intent-id params opts]
   (request stripe-client :post
                (str stripe-payment-intents-endpoint "/" payment-intent-id "/capture")
                params
                opts)))

(defn cancel-payment-intent
  "Cancels a payment intent.
   \nStripe API docs: https://stripe.com/docs/api/payment_intents/cancel"
  ([stripe-client payment-intent-id]
   (cancel-payment-intent stripe-client payment-intent-id {} {}))
  ([stripe-client payment-intent-id params]
   (cancel-payment-intent stripe-client payment-intent-id params {}))
  ([stripe-client payment-intent-id params opts]
   (request stripe-client :post
                (str stripe-payment-intents-endpoint "/" payment-intent-id "/cancel")
                params
                opts)))

(defn list-payment-intents
  "Lists all payment intents.
   \nStripe API docs: https://stripe.com/docs/api/payment_intents/list"
  ([stripe-client]
   (list-payment-intents stripe-client {}))
  ([stripe-client params]
   (list-payment-intents stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-payment-intents-endpoint params opts)))

(defn increment-authorization
  "Increments the authorization on a payment intent.
   \nStripe API docs: https://stripe.com/docs/api/payment_intents/increment_authorization"
  ([stripe-client payment-intent-id]
   (increment-authorization stripe-client payment-intent-id {} {}))
  ([stripe-client payment-intent-id params]
   (increment-authorization stripe-client payment-intent-id params {}))
  ([stripe-client payment-intent-id params opts]
   (request stripe-client :post
                (str stripe-payment-intents-endpoint "/" payment-intent-id "/increment_authorization")
                params
                opts)))

(defn search-payment-intents
  "Searches for payment intents.
   \nStripe API docs: https://stripe.com/docs/api/payment_intents/search"
  ([stripe-client params]
   (search-payment-intents stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get
                (str stripe-payment-intents-endpoint "/search")
                params
                opts)))

(defn verify-microdeposits
  "Verifies microdeposits on a payment intent.
   \nStripe API docs: https://stripe.com/docs/api/payment_intents/verify_microdeposits"
  ([stripe-client payment-intent-id]
   (verify-microdeposits stripe-client payment-intent-id {} {}))
  ([stripe-client payment-intent-id params]
   (verify-microdeposits stripe-client payment-intent-id params {}))
  ([stripe-client payment-intent-id params opts]
   (request stripe-client :post
                (str stripe-payment-intents-endpoint "/" payment-intent-id "/verify_microdeposits")
                params
                opts)))

(defn apply-customer-balance
  "Applies the customer's balance to a payment intent.
   \nStripe API docs: https://stripe.com/docs/api/payment_intents/apply_customer_balance"
  ([stripe-client payment-intent-id]
   (apply-customer-balance stripe-client payment-intent-id {} {}))
  ([stripe-client payment-intent-id params]
   (apply-customer-balance stripe-client payment-intent-id params {}))
  ([stripe-client payment-intent-id params opts]
   (request stripe-client :post
                (str stripe-payment-intents-endpoint "/" payment-intent-id "/apply_customer_balance")
                params
                opts)))

(defn list-amount-details-line-items
  "Lists all line items of a given payment intent.
   \nStripe API docs: https://docs.stripe.com/api/payment_intents/line_items"
  ([stripe-client payment-intent-id]
   (list-amount-details-line-items stripe-client payment-intent-id {}))
  ([stripe-client payment-intent-id params]
   (list-amount-details-line-items stripe-client payment-intent-id params {}))
  ([stripe-client payment-intent-id params opts]
   (request stripe-client :get
                (str stripe-payment-intents-endpoint "/" payment-intent-id "/amount_details_line_items")
                params
                opts)))
