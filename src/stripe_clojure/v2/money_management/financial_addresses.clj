(ns stripe-clojure.v2.money-management.financial-addresses
  "V2 Money Management Financial Addresses API.

   A financial address contains the information needed to transfer money
   to a financial account. A financial account can have more than one
   financial address.

   Docs: https://docs.stripe.com/api/v2/money-management/financial-addresses"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-money-management-financial-addresses config/stripe-v2-endpoints))

(defn create-financial-address
  "Creates a financial address for a financial account.

   Docs: https://docs.stripe.com/api/v2/money-management/financial-addresses/create"
  ([stripe-client params]
   (create-financial-address stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))

(defn retrieve-financial-address
  "Retrieves a financial address by ID.

   Docs: https://docs.stripe.com/api/v2/money-management/financial-addresses/retrieve"
  ([stripe-client financial-address-id]
   (retrieve-financial-address stripe-client financial-address-id {}))
  ([stripe-client financial-address-id opts]
   (request stripe-client :get (str endpoint "/" financial-address-id) {} opts)))

(defn list-financial-addresses
  "Lists all financial addresses.

   Docs: https://docs.stripe.com/api/v2/money-management/financial-addresses/list"
  ([stripe-client]
   (list-financial-addresses stripe-client {}))
  ([stripe-client params]
   (list-financial-addresses stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))
