(ns stripe-clojure.v2.core.vault.gb-bank-accounts
  "V2 Core Vault GB Bank Accounts API.

   GB bank accounts are vaulted bank account credentials that can be used
   as destinations for outbound money movement.

   Docs: https://docs.stripe.com/api/v2/core/vault/gb-bank-accounts"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-core-vault-gb-bank-accounts config/stripe-v2-endpoints))

(defn create-gb-bank-account
  "Creates a GB bank account.

   Docs: https://docs.stripe.com/api/v2/core/vault/gb-bank-accounts/create"
  ([stripe-client params]
   (create-gb-bank-account stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))

(defn retrieve-gb-bank-account
  "Retrieves a GB bank account by ID.

   Docs: https://docs.stripe.com/api/v2/core/vault/gb-bank-accounts/retrieve"
  ([stripe-client bank-account-id]
   (retrieve-gb-bank-account stripe-client bank-account-id {}))
  ([stripe-client bank-account-id opts]
   (request stripe-client :get (str endpoint "/" bank-account-id) {} opts)))

(defn list-gb-bank-accounts
  "Lists all GB bank accounts.

   Docs: https://docs.stripe.com/api/v2/core/vault/gb-bank-accounts/list"
  ([stripe-client]
   (list-gb-bank-accounts stripe-client {}))
  ([stripe-client params]
   (list-gb-bank-accounts stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))

(defn archive-gb-bank-account
  "Archives a GB bank account. Archived bank accounts cannot be used as
   outbound destinations and do not appear in the outbound destination list.

   Docs: https://docs.stripe.com/api/v2/core/vault/gb-bank-accounts/archive"
  ([stripe-client bank-account-id]
   (archive-gb-bank-account stripe-client bank-account-id {}))
  ([stripe-client bank-account-id opts]
   (request stripe-client :post (str endpoint "/" bank-account-id "/archive") {} opts)))

(defn unarchive-gb-bank-account
  "Unarchives a GB bank account.

   Docs: https://docs.stripe.com/api/v2/core/vault/gb-bank-accounts/unarchive"
  ([stripe-client bank-account-id]
   (unarchive-gb-bank-account stripe-client bank-account-id {}))
  ([stripe-client bank-account-id opts]
   (request stripe-client :post (str endpoint "/" bank-account-id "/unarchive") {} opts)))
