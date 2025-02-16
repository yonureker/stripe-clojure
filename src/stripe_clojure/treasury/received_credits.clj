(ns stripe-clojure.treasury.received-credits
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-treasury-received-credits-endpoint (config/stripe-endpoints :treasury-received-credits))

(defn retrieve-received-credit
  "Retrieves a treasury received credit.
   \nStripe API docs: https://stripe.com/docs/api/treasury/received_credits/retrieve"
  ([stripe-client received-credit-id]
   (retrieve-received-credit stripe-client received-credit-id {}))
  ([stripe-client received-credit-id opts]
   (request stripe-client :get
                (str stripe-treasury-received-credits-endpoint "/" received-credit-id)
                {}
                opts)))

(defn list-received-credits
  "Lists all treasury received credits.
   \nStripe API docs: https://stripe.com/docs/api/treasury/received_credits/list"
  ([stripe-client]
   (list-received-credits stripe-client {} {}))
  ([stripe-client params]
   (list-received-credits stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-treasury-received-credits-endpoint params opts))) 