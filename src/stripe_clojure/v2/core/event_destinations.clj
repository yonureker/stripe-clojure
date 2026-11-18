(ns stripe-clojure.v2.core.event-destinations
  "V2 Core Event Destinations API.

   Event destinations define where Stripe sends events. You can configure
   webhook endpoints, Amazon EventBridge destinations, and more.

   Docs: https://docs.stripe.com/api/v2/core/event-destinations"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-core-event-destinations config/stripe-v2-endpoints))

(defn create-event-destination
  "Creates a new event destination.

   Docs: https://docs.stripe.com/api/v2/core/event-destinations/create"
  ([stripe-client params]
   (create-event-destination stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))

(defn retrieve-event-destination
  "Retrieves an event destination by ID.

   Docs: https://docs.stripe.com/api/v2/core/event-destinations/retrieve"
  ([stripe-client destination-id]
   (retrieve-event-destination stripe-client destination-id {}))
  ([stripe-client destination-id opts]
   (request stripe-client :get (str endpoint "/" destination-id) {} opts)))

(defn update-event-destination
  "Updates an event destination.

   Docs: https://docs.stripe.com/api/v2/core/event-destinations/update"
  ([stripe-client destination-id params]
   (update-event-destination stripe-client destination-id params {}))
  ([stripe-client destination-id params opts]
   (request stripe-client :post (str endpoint "/" destination-id) params opts)))

(defn delete-event-destination
  "Deletes an event destination.

   Docs: https://docs.stripe.com/api/v2/core/event-destinations/delete"
  ([stripe-client destination-id]
   (delete-event-destination stripe-client destination-id {}))
  ([stripe-client destination-id opts]
   (request stripe-client :delete (str endpoint "/" destination-id) {} opts)))

(defn list-event-destinations
  "Lists all event destinations.

   Docs: https://docs.stripe.com/api/v2/core/event-destinations/list"
  ([stripe-client]
   (list-event-destinations stripe-client {}))
  ([stripe-client params]
   (list-event-destinations stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))

(defn ping-event-destination
  "Sends a test event to an event destination to verify connectivity.

   Docs: https://docs.stripe.com/api/v2/core/events/ping"
  ([stripe-client destination-id]
   (ping-event-destination stripe-client destination-id {}))
  ([stripe-client destination-id opts]
   (request stripe-client :post (str endpoint "/" destination-id "/ping") {} opts)))

(defn disable-event-destination
  "Disables an event destination.

   Docs: https://docs.stripe.com/api/v2/core/event-destinations/disable"
  ([stripe-client destination-id]
   (disable-event-destination stripe-client destination-id {}))
  ([stripe-client destination-id opts]
   (request stripe-client :post (str endpoint "/" destination-id "/disable") {} opts)))

(defn enable-event-destination
  "Enables a disabled event destination.

   Docs: https://docs.stripe.com/api/v2/core/event-destinations/enable"
  ([stripe-client destination-id]
   (enable-event-destination stripe-client destination-id {}))
  ([stripe-client destination-id opts]
   (request stripe-client :post (str endpoint "/" destination-id "/enable") {} opts)))
