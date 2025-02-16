(ns stripe-clojure.payment-method-configurations
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-payment-method-configurations-endpoint (:payment-method-configurations config/stripe-endpoints))

(defn create-payment-method-configuration
  "Creates a payment method configuration.
   \nStripe API docs: https://stripe.com/docs/api/payment_method_configurations/create"
  ([stripe-client params]
   (create-payment-method-configuration stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-payment-method-configurations-endpoint params opts)))

(defn retrieve-payment-method-configuration
  "Retrieves a payment method configuration.
   \nStripe API docs: https://stripe.com/docs/api/payment_method_configurations/retrieve"
  ([stripe-client configuration-id]
   (retrieve-payment-method-configuration stripe-client configuration-id {}))
  ([stripe-client configuration-id opts]
   (request stripe-client :get (str stripe-payment-method-configurations-endpoint "/" configuration-id) {} opts)))

(defn update-payment-method-configuration
  "Updates a payment method configuration.
   \nStripe API docs: https://stripe.com/docs/api/payment_method_configurations/update"
  ([stripe-client configuration-id params]
   (update-payment-method-configuration stripe-client configuration-id params {}))
  ([stripe-client configuration-id params opts]
   (request stripe-client :post (str stripe-payment-method-configurations-endpoint "/" configuration-id) params opts)))

(defn list-payment-method-configurations
  "Lists all payment method configurations.
   \nStripe API docs: https://stripe.com/docs/api/payment_method_configurations/list"
  ([stripe-client]
   (list-payment-method-configurations stripe-client {}))
  ([stripe-client params]
   (list-payment-method-configurations stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-payment-method-configurations-endpoint params opts)))