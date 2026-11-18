(ns stripe-clojure.test-helpers.treasury.received-debits
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-test-helpers-treasury-received-debits-endpoint (:test-helpers-treasury-received-debits config/stripe-endpoints))

(defn create-received-debit
  "Simulates a test mode received debit initiated by a third party.
   \nStripe API docs: https://stripe.com/docs/api/treasury/received_debits/test_mode_create"
  ([stripe-client params]
   (create-received-debit stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-test-helpers-treasury-received-debits-endpoint params opts)))
