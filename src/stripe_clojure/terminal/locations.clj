(ns stripe-clojure.terminal.locations
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-terminal-locations-endpoint (config/stripe-endpoints :terminal-locations))

(defn create-location
  "Creates a new terminal location.
   \nStripe API docs: https://stripe.com/docs/api/terminal/locations/create"
  ([stripe-client params]
   (create-location stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-terminal-locations-endpoint params opts)))

(defn retrieve-location
  "Retrieves a terminal location.
   \nStripe API docs: https://stripe.com/docs/api/terminal/locations/retrieve"
  ([stripe-client location-id]
   (retrieve-location stripe-client location-id {}))
  ([stripe-client location-id opts]
   (request stripe-client :get
                (str stripe-terminal-locations-endpoint "/" location-id)
                {}
                opts)))

(defn update-location
  "Updates a terminal location.
   \nStripe API docs: https://stripe.com/docs/api/terminal/locations/update"
  ([stripe-client location-id params]
   (update-location stripe-client location-id params {}))
  ([stripe-client location-id params opts]
   (request stripe-client :post
                (str stripe-terminal-locations-endpoint "/" location-id)
                params
                opts)))

(defn list-locations
  "Lists all terminal locations.
   \nStripe API docs: https://stripe.com/docs/api/terminal/locations/list"
  ([stripe-client]
   (list-locations stripe-client {} {}))
  ([stripe-client params]
   (list-locations stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-terminal-locations-endpoint params opts)))

(defn delete-location
  "Deletes a terminal location.
   \nStripe API docs: https://stripe.com/docs/api/terminal/locations/delete"
  ([stripe-client location-id]
   (delete-location stripe-client location-id {}))
  ([stripe-client location-id opts]
   (request stripe-client :delete
                (str stripe-terminal-locations-endpoint "/" location-id)
                nil
                opts))) 