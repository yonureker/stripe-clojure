(ns stripe-clojure.schemas.client
  (:require [stripe-clojure.schemas.common :refer [SharedOptions NonNegativeInt]]
            [malli.util :as mu]))

;; Rate limits schemas

(def RateLimitItem
  [:map {:closed true}
   [:read {:optional true} NonNegativeInt]
   [:write {:optional true} NonNegativeInt]])

(def RateLimitCategory
  [:map {:closed true}
   [:default {:optional true} RateLimitItem]
   [:files {:optional true} RateLimitItem]
   [:search {:optional true} RateLimitItem]
   [:meter {:optional true} RateLimitItem]])

(def RateLimits
  [:map-of
   [:enum :live :test]
   RateLimitCategory])

;; Stripe client schemas

(def StripeClient
  (mu/merge
   SharedOptions
   [:map {:closed true}
    [:api-key string?]
    [:protocol {:optional true} [:enum "http" "https"]]
    [:host {:optional true} string?]
    [:port {:optional true} pos-int?]
    [:rate-limits {:optional true} RateLimits]
    [:kebabify-keys? {:optional true} boolean?]]))
