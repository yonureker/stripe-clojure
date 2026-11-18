(ns stripe-clojure.v2.money-management.payout-methods
  "V2 Money Management Payout Methods API.

   Payout methods are the destinations, such as bank accounts, that
   outbound payments and transfers can be sent to.

   Docs: https://docs.stripe.com/api/v2/money-management/payout-methods"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-money-management-payout-methods config/stripe-v2-endpoints))

(defn retrieve-payout-method
  "Retrieves a payout method by ID.

   Docs: https://docs.stripe.com/api/v2/money-management/payout-methods/retrieve"
  ([stripe-client payout-method-id]
   (retrieve-payout-method stripe-client payout-method-id {}))
  ([stripe-client payout-method-id opts]
   (request stripe-client :get (str endpoint "/" payout-method-id) {} opts)))

(defn list-payout-methods
  "Lists all payout methods.

   Docs: https://docs.stripe.com/api/v2/money-management/payout-methods/list"
  ([stripe-client]
   (list-payout-methods stripe-client {}))
  ([stripe-client params]
   (list-payout-methods stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))

(defn archive-payout-method
  "Archives a payout method. Archiving prevents the payout method from
   being used for outbound payments or transfers and omits it from
   normal list results.

   Docs: https://docs.stripe.com/api/v2/money-management/payout-methods/archive"
  ([stripe-client payout-method-id]
   (archive-payout-method stripe-client payout-method-id {}))
  ([stripe-client payout-method-id opts]
   (request stripe-client :post (str endpoint "/" payout-method-id "/archive") {} opts)))

(defn unarchive-payout-method
  "Unarchives a payout method, restoring it to normal list results.

   Docs: https://docs.stripe.com/api/v2/money-management/payout-methods/unarchive"
  ([stripe-client payout-method-id]
   (unarchive-payout-method stripe-client payout-method-id {}))
  ([stripe-client payout-method-id opts]
   (request stripe-client :post (str endpoint "/" payout-method-id "/unarchive") {} opts)))
