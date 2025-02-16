(ns stripe-clojure.treasury.financial-accounts
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-treasury-financial-accounts-endpoint (config/stripe-endpoints :treasury-financial-accounts))

(defn create-financial-account
  "Creates a new treasury financial account.
   \nStripe API docs: https://stripe.com/docs/api/treasury/financial_accounts/create"
  ([stripe-client params]
   (create-financial-account stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-treasury-financial-accounts-endpoint params opts)))

(defn retrieve-financial-account
  "Retrieves a treasury financial account.
   \nStripe API docs: https://stripe.com/docs/api/treasury/financial_accounts/retrieve"
  ([stripe-client financial-account-id]
   (retrieve-financial-account stripe-client financial-account-id {}))
  ([stripe-client financial-account-id opts]
   (request stripe-client :get
                (str stripe-treasury-financial-accounts-endpoint "/" financial-account-id)
                {}
                opts)))

(defn update-financial-account
  "Updates a treasury financial account.
   \nStripe API docs: https://stripe.com/docs/api/treasury/financial_accounts/update"
  ([stripe-client financial-account-id params]
   (update-financial-account stripe-client financial-account-id params {}))
  ([stripe-client financial-account-id params opts]
   (request stripe-client :post
            (str stripe-treasury-financial-accounts-endpoint "/" financial-account-id)
            params
            opts)))

(defn list-financial-accounts
  "Lists all treasury financial accounts.
   \nStripe API docs: https://stripe.com/docs/api/treasury/financial_accounts/list"
  ([stripe-client]
   (list-financial-accounts stripe-client {} {}))
  ([stripe-client params]
   (list-financial-accounts stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-treasury-financial-accounts-endpoint params opts)))

(defn retrieve-features
  "Retrieves features of a treasury financial account.
   \nStripe API docs: https://stripe.com/docs/api/treasury/financial_account_features/retrieve"
  ([stripe-client financial-account-id]
   (retrieve-features stripe-client financial-account-id {}))
  ([stripe-client financial-account-id opts]
   (request stripe-client :get
                (str stripe-treasury-financial-accounts-endpoint "/" financial-account-id "/features")
                {}
                opts)))

(defn update-features
  "Updates features of a treasury financial account.
   \nStripe API docs: https://stripe.com/docs/api/treasury/financial_account_features/update"
  ([stripe-client financial-account-id params]
   (update-features stripe-client financial-account-id params {}))
  ([stripe-client financial-account-id params opts]
   (request stripe-client :post
                (str stripe-treasury-financial-accounts-endpoint "/" financial-account-id "/features")
                params
                opts))) 