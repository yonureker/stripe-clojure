(ns stripe-clojure.v2.core.accounts
  "Stripe V2 Core Accounts API.

   Accounts v2 is the next generation of the Connect Accounts API, providing
   improved identity management and verification capabilities.

   Docs: https://docs.stripe.com/api/v2/core/accounts"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-core-accounts config/stripe-v2-endpoints))

(defn create-account
  "Creates a new Account.

   Docs: https://docs.stripe.com/api/v2/core/accounts/create"
  ([stripe-client params]
   (create-account stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))

(defn retrieve-account
  "Retrieves an Account by ID.

   Docs: https://docs.stripe.com/api/v2/core/accounts/retrieve"
  ([stripe-client account-id]
   (retrieve-account stripe-client account-id {}))
  ([stripe-client account-id opts]
   (request stripe-client :get (str endpoint "/" account-id) {} opts)))

(defn update-account
  "Updates an Account.

   Docs: https://docs.stripe.com/api/v2/core/accounts/update"
  ([stripe-client account-id params]
   (update-account stripe-client account-id params {}))
  ([stripe-client account-id params opts]
   (request stripe-client :post (str endpoint "/" account-id) params opts)))

(defn list-accounts
  "Lists all Accounts.

   Docs: https://docs.stripe.com/api/v2/core/accounts/list"
  ([stripe-client]
   (list-accounts stripe-client {}))
  ([stripe-client params]
   (list-accounts stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))

(defn close-account
  "Closes an Account.

   Docs: https://docs.stripe.com/api/v2/core/accounts/close"
  ([stripe-client account-id]
   (close-account stripe-client account-id {}))
  ([stripe-client account-id opts]
   (request stripe-client :post (str endpoint "/" account-id "/close") {} opts)))
