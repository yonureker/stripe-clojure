(ns stripe-clojure.v2.billing.meter-events
  "V2 Billing Meter Events API.

   Meter events represent usage data for a customer's meter. This is a
   high-throughput API designed for sending usage events in real-time.

   Key features:
   - High throughput: Up to 1000 events/second in live mode
   - JSON encoding for request/response
   - Designed for real-time usage tracking

   Stripe docs: https://docs.stripe.com/api/v2/billing/meter_events"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-billing-meter-events config/stripe-v2-endpoints))

(defn create-meter-event
  "Creates a meter event to record customer usage.

   This is a high-throughput endpoint designed for real-time usage reporting.
   Events are processed asynchronously and may not be immediately reflected
   in the customer's usage totals.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: The meter event parameters
     - :event_name (required) - The name of the meter event (matches a meter's event_name)
     - :payload (required) - The event payload
       - :stripe_customer_id (required) - The customer's Stripe ID
       - :value (required) - The usage value (must be a positive integer)
       - :timestamp - Unix timestamp of when the event occurred (defaults to now)
     - :identifier - A unique identifier for this event (for idempotency)
   - opts: Optional request options

   Returns:
   The created meter event object.

   Example:
   (create-meter-event client
     {:event_name \"api_requests\"
      :payload {:stripe_customer_id \"cus_xxx\"
                :value 1}})

   (create-meter-event client
     {:event_name \"api_requests\"
      :payload {:stripe_customer_id \"cus_xxx\"
                :value 100
                :timestamp 1234567890}
      :identifier \"unique-event-id-123\"})"
  ([stripe-client params]
   (create-meter-event stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))
