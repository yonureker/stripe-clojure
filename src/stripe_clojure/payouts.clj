(ns stripe-clojure.payouts
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-payouts-endpoint (:payouts config/stripe-endpoints))

(defn create-payout
  "Creates a new payout to your bank account or debit card.
   \nStripe API docs: https://stripe.com/docs/api/payouts/create"
  ([stripe-client params]
   (create-payout stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-payouts-endpoint params opts)))

(defn retrieve-payout
  "Retrieves the details of an existing payout.
   \nStripe API docs: https://stripe.com/docs/api/payouts/retrieve"
  ([stripe-client payout-id]
   (retrieve-payout stripe-client payout-id {}))
  ([stripe-client payout-id opts]
   (request stripe-client :get (str stripe-payouts-endpoint "/" payout-id) {} opts)))

(defn update-payout
  "Updates the specified payout by setting the values of the parameters passed.
   \nStripe API docs: https://stripe.com/docs/api/payouts/update"
  ([stripe-client payout-id params]
   (update-payout stripe-client payout-id params {}))
  ([stripe-client payout-id params opts]
   (request stripe-client :post (str stripe-payouts-endpoint "/" payout-id) params opts)))

(defn list-payouts
  "Returns a list of existing payouts sent to third-party bank accounts.
   \nStripe API docs: https://stripe.com/docs/api/payouts/list"
  ([stripe-client]
   (list-payouts stripe-client {}))
  ([stripe-client params]
   (list-payouts stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-payouts-endpoint params opts)))

(defn cancel-payout
  "Cancels a pending payout. Funds will be refunded to your available balance.
   \nStripe API docs: https://stripe.com/docs/api/payouts/cancel"
  ([stripe-client payout-id]
   (cancel-payout stripe-client payout-id {}))
  ([stripe-client payout-id opts]
   (request stripe-client :post (str stripe-payouts-endpoint "/" payout-id "/cancel") {} opts)))

(defn reverse-payout
  "Reverses a payout that has previously been sent to your bank account.
   \nStripe API docs: https://stripe.com/docs/api/payouts/reverse"
  ([stripe-client payout-id]
   (reverse-payout stripe-client payout-id {}))
  ([stripe-client payout-id opts]
   (request stripe-client :post (str stripe-payouts-endpoint "/" payout-id "/reverse") {} opts)))
