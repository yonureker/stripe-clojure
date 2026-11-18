(ns stripe-clojure.v2.billing.meter-event-adjustments
  "V2 Billing Meter Event Adjustments API.

   Meter event adjustments allow you to correct or cancel previously
   reported meter events. This is useful when you need to:
   - Cancel events that were reported in error
   - Adjust usage values for billing corrections

   Docs: https://docs.stripe.com/api/v2/billing/meter-event-adjustments"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-billing-meter-event-adjustments config/stripe-v2-endpoints))

(defn create-meter-event-adjustment
  "Creates a meter event adjustment to correct or cancel a previous event.

   Use this to make billing corrections when meter events were reported
   incorrectly. Adjustments can cancel events entirely or adjust their
   values.

   Docs: https://docs.stripe.com/api/v2/billing/meter-event-adjustments/create"
  ([stripe-client params]
   (create-meter-event-adjustment stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))
