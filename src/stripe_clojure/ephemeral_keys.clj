(ns stripe-clojure.ephemeral-keys
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-ephemeral-keys-endpoint (config/stripe-endpoints :ephemeral-keys))

(defn create-ephemeral-key
  "Creates a new ephemeral key.
   Note: Requires a Stripe API version (either configured globally or passed in opts as :stripe-version)."
  ([stripe-client params]
   (create-ephemeral-key stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-ephemeral-keys-endpoint params opts)))

(defn delete-ephemeral-key
  "Deletes an ephemeral key."
  ([stripe-client key-id]
   (delete-ephemeral-key stripe-client key-id {}))
  ([stripe-client key-id opts]
   (request stripe-client :delete
            (str stripe-ephemeral-keys-endpoint "/" key-id)
            nil
            opts))) 