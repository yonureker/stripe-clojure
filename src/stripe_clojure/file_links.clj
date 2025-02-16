(ns stripe-clojure.file-links
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-file-links-endpoint (:file-links config/stripe-endpoints))

(defn create-file-link
  "Creates a new file link.
   \nStripe API docs: https://stripe.com/docs/api/file_links/create"
  ([stripe-client params]
   (create-file-link stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-file-links-endpoint params opts)))

(defn retrieve-file-link
  "Retrieves a file link by ID.
   \nStripe API docs: https://stripe.com/docs/api/file_links/retrieve"
  ([stripe-client file-link-id]
   (retrieve-file-link stripe-client file-link-id {}))
  ([stripe-client file-link-id opts]
   (request stripe-client :get 
                        (str stripe-file-links-endpoint "/" file-link-id)
                        {}
                        opts)))

(defn update-file-link
  "Updates a file link.
   \nStripe API docs: https://stripe.com/docs/api/file_links/update"
  ([stripe-client file-link-id params]
   (update-file-link stripe-client file-link-id params {}))
  ([stripe-client file-link-id params opts]
   (request stripe-client :post
                        (str stripe-file-links-endpoint "/" file-link-id)
                        params
                        opts)))

(defn list-file-links
  "Lists all file links.
   \nStripe API docs: https://stripe.com/docs/api/file_links/list"
  ([stripe-client]
   (list-file-links stripe-client {}))
  ([stripe-client params]
   (list-file-links stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-file-links-endpoint params opts)))