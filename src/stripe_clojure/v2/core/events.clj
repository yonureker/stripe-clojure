(ns stripe-clojure.v2.core.events
  "V2 Core Events API.

   Stripe v2 Events represent significant occurrences in your Stripe account.
   They are delivered to event destinations that you configure.

   Key differences from v1 events:
   - JSON request/response encoding
   - URL-based pagination with next_page_url
   - Uses 'include' instead of 'expand' for field expansion

   Docs: https://docs.stripe.com/api/v2/core/events"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-core-events config/stripe-v2-endpoints))

(defn list-events
  "Lists all v2 core events.

   Events are returned in reverse chronological order.

   Docs: https://docs.stripe.com/api/v2/core/events/list"
  ([stripe-client]
   (list-events stripe-client {}))
  ([stripe-client params]
   (list-events stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))

(defn retrieve-event
  "Retrieves a v2 core event by ID.

   Docs: https://docs.stripe.com/api/v2/core/events/retrieve"
  ([stripe-client event-id]
   (retrieve-event stripe-client event-id {}))
  ([stripe-client event-id opts]
   (request stripe-client :get (str endpoint "/" event-id) {} opts)))
