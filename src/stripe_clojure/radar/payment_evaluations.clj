(ns stripe-clojure.radar.payment-evaluations
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-radar-payment-evaluations-endpoint (config/stripe-endpoints :radar-payment-evaluations))

(defn create-payment-evaluation
  "Creates a payment evaluation.
   \nStripe API docs: https://stripe.com/docs/api/radar/payment_evaluations/create"
  ([stripe-client params]
   (create-payment-evaluation stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-radar-payment-evaluations-endpoint params opts)))
