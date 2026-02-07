(ns stripe-clojure.v2.core.event-destinations
  "V2 Core Event Destinations API.

   Event destinations define where Stripe sends events. You can configure
   webhook endpoints, Amazon EventBridge destinations, and more.

   Stripe docs: https://docs.stripe.com/api/v2/core/event_destinations"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-core-event-destinations config/stripe-v2-endpoints))

(defn create-event-destination
  "Creates a new event destination.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: The event destination parameters
     - :type (required) - The type of destination (e.g., \"webhook_endpoint\", \"amazon_eventbridge\")
     - :name - A human-readable name for this destination
     - :enabled_events - The events to subscribe to (e.g., [\"*\"] for all events)
     - :webhook_endpoint - Webhook configuration (if type is webhook_endpoint)
       - :url (required) - The URL to send events to
     - :amazon_eventbridge - EventBridge configuration (if type is amazon_eventbridge)
       - :aws_account_id (required) - Your AWS account ID
       - :aws_region (required) - The AWS region
   - opts: Optional request options

   Returns:
   The created event destination object.

   Example:
   (create-event-destination client
     {:type \"webhook_endpoint\"
      :name \"My Webhook\"
      :enabled_events [\"*\"]
      :webhook_endpoint {:url \"https://example.com/webhook\"}})"
  ([stripe-client params]
   (create-event-destination stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))

(defn retrieve-event-destination
  "Retrieves an event destination by ID.

   Parameters:
   - stripe-client: The Stripe client instance
   - destination-id: The ID of the event destination
   - opts: Optional request options

   Returns:
   The event destination object.

   Example:
   (retrieve-event-destination client \"ed_xxx\")"
  ([stripe-client destination-id]
   (retrieve-event-destination stripe-client destination-id {}))
  ([stripe-client destination-id opts]
   (request stripe-client :get (str endpoint "/" destination-id) {} opts)))

(defn update-event-destination
  "Updates an event destination.

   Parameters:
   - stripe-client: The Stripe client instance
   - destination-id: The ID of the event destination to update
   - params: The parameters to update
     - :name - A human-readable name
     - :enabled_events - The events to subscribe to
     - :disabled - Whether the destination is disabled
   - opts: Optional request options

   Returns:
   The updated event destination object.

   Example:
   (update-event-destination client \"ed_xxx\"
     {:name \"Updated Webhook Name\"
      :enabled_events [\"v2.billing.*\"]})"
  ([stripe-client destination-id params]
   (update-event-destination stripe-client destination-id params {}))
  ([stripe-client destination-id params opts]
   (request stripe-client :post (str endpoint "/" destination-id) params opts)))

(defn delete-event-destination
  "Deletes an event destination.

   Parameters:
   - stripe-client: The Stripe client instance
   - destination-id: The ID of the event destination to delete
   - opts: Optional request options

   Returns:
   A confirmation of deletion.

   Example:
   (delete-event-destination client \"ed_xxx\")"
  ([stripe-client destination-id]
   (delete-event-destination stripe-client destination-id {}))
  ([stripe-client destination-id opts]
   (request stripe-client :delete (str endpoint "/" destination-id) {} opts)))

(defn list-event-destinations
  "Lists all event destinations.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: Optional query parameters
   - opts: Optional request options
     - :include - Fields to include in the response

   Returns:
   A list object containing event destinations.

   Example:
   (list-event-destinations client)"
  ([stripe-client]
   (list-event-destinations stripe-client {}))
  ([stripe-client params]
   (list-event-destinations stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))

(defn ping-event-destination
  "Sends a test event to an event destination to verify connectivity.

   Parameters:
   - stripe-client: The Stripe client instance
   - destination-id: The ID of the event destination to ping
   - opts: Optional request options

   Returns:
   The ping result.

   Example:
   (ping-event-destination client \"ed_xxx\")"
  ([stripe-client destination-id]
   (ping-event-destination stripe-client destination-id {}))
  ([stripe-client destination-id opts]
   (request stripe-client :post (str endpoint "/" destination-id "/ping") {} opts)))

(defn disable-event-destination
  "Disables an event destination.

   Parameters:
   - stripe-client: The Stripe client instance
   - destination-id: The ID of the event destination to disable
   - opts: Optional request options

   Returns:
   The updated event destination object.

   Example:
   (disable-event-destination client \"ed_xxx\")"
  ([stripe-client destination-id]
   (disable-event-destination stripe-client destination-id {}))
  ([stripe-client destination-id opts]
   (request stripe-client :post (str endpoint "/" destination-id "/disable") {} opts)))

(defn enable-event-destination
  "Enables a disabled event destination.

   Parameters:
   - stripe-client: The Stripe client instance
   - destination-id: The ID of the event destination to enable
   - opts: Optional request options

   Returns:
   The updated event destination object.

   Example:
   (enable-event-destination client \"ed_xxx\")"
  ([stripe-client destination-id]
   (enable-event-destination stripe-client destination-id {}))
  ([stripe-client destination-id opts]
   (request stripe-client :post (str endpoint "/" destination-id "/enable") {} opts)))
