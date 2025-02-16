(ns stripe-clojure.treasury.received-debits
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-treasury-received-debits-endpoint (config/stripe-endpoints :treasury-received-debits))

(defn retrieve-received-debit
  "Retrieves a treasury received debit.
   \nStripe API docs: https://stripe.com/docs/api/treasury/received_debits/retrieve"
  ([stripe-client received-debit-id]
   (retrieve-received-debit stripe-client received-debit-id {}))
  ([stripe-client received-debit-id opts]
   (request stripe-client :get
                (str stripe-treasury-received-debits-endpoint "/" received-debit-id)
                {}
                opts)))

(defn list-received-debits
  "Lists all treasury received debits.
   \nStripe API docs: https://stripe.com/docs/api/treasury/received_debits/list"
  ([stripe-client params]
   (list-received-debits stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-treasury-received-debits-endpoint params opts))) 