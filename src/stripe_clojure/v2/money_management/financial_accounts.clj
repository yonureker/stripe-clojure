(ns stripe-clojure.v2.money-management.financial-accounts
  "V2 Money Management Financial Accounts API.

   A financial account represents a balance and can be the source or
   destination of money movement.

   Docs: https://docs.stripe.com/api/v2/money-management/financial-accounts"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-money-management-financial-accounts config/stripe-v2-endpoints))

(defn retrieve-financial-account
  "Retrieves a financial account by ID.

   Docs: https://docs.stripe.com/api/v2/money-management/financial-accounts/retrieve"
  ([stripe-client financial-account-id]
   (retrieve-financial-account stripe-client financial-account-id {}))
  ([stripe-client financial-account-id opts]
   (request stripe-client :get (str endpoint "/" financial-account-id) {} opts)))

(defn update-financial-account
  "Updates a financial account.

   Docs: https://docs.stripe.com/api/v2/money-management/financial-accounts/update"
  ([stripe-client financial-account-id params]
   (update-financial-account stripe-client financial-account-id params {}))
  ([stripe-client financial-account-id params opts]
   (request stripe-client :post (str endpoint "/" financial-account-id) params opts)))

(defn list-financial-accounts
  "Lists all financial accounts.

   Docs: https://docs.stripe.com/api/v2/money-management/financial-accounts/list"
  ([stripe-client]
   (list-financial-accounts stripe-client {}))
  ([stripe-client params]
   (list-financial-accounts stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))
