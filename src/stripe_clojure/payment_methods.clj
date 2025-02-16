(ns stripe-clojure.payment-methods
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-payment-methods-endpoint (:payment-methods config/stripe-endpoints))

(defn create-payment-method
  "Creates a PaymentMethod object that can be used for payments.
   \nStripe API docs: https://stripe.com/docs/api/payment_methods/create"
  ([stripe-client params]
   (create-payment-method stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-payment-methods-endpoint params opts)))

(defn retrieve-payment-method
  "Retrieves a PaymentMethod object.
   \nStripe API docs: https://stripe.com/docs/api/payment_methods/retrieve"
  ([stripe-client payment-method-id]
   (retrieve-payment-method stripe-client payment-method-id {}))
  ([stripe-client payment-method-id opts]
   (request stripe-client :get (str stripe-payment-methods-endpoint "/" payment-method-id) {} opts)))

(defn update-payment-method
  "Updates a PaymentMethod object.
   \nStripe API docs: https://stripe.com/docs/api/payment_methods/update"
  ([stripe-client payment-method-id params]
   (update-payment-method stripe-client payment-method-id params {}))
  ([stripe-client payment-method-id params opts]
   (request stripe-client :post (str stripe-payment-methods-endpoint "/" payment-method-id) params opts)))

(defn list-payment-methods
  "Lists the PaymentMethods attached to the customer.
   \nStripe API docs: https://stripe.com/docs/api/payment_methods/list"
  ([stripe-client]
   (list-payment-methods stripe-client {}))
  ([stripe-client params]
   (list-payment-methods stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-payment-methods-endpoint params opts)))

(defn attach-payment-method
  "Attaches a PaymentMethod to a Customer.
   \nStripe API docs: https://stripe.com/docs/api/payment_methods/attach"
  ([stripe-client payment-method-id params]
   (attach-payment-method stripe-client payment-method-id params {}))
  ([stripe-client payment-method-id params opts]
   (request stripe-client :post (str stripe-payment-methods-endpoint "/" payment-method-id "/attach") params opts)))

(defn detach-payment-method
  "Detaches a PaymentMethod from a Customer.
   \nStripe API docs: https://stripe.com/docs/api/payment_methods/detach"
  ([stripe-client payment-method-id]
   (detach-payment-method stripe-client payment-method-id {}))
  ([stripe-client payment-method-id opts]
   (request stripe-client :post (str stripe-payment-methods-endpoint "/" payment-method-id "/detach") {} opts)))
