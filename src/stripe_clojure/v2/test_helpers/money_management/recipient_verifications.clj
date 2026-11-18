(ns stripe-clojure.v2.test-helpers.money-management.recipient-verifications
  "V2 Money Management Recipient Verifications test helpers.

   Test helpers create recipient verifications with a chosen match result
   in a sandbox environment.

   Docs: https://docs.stripe.com/api/v2/money-management/recipient-verifications"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-test-helpers-money-management-recipient-verifications config/stripe-v2-endpoints))

(defn create-recipient-verification
  "Creates a recipient verification with a specified match result for
   testing in a sandbox environment.

   Docs: https://docs.stripe.com/api/v2/money-management/recipient-verifications/create"
  ([stripe-client params]
   (create-recipient-verification stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))
