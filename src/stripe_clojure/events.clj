(ns stripe-clojure.events
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-events-endpoint (:events config/stripe-endpoints))

(defn retrieve-event
  "Retrieves the details of an event.
   \nStripe API docs: https://stripe.com/docs/api/events/retrieve"
  ([stripe-client event-id]
   (retrieve-event stripe-client event-id {}))
  ([stripe-client event-id opts]
   (request stripe-client :get (str stripe-events-endpoint "/" event-id) {} opts)))

(defn list-events
  "List events, going back up to 30 days.
   \nStripe API docs: https://stripe.com/docs/api/events/list"
  ([stripe-client]
   (list-events stripe-client {}))
  ([stripe-client params]
   (list-events stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-events-endpoint params opts)))
