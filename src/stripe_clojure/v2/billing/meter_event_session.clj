(ns stripe-clojure.v2.billing.meter-event-session
  "V2 Billing Meter Event Session API.

   Meter event sessions provide authentication tokens for high-throughput
   meter event streaming. A session token is valid for a limited time and
   can be used to send meter events without per-request authentication.

   Docs: https://docs.stripe.com/api/v2/meter-event-streams"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-billing-meter-event-session config/stripe-v2-endpoints))

(defn create-meter-event-session
  "Creates a meter event session for high-throughput event streaming.

   The session provides an authentication token that can be used for
   sending meter events without per-request API key authentication.
   This is useful for high-volume usage tracking scenarios.

   Docs: https://docs.stripe.com/api/v2/meter-event-streams/meter-event-sessions/create"
  ([stripe-client]
   (create-meter-event-session stripe-client {}))
  ([stripe-client params]
   (create-meter-event-session stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))
