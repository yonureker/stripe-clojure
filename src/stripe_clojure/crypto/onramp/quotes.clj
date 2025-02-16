(ns stripe-clojure.crypto.onramp.quotes
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-crypto-onramp-quotes-endpoint (config/stripe-endpoints :crypto-onramp-quotes))

(defn list-quotes
  "Lists all crypto onramp quotes.
   \nStripe API docs: https://stripe.com/docs/api/crypto/onramp_quotes"
  ([stripe-client]
   (list-quotes stripe-client {} {}))
  ([stripe-client params]
   (list-quotes stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-crypto-onramp-quotes-endpoint params opts))) 