(ns stripe-clojure.application-fees
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-application-fees-endpoint (config/stripe-endpoints :application-fees))

(defn retrieve-application-fee
  "Retrieves the details of an application fee that your account has collected.
   \nStripe API docs: https://stripe.com/docs/api/application_fees/retrieve"
  ([stripe-client fee-id]
   (retrieve-application-fee stripe-client fee-id {}))
  ([stripe-client fee-id opts]
   (request stripe-client :get
                (str stripe-application-fees-endpoint "/" fee-id)
                {}
                opts)))

(defn list-application-fees
  "Returns a list of application fees you've previously collected.
   \nStripe API docs: https://stripe.com/docs/api/application_fees/list"
  ([stripe-client]
   (list-application-fees stripe-client {}))
  ([stripe-client params]
   (list-application-fees stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-application-fees-endpoint params opts)))

;; Application Fee Refunds

(defn create-refund
  "Refunds an application fee that has previously been collected.
   \nStripe API docs: https://stripe.com/docs/api/fee_refunds/create"
  ([stripe-client fee-id params]
   (create-refund stripe-client fee-id params {}))
  ([stripe-client fee-id params opts]
   (request stripe-client :post
                (str stripe-application-fees-endpoint "/" fee-id "/refunds")
                params
                opts)))

(defn retrieve-refund
  "Retrieves the details of an application fee refund.
   \nStripe API docs: https://stripe.com/docs/api/fee_refunds/retrieve"
  ([stripe-client fee-id refund-id]
   (retrieve-refund stripe-client fee-id refund-id {}))
  ([stripe-client fee-id refund-id opts]
   (request stripe-client :get
                (str stripe-application-fees-endpoint "/" fee-id "/refunds/" refund-id)
                {}
                opts)))

(defn update-refund
  "Updates the specified application fee refund.
   \nStripe API docs: https://stripe.com/docs/api/fee_refunds/update"
  ([stripe-client fee-id refund-id params]
   (update-refund stripe-client fee-id refund-id params {}))
  ([stripe-client fee-id refund-id params opts]
   (request stripe-client :post
                (str stripe-application-fees-endpoint "/" fee-id "/refunds/" refund-id)
                params
                opts)))

(defn list-refunds
  "Returns a list of all refunds for a specific application fee.
   \nStripe API docs: https://stripe.com/docs/api/fee_refunds/list"
  ([stripe-client fee-id]
   (list-refunds stripe-client fee-id {}))
  ([stripe-client fee-id params]
   (list-refunds stripe-client fee-id params {}))
  ([stripe-client fee-id params opts]
   (request stripe-client :get
                (str stripe-application-fees-endpoint "/" fee-id "/refunds")
                params
                opts)))