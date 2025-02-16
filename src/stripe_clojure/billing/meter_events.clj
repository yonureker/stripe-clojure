(ns stripe-clojure.billing.meter-events
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-billing-meter-events-endpoint (config/stripe-endpoints :billing-meter-events))

(defn create-meter-event
  "Creates a new meter event.
   \nStripe API docs: https://stripe.com/docs/api/billing/meter-event/create"
  ([stripe-client params]
   (create-meter-event stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-billing-meter-events-endpoint params opts)))