(ns stripe-clojure.billing.meters
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-billing-meters-endpoint (config/stripe-endpoints :billing-meters))

(defn create-meter
  "Creates a new meter.
   \nStripe API docs: https://stripe.com/docs/api/billing/meter/create"
  ([stripe-client params]
   (create-meter stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-billing-meters-endpoint params opts)))

(defn retrieve-meter
  "Retrieves a meter.
   \nStripe API docs: https://stripe.com/docs/api/billing/meter/retrieve"
  ([stripe-client meter-id]
   (retrieve-meter stripe-client meter-id {}))
  ([stripe-client meter-id opts]
   (request stripe-client :get
            (str stripe-billing-meters-endpoint "/" meter-id)
            {}
            opts)))

(defn update-meter
  "Updates a meter.
   \nStripe API docs: https://stripe.com/docs/api/billing/meter/update"
  ([stripe-client meter-id params]
   (update-meter stripe-client meter-id params {}))
  ([stripe-client meter-id params opts]
   (request stripe-client :post
            (str stripe-billing-meters-endpoint "/" meter-id)
            params
            opts)))

(defn list-meters
  "Lists all meters.
   \nStripe API docs: https://stripe.com/docs/api/billing/meter/list"
  ([stripe-client]
   (list-meters stripe-client {} {}))
  ([stripe-client params]
   (list-meters stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-billing-meters-endpoint params opts)))

(defn deactivate-meter
  "Deactivates a meter.
   \nStripe API docs: https://stripe.com/docs/api/billing/meter/deactivate"
  ([stripe-client meter-id]
   (deactivate-meter stripe-client meter-id {}))
  ([stripe-client meter-id opts]
   (request stripe-client :post
            (str stripe-billing-meters-endpoint "/" meter-id "/deactivate")
            {}
            opts)))

(defn list-event-summaries
  "Lists all event summaries for a meter.
   \nStripe API docs: https://stripe.com/docs/api/billing/meter-event-summary/list"
  ([stripe-client meter-id params]
   (list-event-summaries stripe-client meter-id params {}))
  ([stripe-client meter-id params opts]
   (request stripe-client :get
            (str stripe-billing-meters-endpoint "/" meter-id "/event_summaries")
            params
            opts)))

(defn reactivate-meter
  "Reactivates a meter.
   \nStripe API docs: https://stripe.com/docs/api/billing/meter/reactivate"
  ([stripe-client meter-id]
   (reactivate-meter stripe-client meter-id {}))
  ([stripe-client meter-id opts]
   (request stripe-client :post
            (str stripe-billing-meters-endpoint "/" meter-id "/reactivate")
            {}
            opts)))