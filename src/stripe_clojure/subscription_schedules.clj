(ns stripe-clojure.subscription-schedules
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-subscription-schedules-endpoint
  (config/stripe-endpoints :subscription-schedules))

(defn create-subscription-schedule
  "Creates a new subscription schedule.
   \nStripe API docs: https://stripe.com/docs/api/subscription_schedules/create"
  ([stripe-client params]
   (create-subscription-schedule stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-subscription-schedules-endpoint params opts)))

(defn retrieve-subscription-schedule
  "Retrieves the subscription schedule with the given ID.
   \nStripe API docs: https://stripe.com/docs/api/subscription_schedules/retrieve"
  ([stripe-client schedule-id]
   (retrieve-subscription-schedule stripe-client schedule-id {}))
  ([stripe-client schedule-id opts]
   (request stripe-client :get
            (str stripe-subscription-schedules-endpoint "/" schedule-id)
            {}
            opts)))

(defn update-subscription-schedule
  "Updates the specified subscription schedule by setting the values of the parameters passed.
   \nStripe API docs: https://stripe.com/docs/api/subscription_schedules/update"
  ([stripe-client schedule-id params]
   (update-subscription-schedule stripe-client schedule-id params {}))
  ([stripe-client schedule-id params opts]
   (request stripe-client :post
            (str stripe-subscription-schedules-endpoint "/" schedule-id)
            params
            opts)))

(defn list-subscription-schedules
  "Returns a list of your subscription schedules.
   \nStripe API docs: https://stripe.com/docs/api/subscription_schedules/list"
  ([stripe-client]
   (list-subscription-schedules stripe-client {}))
  ([stripe-client params]
   (list-subscription-schedules stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-subscription-schedules-endpoint params opts)))

(defn cancel-subscription-schedule
  "Cancels a subscription schedule.
   \nStripe API docs: https://stripe.com/docs/api/subscription_schedules/cancel"
  ([stripe-client schedule-id]
   (cancel-subscription-schedule stripe-client schedule-id {}))
  ([stripe-client schedule-id opts]
   (request stripe-client :post
            (str stripe-subscription-schedules-endpoint "/" schedule-id "/cancel")
            {}
            opts)))

(defn release-subscription-schedule
  "Releases the subscription schedule.
   \nStripe API docs: https://stripe.com/docs/api/subscription_schedules/release"
  ([stripe-client schedule-id]
   (release-subscription-schedule stripe-client schedule-id {}))
  ([stripe-client schedule-id opts]
   (request stripe-client :post
            (str stripe-subscription-schedules-endpoint "/" schedule-id "/release")
            {}
            opts)))