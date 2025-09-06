(ns stripe-clojure.apple-pay.domains
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-apple-pay-domains-endpoint (:apple-pay-domains config/stripe-endpoints))

(defn create-apple-pay-domain
  "Create an apple pay domain.
   \nStripe API docs: https://stripe.com/docs/api/apple_pay/domains/create"
  ([stripe-client params]
   (create-apple-pay-domain stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-apple-pay-domains-endpoint params opts)))

(defn retrieve-apple-pay-domain
  "Retrieve an apple pay domain.
   \nStripe API docs: https://stripe.com/docs/api/apple_pay/domains/retrieve"
  ([stripe-client domain-id]
   (retrieve-apple-pay-domain stripe-client domain-id {}))
  ([stripe-client domain-id opts]
   (request stripe-client :get (str stripe-apple-pay-domains-endpoint "/" domain-id) {} opts)))

(defn delete-apple-pay-domain
  "Delete an apple pay domain.
   \nStripe API docs: https://stripe.com/docs/api/apple_pay/domains/delete"
  ([stripe-client domain-id]
   (delete-apple-pay-domain stripe-client domain-id {}))
  ([stripe-client domain-id opts]
   (request stripe-client :delete (str stripe-apple-pay-domains-endpoint "/" domain-id) nil opts)))

(defn list-apple-pay-domains
  "List apple pay domains.
   \nStripe API docs: https://stripe.com/docs/api/apple_pay/domains/list"
  ([stripe-client]
   (list-apple-pay-domains stripe-client {} {}))
  ([stripe-client params]
   (list-apple-pay-domains stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-apple-pay-domains-endpoint params opts)))
