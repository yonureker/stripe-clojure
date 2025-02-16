(ns stripe-clojure.forwarding.requests
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-forwarding-requests-endpoint (config/stripe-endpoints :forwarding-requests))

(defn create-request
  "Creates a new forwarding request.
   \nStripe API docs: https://stripe.com/docs/api/forwarding/forwarding_requests/create"
  ([stripe-client params]
   (create-request stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-forwarding-requests-endpoint params opts)))

(defn retrieve-request
  "Retrieves a forwarding request.
   \nStripe API docs: https://stripe.com/docs/api/forwarding/forwarding_requests/retrieve"
  ([stripe-client request-id]
   (retrieve-request stripe-client request-id {}))
  ([stripe-client request-id opts]
   (request stripe-client :get
                (str stripe-forwarding-requests-endpoint "/" request-id)
                {}
                opts)))

(defn list-requests
  "Lists all forwarding requests.
   \nStripe API docs: https://stripe.com/docs/api/forwarding/forwarding_requests/list"
  ([stripe-client]
   (list-requests stripe-client {} {}))
  ([stripe-client params]
   (list-requests stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-forwarding-requests-endpoint params opts))) 