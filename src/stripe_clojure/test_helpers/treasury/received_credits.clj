(ns stripe-clojure.test-helpers.treasury.received-credits
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-test-helpers-treasury-received-credits-endpoint (:test-helpers-treasury-received-credits config/stripe-endpoints))

(defn create-received-credit
  "Simulates a test mode received credit initiated by a third party.
   \nStripe API docs: https://stripe.com/docs/api/treasury/received_credits/test_mode_create"
  ([stripe-client params]
   (create-received-credit stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-test-helpers-treasury-received-credits-endpoint params opts)))
