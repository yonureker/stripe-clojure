(ns stripe-clojure.subscription-items
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-subscription-items-endpoint (config/stripe-endpoints :subscription-items))

(defn create-subscription-item
  "Creates a new subscription item on an existing subscription.
   \nStripe API docs: https://stripe.com/docs/api/subscription_items/create"
  ([stripe-client params]
   (create-subscription-item stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-subscription-items-endpoint params opts)))

(defn retrieve-subscription-item
  "Retrieves the subscription item with the given ID.
   \nStripe API docs: https://stripe.com/docs/api/subscription_items/retrieve"
  ([stripe-client subscription-item-id]
   (retrieve-subscription-item stripe-client subscription-item-id {}))
  ([stripe-client subscription-item-id opts]
   (request stripe-client :get
            (str stripe-subscription-items-endpoint "/" subscription-item-id)
            {}
            opts)))

(defn update-subscription-item
  "Updates the specified subscription item by setting the values of the parameters passed.
   \nStripe API docs: https://stripe.com/docs/api/subscription_items/update"
  ([stripe-client subscription-item-id params]
   (update-subscription-item stripe-client subscription-item-id params {}))
  ([stripe-client subscription-item-id params opts]
   (request stripe-client :post
            (str stripe-subscription-items-endpoint "/" subscription-item-id)
            params
            opts)))

(defn list-subscription-items
  "Returns a list of your subscription items.
   \nStripe API docs: https://stripe.com/docs/api/subscription_items/list"
  ([stripe-client]
   (list-subscription-items stripe-client {}))
  ([stripe-client params]
   (list-subscription-items stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-subscription-items-endpoint params opts)))

(defn delete-subscription-item
  "Deletes a subscription item.
   \nStripe API docs: https://stripe.com/docs/api/subscription_items/delete"
  ([stripe-client subscription-item-id]
   (delete-subscription-item stripe-client subscription-item-id {}))
  ([stripe-client subscription-item-id opts]
   (request stripe-client :delete
            (str stripe-subscription-items-endpoint "/" subscription-item-id)
            nil
            opts)))

(defn create-usage-record
  "Creates a usage record for a specified subscription item.
   \nStripe API docs: https://stripe.com/docs/api/usage_records/create"
  ([stripe-client subscription-item-id params]
   (create-usage-record stripe-client subscription-item-id params {}))
  ([stripe-client subscription-item-id params opts]
   (request stripe-client :post
            (str stripe-subscription-items-endpoint "/" subscription-item-id "/usage_records")
            params
            opts)))

(defn list-usage-record-summaries
  "Lists all usage record summaries for a specified subscription item.
   \nStripe API docs: https://stripe.com/docs/api/usage_records/subscription_item_summary_list"
  ([stripe-client subscription-item-id]
   (list-usage-record-summaries stripe-client subscription-item-id {}))
  ([stripe-client subscription-item-id params]
   (list-usage-record-summaries stripe-client subscription-item-id params {}))
  ([stripe-client subscription-item-id params opts]
   (request stripe-client :get
            (str stripe-subscription-items-endpoint "/" subscription-item-id "/usage_record_summaries")
            params
            opts)))