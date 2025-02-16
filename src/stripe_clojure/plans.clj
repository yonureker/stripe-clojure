(ns stripe-clojure.plans
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-plans-endpoint (config/stripe-endpoints :plans))

(defn create-plan
  "Creates a new plan.
   \nStripe API docs: https://stripe.com/docs/api/plans/create"
  ([stripe-client params]
   (create-plan stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-plans-endpoint params opts)))

(defn retrieve-plan
  "Retrieves a plan.
   \nStripe API docs: https://stripe.com/docs/api/plans/retrieve"
  ([stripe-client plan-id]
   (retrieve-plan stripe-client plan-id {}))
  ([stripe-client plan-id opts]
   (request stripe-client :get (str stripe-plans-endpoint "/" plan-id) {} opts)))

(defn update-plan
  "Updates a plan.
   \nStripe API docs: https://stripe.com/docs/api/plans/update"
  ([stripe-client plan-id params]
   (update-plan stripe-client plan-id params {}))
  ([stripe-client plan-id params opts]
   (request stripe-client :post (str stripe-plans-endpoint "/" plan-id) params opts)))

(defn list-plans
  "Lists all plans.
   \nStripe API docs: https://stripe.com/docs/api/plans/list"
  ([stripe-client]
   (list-plans stripe-client {}))
  ([stripe-client params]
   (list-plans stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-plans-endpoint params opts)))

(defn delete-plan
  "Deletes a plan.
   \nStripe API docs: https://stripe.com/docs/api/plans/delete"
  ([stripe-client plan-id]
   (delete-plan stripe-client plan-id {}))
  ([stripe-client plan-id opts]
   (request stripe-client :delete (str stripe-plans-endpoint "/" plan-id) nil opts)))