(ns stripe-clojure.v2.billing.meter-event-stream
  "V2 Billing Meter Event Stream API.

   The meter event stream endpoint enables high-throughput meter event
   submission using a session-based authentication token. This is
   designed for scenarios where you need to send many events quickly.

   Unlike the standard meter events endpoint, the stream endpoint:
   - Uses session tokens instead of API keys for authentication
   - Supports higher throughput
   - Is optimized for bulk event submission

   Stripe docs: https://docs.stripe.com/api/v2/billing/meter_event_stream"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-billing-meter-event-stream config/stripe-v2-endpoints))

(defn send-meter-events
  "Sends meter events using a session authentication token.

   This is a high-throughput endpoint for sending meter events. You must
   first create a meter event session to obtain an authentication token.

   Note: This endpoint uses the session's authentication token instead of
   the standard API key. The token should be passed via custom-headers.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: The stream parameters
     - :events (required) - Array of meter events to send
       Each event should have:
         - :event_name - The meter's event name
         - :payload - Event payload with stripe_customer_id and value
         - :identifier - Optional unique identifier
   - opts: Optional request options
     - :custom-headers - Should include the Authorization header with session token

   Returns:
   Confirmation of events submitted.

   Example:
   (let [session (meter-event-session/create-meter-event-session client)
         token (:authentication_token session)]
     (send-meter-events client
       {:events [{:event_name \"api_requests\"
                  :payload {:stripe_customer_id \"cus_xxx\" :value 1}}
                 {:event_name \"api_requests\"
                  :payload {:stripe_customer_id \"cus_yyy\" :value 5}}]}
       {:custom-headers {\"Authorization\" (str \"Bearer \" token)}}))"
  ([stripe-client params]
   (send-meter-events stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))
