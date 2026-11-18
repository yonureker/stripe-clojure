(ns stripe-clojure.test-helpers.issuing.cards
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-test-helpers-issuing-cards-endpoint (:test-helpers-issuing-cards config/stripe-endpoints))

(defn deliver-card
  "Updates the shipping status of a card to delivered.
   \nStripe API docs: https://stripe.com/docs/api/issuing/cards/test_mode_deliver"
  ([stripe-client card-id]
   (deliver-card stripe-client card-id {}))
  ([stripe-client card-id opts]
   (request stripe-client :post
            (str stripe-test-helpers-issuing-cards-endpoint "/" card-id "/shipping/deliver")
            {}
            opts)))

(defn fail-card
  "Updates the shipping status of a card to failure.
   \nStripe API docs: https://stripe.com/docs/api/issuing/cards/test_mode_fail"
  ([stripe-client card-id]
   (fail-card stripe-client card-id {}))
  ([stripe-client card-id opts]
   (request stripe-client :post
            (str stripe-test-helpers-issuing-cards-endpoint "/" card-id "/shipping/fail")
            {}
            opts)))

(defn return-card
  "Updates the shipping status of a card to returned.
   \nStripe API docs: https://stripe.com/docs/api/issuing/cards/test_mode_return"
  ([stripe-client card-id]
   (return-card stripe-client card-id {}))
  ([stripe-client card-id opts]
   (request stripe-client :post
            (str stripe-test-helpers-issuing-cards-endpoint "/" card-id "/shipping/return")
            {}
            opts)))

(defn ship-card
  "Updates the shipping status of a card to shipped.
   \nStripe API docs: https://stripe.com/docs/api/issuing/cards/test_mode_ship"
  ([stripe-client card-id]
   (ship-card stripe-client card-id {}))
  ([stripe-client card-id opts]
   (request stripe-client :post
            (str stripe-test-helpers-issuing-cards-endpoint "/" card-id "/shipping/ship")
            {}
            opts)))

(defn submit-card
  "Updates the shipping status of a card to submitted.
   \nStripe API docs: https://stripe.com/docs/api/issuing/cards/test_mode_submit"
  ([stripe-client card-id]
   (submit-card stripe-client card-id {}))
  ([stripe-client card-id opts]
   (request stripe-client :post
            (str stripe-test-helpers-issuing-cards-endpoint "/" card-id "/shipping/submit")
            {}
            opts)))
