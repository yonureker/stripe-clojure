(ns stripe-clojure.coupons
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-coupons-endpoint (:coupons config/stripe-endpoints))

(defn create-coupon
  "Creates a new coupon that can be applied to subscriptions or invoices.
   \nStripe API docs: https://stripe.com/docs/api/coupons/create"
  ([stripe-client params]
   (create-coupon stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-coupons-endpoint params opts)))

(defn retrieve-coupon
  "Retrieves the coupon with the given ID.
   \nStripe API docs: https://stripe.com/docs/api/coupons/retrieve"
  ([stripe-client coupon-id]
   (retrieve-coupon stripe-client coupon-id {}))
  ([stripe-client coupon-id opts]
   (request stripe-client :get (str stripe-coupons-endpoint "/" coupon-id) {} opts)))

(defn update-coupon
  "Updates the metadata of a coupon. Other coupon details (currency, duration, amount_off) are, by design, not editable.
   \nStripe API docs: https://stripe.com/docs/api/coupons/update"
  ([stripe-client coupon-id params]
   (update-coupon stripe-client coupon-id params {}))
  ([stripe-client coupon-id params opts]
   (request stripe-client :post (str stripe-coupons-endpoint "/" coupon-id) params opts)))

(defn delete-coupon
  "Deletes the coupon with the given ID.
   \nStripe API docs: https://stripe.com/docs/api/coupons/delete"
  ([stripe-client coupon-id]
   (delete-coupon stripe-client coupon-id {}))
  ([stripe-client coupon-id opts]
   (request stripe-client :delete (str stripe-coupons-endpoint "/" coupon-id) nil opts)))

(defn list-coupons
  "Returns a list of your coupons.
   \nStripe API docs: https://stripe.com/docs/api/coupons/list"
  ([stripe-client]
   (list-coupons stripe-client {}))
  ([stripe-client params]
   (list-coupons stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-coupons-endpoint params opts)))