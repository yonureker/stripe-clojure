(ns stripe-clojure.billing.meter-event-adjustments
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-billing-meter-event-adjustments-endpoint (config/stripe-endpoints :billing-meter-event-adjustments))

(defn create-meter-event-adjustment
  "Creates a new meter event adjustment.
   \nStripe API docs: https://stripe.com/docs/api/billing/meter-event-adjustment/create"
  ([stripe-client params]
   (create-meter-event-adjustment stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-billing-meter-event-adjustments-endpoint params opts)))