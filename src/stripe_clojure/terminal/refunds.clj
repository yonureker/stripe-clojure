(ns stripe-clojure.terminal.refunds
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-terminal-refunds-endpoint (config/stripe-endpoints :terminal-refunds))

(defn create-refund
  "Creates a terminal refund.
   \nStripe API docs: https://stripe.com/docs/api/terminal/refunds/create"
  ([stripe-client params]
   (create-refund stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-terminal-refunds-endpoint params opts)))
