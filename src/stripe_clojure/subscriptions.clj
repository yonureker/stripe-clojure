(ns stripe-clojure.subscriptions
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-subscriptions-endpoint (config/stripe-endpoints :subscriptions))

(defn create-subscription
  "Creates a new subscription.
   \nStripe API docs: https://stripe.com/docs/api/subscriptions/create"
  ([stripe-client params]
   (create-subscription stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-subscriptions-endpoint params opts)))

(defn retrieve-subscription
  "Retrieves a subscription.
   \nStripe API docs: https://stripe.com/docs/api/subscriptions/retrieve"
  ([stripe-client subscription-id]
   (retrieve-subscription stripe-client subscription-id {}))
  ([stripe-client subscription-id opts]
   (request stripe-client :get (str stripe-subscriptions-endpoint "/" subscription-id) {} opts)))

(defn update-subscription
  "Updates a subscription.
   \nStripe API docs: https://stripe.com/docs/api/subscriptions/update"
  ([stripe-client subscription-id params]
   (update-subscription stripe-client subscription-id params {}))
  ([stripe-client subscription-id params opts]
   (request stripe-client :post (str stripe-subscriptions-endpoint "/" subscription-id) params opts)))

(defn cancel-subscription
  "Cancels a subscription.
   \nStripe API docs: https://stripe.com/docs/api/subscriptions/cancel"
  ([stripe-client subscription-id]
   (cancel-subscription stripe-client subscription-id {}))
  ([stripe-client subscription-id opts]
   (request stripe-client :delete (str stripe-subscriptions-endpoint "/" subscription-id) nil opts)))

(defn list-subscriptions
  "Lists all subscriptions.
   \nStripe API docs: https://stripe.com/docs/api/subscriptions/list"
  ([stripe-client]
   (list-subscriptions stripe-client {}))
  ([stripe-client params]
   (list-subscriptions stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-subscriptions-endpoint params opts)))

(defn search-subscriptions
  "Searches for subscriptions.
   \nStripe API docs: https://stripe.com/docs/api/subscriptions/search"
  ([stripe-client params]
   (search-subscriptions stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get (str stripe-subscriptions-endpoint "/search") params opts)))

(defn resume-subscription
  "Resumes a paused subscription.
   \nStripe API docs: https://stripe.com/docs/api/subscriptions/resume"
  ([stripe-client subscription-id]
   (resume-subscription stripe-client subscription-id {}))
  ([stripe-client subscription-id params]
   (resume-subscription stripe-client subscription-id params {}))
  ([stripe-client subscription-id params opts]
   (request stripe-client :post (str stripe-subscriptions-endpoint "/" subscription-id "/resume") params opts)))
