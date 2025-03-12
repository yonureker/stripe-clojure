(ns stripe-clojure.schemas.common)

(def NonNegativeInt [:and int? [:>= 0]])

(def SharedOptions
  [:map {:closed true}
   [:api-version {:optional true} string?]
   [:stripe-account {:optional true} string?]
   [:max-network-retries {:optional true} NonNegativeInt]
   [:timeout {:optional true} NonNegativeInt]
   [:full-response? {:optional true} boolean?]])