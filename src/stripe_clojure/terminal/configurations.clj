(ns stripe-clojure.terminal.configurations
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-terminal-configurations-endpoint (config/stripe-endpoints :terminal-configurations))

(defn create-configuration
  "Creates a new terminal configuration.
   \nStripe API docs: https://stripe.com/docs/api/terminal/configuration/create"
  ([stripe-client params]
   (create-configuration stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-terminal-configurations-endpoint params opts)))

(defn retrieve-configuration
  "Retrieves a terminal configuration.
   \nStripe API docs: https://stripe.com/docs/api/terminal/configuration/retrieve"
  ([stripe-client configuration-id]
   (retrieve-configuration stripe-client configuration-id {}))
  ([stripe-client configuration-id opts]
   (request stripe-client :get
                (str stripe-terminal-configurations-endpoint "/" configuration-id)
                {}
                opts)))

(defn update-configuration
  "Updates a terminal configuration.
   \nStripe API docs: https://stripe.com/docs/api/terminal/configuration/update"
  ([stripe-client configuration-id params]
   (update-configuration stripe-client configuration-id params {}))
  ([stripe-client configuration-id params opts]
   (request stripe-client :post
                (str stripe-terminal-configurations-endpoint "/" configuration-id)
                params
                opts)))

(defn list-configurations
  "Lists all terminal configurations.
   \nStripe API docs: https://stripe.com/docs/api/terminal/configuration/list"
  ([stripe-client]
   (list-configurations stripe-client {} {}))
  ([stripe-client params]
   (list-configurations stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-terminal-configurations-endpoint params opts)))

(defn delete-configuration
  "Deletes a terminal configuration.
   \nStripe API docs: https://stripe.com/docs/api/terminal/configuration/delete"
  ([stripe-client configuration-id]
   (delete-configuration stripe-client configuration-id {}))
  ([stripe-client configuration-id opts]
   (request stripe-client :delete
                (str stripe-terminal-configurations-endpoint "/" configuration-id)
                nil
                opts))) 