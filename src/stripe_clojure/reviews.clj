(ns stripe-clojure.reviews
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-reviews-endpoint (config/stripe-endpoints :reviews))

(defn retrieve-review
  "Retrieves a Review object.
   \nStripe API docs: https://stripe.com/docs/api/radar/reviews/retrieve"
  ([stripe-client review-id]
   (retrieve-review stripe-client review-id {}))
  ([stripe-client review-id opts]
   (request stripe-client :get (str stripe-reviews-endpoint "/" review-id) {} opts)))

(defn list-reviews
  "Lists all reviews.
   \nStripe API docs: https://stripe.com/docs/api/radar/reviews/list"
  ([stripe-client]
   (list-reviews stripe-client {}))
  ([stripe-client params]
   (list-reviews stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-reviews-endpoint params opts)))

(defn approve-review
  "Approves a review.
   \nStripe API docs: https://stripe.com/docs/api/radar/reviews/approve"
  ([stripe-client review-id]
   (approve-review stripe-client review-id {}))
  ([stripe-client review-id opts]
   (request stripe-client :post (str stripe-reviews-endpoint "/" review-id "/approve") {} opts)))