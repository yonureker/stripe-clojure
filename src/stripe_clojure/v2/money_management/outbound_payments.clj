(ns stripe-clojure.v2.money-management.outbound-payments
  "V2 Money Management Outbound Payments API.

   An outbound payment is a single money movement from a financial
   account you own to a payout method someone else owns.

   Docs: https://docs.stripe.com/api/v2/money-management/outbound-payments"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-money-management-outbound-payments config/stripe-v2-endpoints))

(defn create-outbound-payment
  "Creates an outbound payment.

   Docs: https://docs.stripe.com/api/v2/money-management/outbound-payments/create"
  ([stripe-client params]
   (create-outbound-payment stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))

(defn retrieve-outbound-payment
  "Retrieves an outbound payment by ID.

   Docs: https://docs.stripe.com/api/v2/money-management/outbound-payments/retrieve"
  ([stripe-client outbound-payment-id]
   (retrieve-outbound-payment stripe-client outbound-payment-id {}))
  ([stripe-client outbound-payment-id opts]
   (request stripe-client :get (str endpoint "/" outbound-payment-id) {} opts)))

(defn list-outbound-payments
  "Lists all outbound payments.

   Docs: https://docs.stripe.com/api/v2/money-management/outbound-payments/list"
  ([stripe-client]
   (list-outbound-payments stripe-client {}))
  ([stripe-client params]
   (list-outbound-payments stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))

(defn cancel-outbound-payment
  "Cancels an outbound payment. Only processing outbound payments can
   be canceled.

   Docs: https://docs.stripe.com/api/v2/money-management/outbound-payments/cancel"
  ([stripe-client outbound-payment-id]
   (cancel-outbound-payment stripe-client outbound-payment-id {}))
  ([stripe-client outbound-payment-id opts]
   (request stripe-client :post (str endpoint "/" outbound-payment-id "/cancel") {} opts)))
