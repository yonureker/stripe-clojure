(ns stripe-clojure.payment-method-domains
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-payment-method-domains-endpoint (:payment-method-domains config/stripe-endpoints))

(defn create-payment-method-domain
  "Creates a payment method domain.
   \nStripe API docs: https://stripe.com/docs/api/payment_method_domains/create"
  ([stripe-client params]
   (create-payment-method-domain stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-payment-method-domains-endpoint params opts)))

(defn retrieve-payment-method-domain
  "Retrieves a payment method domain.
   \nStripe API docs: https://stripe.com/docs/api/payment_method_domains/retrieve"
  ([stripe-client domain-id]
   (retrieve-payment-method-domain stripe-client domain-id {}))
  ([stripe-client domain-id opts]
   (request stripe-client :get (str stripe-payment-method-domains-endpoint "/" domain-id) {} opts)))

(defn update-payment-method-domain
  "Updates a payment method domain.
   \nStripe API docs: https://stripe.com/docs/api/payment_method_domains/update"
  ([stripe-client domain-id params]
   (update-payment-method-domain stripe-client domain-id params {}))
  ([stripe-client domain-id params opts]
   (request stripe-client :post (str stripe-payment-method-domains-endpoint "/" domain-id) params opts)))

(defn list-payment-method-domains
  "Lists all payment method domains.
   \nStripe API docs: https://stripe.com/docs/api/payment_method_domains/list"
  ([stripe-client]
   (list-payment-method-domains stripe-client {}))
  ([stripe-client params]
   (list-payment-method-domains stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-payment-method-domains-endpoint params opts)))

(defn validate-payment-method-domain
  "Validates a payment method domain.
   \nStripe API docs: https://stripe.com/docs/api/payment_method_domains/validate"
  ([stripe-client domain-id]
   (validate-payment-method-domain stripe-client domain-id {}))
  ([stripe-client domain-id opts]
   (request stripe-client :post (str stripe-payment-method-domains-endpoint "/" domain-id "/validate") {} opts)))