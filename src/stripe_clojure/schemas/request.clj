(ns stripe-clojure.schemas.request
  (:require [stripe-clojure.schemas.common :refer [SharedOptions]]
            [malli.util :as mu]))

(def RequestOpts
  (mu/merge
   SharedOptions
   [:map {:closed true}
    [:idempotency-key {:optional true} string?]
    [:expand {:optional true} [:or
                               string?
                               [:vector string?]]]
    [:auto-paginate? {:optional true} boolean?]
    [:test-clock {:optional true} string?]
    [:custom-headers {:optional true} [:map-of string? string?]]]))