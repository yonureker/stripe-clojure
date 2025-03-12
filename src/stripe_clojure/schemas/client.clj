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

;; Connection pool schemas

(def ConnectionPoolOptions
  [:map {:closed true}
   [:timeout {:optional true} NonNegativeInt]
   [:threads {:optional true} [:and int? [:>= 1]]]
   [:default-per-route {:optional true} [:and int? [:>= 1]]]
   [:insecure? {:optional true} boolean?]])

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
    [:use-connection-pool? {:optional true} boolean?]
    [:pool-options {:optional true} ConnectionPoolOptions]
    [:kebabify-keys? {:optional true} boolean?]]))
