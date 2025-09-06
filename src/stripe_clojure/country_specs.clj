(ns stripe-clojure.country-specs
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-country-specs-endpoint (:country-specs config/stripe-endpoints))

(defn retrieve-country-spec
  "Retrieves the details of a country specification.
   \nStripe API docs: https://stripe.com/docs/api/country_specs/retrieve"
  ([stripe-client country-code]
   (retrieve-country-spec stripe-client country-code {}))
  ([stripe-client country-code opts]
   (request stripe-client :get (str stripe-country-specs-endpoint "/" country-code) {} opts)))

(defn list-country-specs
  "Lists all country specification objects available in the API.
   \nStripe API docs: https://stripe.com/docs/api/country_specs/list"
  ([stripe-client]
   (list-country-specs stripe-client {} {}))
  ([stripe-client params]
   (list-country-specs stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-country-specs-endpoint params opts)))