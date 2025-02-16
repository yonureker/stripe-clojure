(ns stripe-clojure.billing-portal.configurations
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-billing-portal-configurations-endpoint (:billing-portal-configurations config/stripe-endpoints))

(defn create-configuration
  "Creates a configuration that describes the functionality and behavior of a portal session.
   \nStripe API docs: https://stripe.com/docs/api/customer_portal/configurations/create"
  ([stripe-client params]
   (create-configuration stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-billing-portal-configurations-endpoint params opts)))

(defn retrieve-configuration
  "Retrieves a configuration that describes the functionality and behavior of a portal session.
   \nStripe API docs: https://stripe.com/docs/api/customer_portal/configurations/retrieve"
  ([stripe-client configuration-id]
   (retrieve-configuration stripe-client configuration-id {}))
  ([stripe-client configuration-id opts]
   (request stripe-client :get 
                (str stripe-billing-portal-configurations-endpoint "/" configuration-id)
                {}
                opts)))

(defn update-configuration
  "Updates a configuration that describes the functionality and behavior of a portal session.
   \nStripe API docs: https://stripe.com/docs/api/customer_portal/configurations/update"
  ([stripe-client configuration-id params]
   (update-configuration stripe-client configuration-id params {}))
  ([stripe-client configuration-id params opts]
   (request stripe-client :post
                (str stripe-billing-portal-configurations-endpoint "/" configuration-id)
                params
                opts)))

(defn list-configurations
  "Returns a list of configurations that describe the functionality of portal sessions.
   \nStripe API docs: https://stripe.com/docs/api/customer_portal/configurations/list"
  ([stripe-client]
   (list-configurations stripe-client {} {}))
  ([stripe-client params]
   (list-configurations stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-billing-portal-configurations-endpoint params opts)))