(ns stripe-clojure.financial-connections.accounts
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-financial-connections-accounts-endpoint (config/stripe-endpoints :financial-connections-accounts))

(defn retrieve-account
  "Retrieves a financial connections account.
   \nStripe API docs: https://stripe.com/docs/api/financial_connections/accounts/retrieve"
  ([stripe-client account-id]
   (retrieve-account stripe-client account-id {}))
  ([stripe-client account-id opts]
   (request stripe-client :get
                (str stripe-financial-connections-accounts-endpoint "/" account-id)
                {}
                opts)))

(defn list-accounts
  "Lists all financial connections accounts.
   \nStripe API docs: https://stripe.com/docs/api/financial_connections/accounts/list"
  ([stripe-client]
   (list-accounts stripe-client {} {}))
  ([stripe-client params]
   (list-accounts stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-financial-connections-accounts-endpoint params opts)))

(defn disconnect-account
  "Disconnects a financial connections account.
   \nStripe API docs: https://stripe.com/docs/api/financial_connections/accounts/disconnect"
  ([stripe-client account-id]
   (disconnect-account stripe-client account-id {}))
  ([stripe-client account-id opts]
   (request stripe-client :post
                (str stripe-financial-connections-accounts-endpoint "/" account-id "/disconnect")
                {}
                opts)))

(defn list-owners
  "Lists all owners of a financial connections account.
   \nStripe API docs: https://stripe.com/docs/api/financial_connections/accounts/list_owners"
  ([stripe-client account-id params]
   (list-owners stripe-client account-id params {}))
  ([stripe-client account-id params opts]
   (request stripe-client :get
                (str stripe-financial-connections-accounts-endpoint "/" account-id "/owners")
                params
                opts)))

(defn refresh-account
  "Refreshes a financial connections account.
   \nStripe API docs: https://stripe.com/docs/api/financial_connections/accounts/refresh"
  ([stripe-client account-id params]
   (refresh-account stripe-client account-id params {}))
  ([stripe-client account-id params opts]
   (request stripe-client :post
                (str stripe-financial-connections-accounts-endpoint "/" account-id "/refresh")
                params
                opts)))

(defn subscribe-account
  "Subscribes to a financial connections account.
   \nStripe API docs: https://stripe.com/docs/api/financial_connections/accounts/subscribe"
  ([stripe-client account-id params]
   (subscribe-account stripe-client account-id params {}))
  ([stripe-client account-id params opts]
   (request stripe-client :post
                (str stripe-financial-connections-accounts-endpoint "/" account-id "/subscribe")
                params
                opts)))

(defn unsubscribe-account
  "Unsubscribes from a financial connections account.
   \nStripe API docs: https://stripe.com/docs/api/financial_connections/accounts/unsubscribe"
  ([stripe-client account-id params]
   (unsubscribe-account stripe-client account-id params {}))
  ([stripe-client account-id params opts]
   (request stripe-client :post
                (str stripe-financial-connections-accounts-endpoint "/" account-id "/unsubscribe")
                params
                opts))) 