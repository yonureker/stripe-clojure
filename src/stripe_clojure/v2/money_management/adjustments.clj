(ns stripe-clojure.v2.money-management.adjustments
  "V2 Money Management Adjustments API.

   An adjustment records a credit or debit that Stripe makes to your
   financial account balance, for example to correct an error.

   Docs: https://docs.stripe.com/api/v2/money-management/adjustments"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-money-management-adjustments config/stripe-v2-endpoints))

(defn retrieve-adjustment
  "Retrieves an adjustment by ID.

   Docs: https://docs.stripe.com/api/v2/money-management/adjustments/retrieve"
  ([stripe-client adjustment-id]
   (retrieve-adjustment stripe-client adjustment-id {}))
  ([stripe-client adjustment-id opts]
   (request stripe-client :get (str endpoint "/" adjustment-id) {} opts)))

(defn list-adjustments
  "Lists all adjustments.

   Docs: https://docs.stripe.com/api/v2/money-management/adjustments/list"
  ([stripe-client]
   (list-adjustments stripe-client {}))
  ([stripe-client params]
   (list-adjustments stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))
