(ns stripe-clojure.exchange-rates
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-exchange-rates-endpoint (:exchange-rates config/stripe-endpoints))

(defn retrieve-exchange-rate
  "Retrieves the exchange rates from the given currency to every supported currency.
   \nStripe API docs: https://stripe.com/docs/api/exchange_rates/retrieve"
  ([stripe-client currency]
   (retrieve-exchange-rate stripe-client currency {}))
  ([stripe-client currency opts]
   (request stripe-client :get (str stripe-exchange-rates-endpoint "/" currency) {} opts)))

(defn list-exchange-rates
  "Returns a list of objects that contain the rates at which foreign currencies are converted to one another.
   \nStripe API docs: https://stripe.com/docs/api/exchange_rates/list"
  ([stripe-client]
   (list-exchange-rates stripe-client {} {}))
  ([stripe-client params]
   (list-exchange-rates stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-exchange-rates-endpoint params opts)))
