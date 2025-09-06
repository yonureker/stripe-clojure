(ns stripe-clojure.linked-accounts
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-linked-accounts-endpoint (:linked-accounts config/stripe-endpoints))

(defn retrieve-linked-account
  "Retrieves the details of an existing linked account.
   \nStripe API docs: https://stripe.com/docs/api/linked_accounts/retrieve"
  ([stripe-client account-id]
   (retrieve-linked-account stripe-client account-id {}))
  ([stripe-client account-id opts]
   (request stripe-client :get (str stripe-linked-accounts-endpoint "/" account-id) {} opts)))

(defn list-linked-accounts
  "Returns a list of linked accounts for the account.
   \nStripe API docs: https://stripe.com/docs/api/linked_accounts/list"
  ([stripe-client]
   (list-linked-accounts stripe-client {} {}))
  ([stripe-client params]
   (list-linked-accounts stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-linked-accounts-endpoint params opts)))

(defn disconnect-linked-account
  "Disables the linked account for the account.
   \nStripe API docs: https://stripe.com/docs/api/linked_accounts/disconnect"
  ([stripe-client account-id]
   (disconnect-linked-account stripe-client account-id {}))
  ([stripe-client account-id opts]
   (request stripe-client :post 
                (str stripe-linked-accounts-endpoint "/" account-id "/disconnect")
                {}
                opts)))

(defn refresh-linked-account
  "Refreshes the linked account balance for the account.
   \nStripe API docs: https://stripe.com/docs/api/linked_accounts/refresh"
  ([stripe-client account-id]
   (refresh-linked-account stripe-client account-id {}))
  ([stripe-client account-id params]
   (refresh-linked-account stripe-client account-id params {}))
  ([stripe-client account-id params opts]
   (request stripe-client :post 
                (str stripe-linked-accounts-endpoint "/" account-id "/refresh")
                params
                opts)))

(defn list-linked-account-owners
  "Lists all owners for the linked account.
   \nStripe API docs: https://stripe.com/docs/api/linked_accounts/owners"
  ([stripe-client account-id]
   (list-linked-account-owners stripe-client account-id {} {}))
  ([stripe-client account-id params]
   (list-linked-account-owners stripe-client account-id params {}))
  ([stripe-client account-id params opts]
   (request stripe-client :get 
                (str stripe-linked-accounts-endpoint "/" account-id "/owners")
                params
                opts)))