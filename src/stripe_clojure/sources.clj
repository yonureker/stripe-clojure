(ns stripe-clojure.sources
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-sources-endpoint (:sources config/stripe-endpoints))

(defn create-source
  "Creates a new source object.
   \nStripe API docs: https://stripe.com/docs/api/sources/create"
  ([stripe-client params]
   (create-source stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-sources-endpoint params opts)))

(defn retrieve-source
  "Retrieves an existing source object.
   \nStripe API docs: https://stripe.com/docs/api/sources/retrieve"
  ([stripe-client source-id]
   (retrieve-source stripe-client source-id {}))
  ([stripe-client source-id opts]
   (request stripe-client :get (str stripe-sources-endpoint "/" source-id) {} opts)))

(defn update-source
  "Updates the specified source by setting the values of the parameters passed.
   \nStripe API docs: https://stripe.com/docs/api/sources/update"
  ([stripe-client source-id params]
   (update-source stripe-client source-id params {}))
  ([stripe-client source-id params opts]
   (request stripe-client :post (str stripe-sources-endpoint "/" source-id) params opts)))

(defn verify-source
  "Verifies a source.
   \nStripe API docs: https://stripe.com/docs/api/sources/verify"
  ([stripe-client source-id params]
   (verify-source stripe-client source-id params {}))
  ([stripe-client source-id params opts]
   (request stripe-client :post (str stripe-sources-endpoint "/" source-id "/verify") params opts)))

;; Source Transactions
(defn list-source-transactions
  "Lists source transactions for a source.
   \nStripe API docs: https://stripe.com/docs/api/source_transactions/list"
  ([stripe-client source-id]
   (list-source-transactions stripe-client source-id {}))
  ([stripe-client source-id params]
   (list-source-transactions stripe-client source-id params {}))
  ([stripe-client source-id params opts]
   (request stripe-client :get (str stripe-sources-endpoint "/" source-id "/source_transactions") params opts)))

(defn retrieve-source-transaction
  "Retrieves an existing source transaction object.
   \nStripe API docs: https://stripe.com/docs/api/source_transactions/retrieve"
  ([stripe-client source-id source-transaction-id]
   (retrieve-source-transaction stripe-client source-id source-transaction-id {}))
  ([stripe-client source-id source-transaction-id opts]
   (request stripe-client :get 
                (str stripe-sources-endpoint "/" source-id "/source_transactions/" source-transaction-id) 
                {} 
                opts)))

;; Mandate Notifications
(defn retrieve-mandate-notification
  "Retrieves a source mandate notification.
   \nStripe API docs: https://stripe.com/docs/api/source_mandate_notifications/retrieve"
  ([stripe-client source-id mandate-notification-id]
   (retrieve-mandate-notification stripe-client source-id mandate-notification-id {}))
  ([stripe-client source-id mandate-notification-id opts]
   (request stripe-client :get
                (str stripe-sources-endpoint "/" source-id "/mandate_notifications/" mandate-notification-id)
                {}
                opts)))