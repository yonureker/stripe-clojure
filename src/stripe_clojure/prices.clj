(ns stripe-clojure.prices
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-prices-endpoint (:prices config/stripe-endpoints))

(defn create-price
  "Creates a new price for an existing product.
   \nStripe API docs: https://stripe.com/docs/api/prices/create"
  ([stripe-client params]
   (create-price stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-prices-endpoint params opts)))

(defn retrieve-price
  "Retrieves the price with the given ID.
   \nStripe API docs: https://stripe.com/docs/api/prices/retrieve"
  ([stripe-client price-id]
   (retrieve-price stripe-client price-id {}))
  ([stripe-client price-id opts]
   (request stripe-client :get (str stripe-prices-endpoint "/" price-id) {} opts)))

(defn update-price
  "Updates the specified price by setting the values of the parameters passed.
   \nStripe API docs: https://stripe.com/docs/api/prices/update"
  ([stripe-client price-id params]
   (update-price stripe-client price-id params {}))
  ([stripe-client price-id params opts]
   (request stripe-client :post (str stripe-prices-endpoint "/" price-id) params opts)))

(defn list-prices
  "Lists all prices.
   \nStripe API docs: https://stripe.com/docs/api/prices/list"
  ([stripe-client]
   (list-prices stripe-client {}))
  ([stripe-client params]
   (list-prices stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-prices-endpoint params opts)))

(defn search-prices
  "Searches for prices that match the given criteria.
   \nStripe API docs: https://stripe.com/docs/api/prices/search"
  ([stripe-client params]
   (search-prices stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get
                (str stripe-prices-endpoint "/search")
                params
                opts)))
