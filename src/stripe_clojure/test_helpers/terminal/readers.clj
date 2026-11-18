(ns stripe-clojure.test-helpers.terminal.readers
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-test-helpers-terminal-readers-endpoint (:test-helpers-terminal-readers config/stripe-endpoints))

(defn present-payment-method
  "Presents a payment method on a simulated reader.
   \nStripe API docs: https://stripe.com/docs/api/terminal/readers/present_payment_method"
  ([stripe-client reader-id]
   (present-payment-method stripe-client reader-id {} {}))
  ([stripe-client reader-id params]
   (present-payment-method stripe-client reader-id params {}))
  ([stripe-client reader-id params opts]
   (request stripe-client :post
            (str stripe-test-helpers-terminal-readers-endpoint "/" reader-id "/present_payment_method")
            params
            opts)))

(defn succeed-input-collection
  "Triggers a successful input collection on a simulated reader.
   \nStripe API docs: https://stripe.com/docs/api/terminal/readers/succeed_input_collection"
  ([stripe-client reader-id]
   (succeed-input-collection stripe-client reader-id {} {}))
  ([stripe-client reader-id params]
   (succeed-input-collection stripe-client reader-id params {}))
  ([stripe-client reader-id params opts]
   (request stripe-client :post
            (str stripe-test-helpers-terminal-readers-endpoint "/" reader-id "/succeed_input_collection")
            params
            opts)))

(defn timeout-input-collection
  "Completes an input collection with a timeout error on a simulated reader.
   \nStripe API docs: https://stripe.com/docs/api/terminal/readers/timeout_input_collection"
  ([stripe-client reader-id]
   (timeout-input-collection stripe-client reader-id {}))
  ([stripe-client reader-id opts]
   (request stripe-client :post
            (str stripe-test-helpers-terminal-readers-endpoint "/" reader-id "/timeout_input_collection")
            {}
            opts)))
