(ns stripe-clojure.tax.associations
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-tax-associations-endpoint (config/stripe-endpoints :tax-associations))

(defn find-association
  "Finds a tax association object by PaymentIntent id.
   \nStripe API docs: https://docs.stripe.com/api/tax/associations/find"
  ([stripe-client params]
   (find-association stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get
            (str stripe-tax-associations-endpoint "/find")
            params
            opts)))
