(ns stripe-clojure.webhook-endpoints
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-webhook-endpoints-endpoint (config/stripe-endpoints :webhook-endpoints))

(defn create-webhook-endpoint
  "Creates a new webhook endpoint.
   \nStripe API docs: https://stripe.com/docs/api/webhook_endpoints/create"
  ([stripe-client params]
   (create-webhook-endpoint stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-webhook-endpoints-endpoint params opts)))

(defn retrieve-webhook-endpoint
  "Retrieves the webhook endpoint with the given ID.
   \nStripe API docs: https://stripe.com/docs/api/webhook_endpoints/retrieve"
  ([stripe-client webhook-endpoint-id]
   (retrieve-webhook-endpoint stripe-client webhook-endpoint-id {}))
  ([stripe-client webhook-endpoint-id opts]
   (request stripe-client :get
                (str stripe-webhook-endpoints-endpoint "/" webhook-endpoint-id)
                {}
                opts)))

(defn update-webhook-endpoint
  "Updates the webhook endpoint.
   \nStripe API docs: https://stripe.com/docs/api/webhook_endpoints/update"
  ([stripe-client webhook-endpoint-id params]
   (update-webhook-endpoint stripe-client webhook-endpoint-id params {}))
  ([stripe-client webhook-endpoint-id params opts]
   (request stripe-client :post
            (str stripe-webhook-endpoints-endpoint "/" webhook-endpoint-id)
            params
            opts)))

(defn list-webhook-endpoints
  "Lists all webhook endpoints.
   \nStripe API docs: https://stripe.com/docs/api/webhook_endpoints/list"
  ([stripe-client]
   (list-webhook-endpoints stripe-client {}))
  ([stripe-client params]
   (list-webhook-endpoints stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-webhook-endpoints-endpoint params opts)))

(defn delete-webhook-endpoint
  "Deletes a webhook endpoint.
   \nStripe API docs: https://stripe.com/docs/api/webhook_endpoints/delete"
  ([stripe-client webhook-endpoint-id]
   (delete-webhook-endpoint stripe-client webhook-endpoint-id {}))
  ([stripe-client webhook-endpoint-id opts]
   (request stripe-client :delete
                (str stripe-webhook-endpoints-endpoint "/" webhook-endpoint-id)
                nil
                opts)))