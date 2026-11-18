(ns stripe-clojure.v2.money-management.recipient-verifications
  "V2 Money Management Recipient Verifications API.

   A recipient verification verifies the recipient you intend to send
   funds to.

   Docs: https://docs.stripe.com/api/v2/money-management/recipient-verifications"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-money-management-recipient-verifications config/stripe-v2-endpoints))

(defn create-recipient-verification
  "Creates a recipient verification to verify the recipient you intend
   to send funds to.

   Docs: https://docs.stripe.com/api/v2/money-management/recipient-verifications/create"
  ([stripe-client params]
   (create-recipient-verification stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))

(defn retrieve-recipient-verification
  "Retrieves a recipient verification by ID.

   Docs: https://docs.stripe.com/api/v2/money-management/recipient-verifications/retrieve"
  ([stripe-client recipient-verification-id]
   (retrieve-recipient-verification stripe-client recipient-verification-id {}))
  ([stripe-client recipient-verification-id opts]
   (request stripe-client :get (str endpoint "/" recipient-verification-id) {} opts)))

(defn acknowledge-recipient-verification
  "Acknowledges a recipient verification in the awaiting_acknowledgement
   state.

   Docs: https://docs.stripe.com/api/v2/money-management/recipient-verifications/acknowledge"
  ([stripe-client recipient-verification-id]
   (acknowledge-recipient-verification stripe-client recipient-verification-id {}))
  ([stripe-client recipient-verification-id opts]
   (request stripe-client :post (str endpoint "/" recipient-verification-id "/acknowledge") {} opts)))
