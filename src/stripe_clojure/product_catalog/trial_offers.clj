(ns stripe-clojure.product-catalog.trial-offers
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-product-catalog-trial-offers-endpoint (config/stripe-endpoints :product-catalog-trial-offers))

(defn create-trial-offer
  "Creates a trial offer.
   \nStripe API docs: https://stripe.com/docs/api/product_catalog/trial_offers/create"
  ([stripe-client params]
   (create-trial-offer stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-product-catalog-trial-offers-endpoint params opts)))

(defn retrieve-trial-offer
  "Retrieves a trial offer.
   \nStripe API docs: https://stripe.com/docs/api/product_catalog/trial_offers/retrieve"
  ([stripe-client trial-offer-id]
   (retrieve-trial-offer stripe-client trial-offer-id {}))
  ([stripe-client trial-offer-id opts]
   (request stripe-client :get
            (str stripe-product-catalog-trial-offers-endpoint "/" trial-offer-id)
            {}
            opts)))

(defn update-trial-offer
  "Updates a trial offer.
   \nStripe API docs: https://stripe.com/docs/api/product_catalog/trial_offers/update"
  ([stripe-client trial-offer-id params]
   (update-trial-offer stripe-client trial-offer-id params {}))
  ([stripe-client trial-offer-id params opts]
   (request stripe-client :post
            (str stripe-product-catalog-trial-offers-endpoint "/" trial-offer-id)
            params
            opts)))

(defn list-trial-offers
  "Lists all trial offers.
   \nStripe API docs: https://stripe.com/docs/api/product_catalog/trial_offers/list"
  ([stripe-client]
   (list-trial-offers stripe-client {} {}))
  ([stripe-client params]
   (list-trial-offers stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-product-catalog-trial-offers-endpoint params opts)))
