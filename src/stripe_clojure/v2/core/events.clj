(ns stripe-clojure.v2.core.events
  "V2 Core Events API.

   Stripe v2 Events represent significant occurrences in your Stripe account.
   They are delivered to event destinations that you configure.

   Key differences from v1 events:
   - JSON request/response encoding
   - URL-based pagination with next_page_url
   - Uses 'include' instead of 'expand' for field expansion

   Stripe docs: https://docs.stripe.com/api/v2/core/events"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-core-events config/stripe-v2-endpoints))

(defn list-events
  "Lists all v2 core events.

   Events are returned in reverse chronological order.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: Optional query parameters
     - :object_id - Filter by object ID
     - :type - Filter by event type
     - :created - Filter by creation time
   - opts: Optional request options
     - :include - Fields to include in the response

   Returns:
   A list object containing event data and pagination info.

   Example:
   (list-events client)
   (list-events client {:type \"v2.billing.meter_event.created\"})
   (list-events client {} {:include [\"data.payload\"]})"
  ([stripe-client]
   (list-events stripe-client {}))
  ([stripe-client params]
   (list-events stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))

(defn retrieve-event
  "Retrieves a v2 core event by ID.

   Parameters:
   - stripe-client: The Stripe client instance
   - event-id: The ID of the event to retrieve (e.g., \"evt_xxx\")
   - opts: Optional request options
     - :include - Fields to include in the response

   Returns:
   The event object.

   Example:
   (retrieve-event client \"evt_1234567890\")
   (retrieve-event client \"evt_1234567890\" {:include [\"payload\"]})"
  ([stripe-client event-id]
   (retrieve-event stripe-client event-id {}))
  ([stripe-client event-id opts]
   (request stripe-client :get (str endpoint "/" event-id) {} opts)))
