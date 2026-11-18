(ns stripe-clojure.v2.billing.meter-event-stream
  "V2 Billing Meter Event Stream API.

   The meter event stream endpoint enables high-throughput meter event
   submission using a session-based authentication token. This is
   designed for scenarios where you need to send many events quickly.

   Unlike the standard meter events endpoint, the stream endpoint:
   - Uses session tokens instead of API keys for authentication
   - Supports higher throughput
   - Is optimized for bulk event submission

   Docs: https://docs.stripe.com/api/v2/meter-event-streams"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-billing-meter-event-stream config/stripe-v2-endpoints))

(defn send-meter-events
  "Sends meter events using a session authentication token.

   This is a high-throughput endpoint for sending meter events. You must
   first create a meter event session to obtain an authentication token.

   Note: This endpoint uses the session's authentication token instead of
   the standard API key. The token should be passed via custom-headers.

   Docs: https://docs.stripe.com/api/v2/billing/meter-event-sessions/create-async"
  ([stripe-client params]
   (send-meter-events stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))
