(ns stripe-clojure.v2.billing.meter-events
  "V2 Billing Meter Events API.

   Meter events represent usage data for a customer's meter. This is a
   high-throughput API designed for sending usage events in real-time.

   Key features:
   - High throughput: Up to 1000 events/second in live mode
   - JSON encoding for request/response
   - Designed for real-time usage tracking

   Docs: https://docs.stripe.com/api/v2/billing/meter-events/create"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-billing-meter-events config/stripe-v2-endpoints))

(defn create-meter-event
  "Creates a meter event to record customer usage.

   This is a high-throughput endpoint designed for real-time usage reporting.
   Events are processed asynchronously and may not be immediately reflected
   in the customer's usage totals.

   Docs: https://docs.stripe.com/api/v2/billing/meter-events/create"
  ([stripe-client params]
   (create-meter-event stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))
