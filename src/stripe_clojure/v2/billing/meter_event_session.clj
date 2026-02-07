(ns stripe-clojure.v2.billing.meter-event-session
  "V2 Billing Meter Event Session API.

   Meter event sessions provide authentication tokens for high-throughput
   meter event streaming. A session token is valid for a limited time and
   can be used to send meter events without per-request authentication.

   Stripe docs: https://docs.stripe.com/api/v2/billing/meter_event_session"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-billing-meter-event-session config/stripe-v2-endpoints))

(defn create-meter-event-session
  "Creates a meter event session for high-throughput event streaming.

   The session provides an authentication token that can be used for
   sending meter events without per-request API key authentication.
   This is useful for high-volume usage tracking scenarios.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: Optional session parameters (currently no parameters supported)
   - opts: Optional request options

   Returns:
   A meter event session object containing:
   - :authentication_token - Token for authenticating meter event requests
   - :expires_at - Unix timestamp when the session expires
   - :created - Unix timestamp when the session was created

   Example:
   (create-meter-event-session client)

   Usage with the streaming endpoint:
   (let [session (create-meter-event-session client)]
     ;; Use session's authentication_token for streaming meter events
     (:authentication_token session))"
  ([stripe-client]
   (create-meter-event-session stripe-client {}))
  ([stripe-client params]
   (create-meter-event-session stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))
