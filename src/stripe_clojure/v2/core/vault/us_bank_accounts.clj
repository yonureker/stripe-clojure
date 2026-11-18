(ns stripe-clojure.v2.core.vault.us-bank-accounts
  "V2 Core Vault US Bank Accounts API.

   US bank accounts are vaulted bank account credentials that can be used
   as destinations for outbound money movement.

   Docs: https://docs.stripe.com/api/v2/core/vault/us-bank-accounts"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-core-vault-us-bank-accounts config/stripe-v2-endpoints))

(defn create-us-bank-account
  "Creates a US bank account.

   Docs: https://docs.stripe.com/api/v2/core/vault/us-bank-accounts/create"
  ([stripe-client params]
   (create-us-bank-account stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))

(defn retrieve-us-bank-account
  "Retrieves a US bank account by ID.

   Docs: https://docs.stripe.com/api/v2/core/vault/us-bank-accounts/retrieve"
  ([stripe-client bank-account-id]
   (retrieve-us-bank-account stripe-client bank-account-id {}))
  ([stripe-client bank-account-id opts]
   (request stripe-client :get (str endpoint "/" bank-account-id) {} opts)))

(defn update-us-bank-account
  "Updates a US bank account. This is limited to supplying a previously
   empty routing number.

   Docs: https://docs.stripe.com/api/v2/core/vault/us-bank-accounts/update"
  ([stripe-client bank-account-id params]
   (update-us-bank-account stripe-client bank-account-id params {}))
  ([stripe-client bank-account-id params opts]
   (request stripe-client :post (str endpoint "/" bank-account-id) params opts)))

(defn list-us-bank-accounts
  "Lists all US bank accounts.

   Docs: https://docs.stripe.com/api/v2/core/vault/us-bank-accounts/list"
  ([stripe-client]
   (list-us-bank-accounts stripe-client {}))
  ([stripe-client params]
   (list-us-bank-accounts stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))

(defn archive-us-bank-account
  "Archives a US bank account. Archived bank accounts cannot be used as
   outbound destinations and do not appear in the outbound destination list.

   Docs: https://docs.stripe.com/api/v2/core/vault/us-bank-accounts/archive"
  ([stripe-client bank-account-id]
   (archive-us-bank-account stripe-client bank-account-id {}))
  ([stripe-client bank-account-id opts]
   (request stripe-client :post (str endpoint "/" bank-account-id "/archive") {} opts)))

(defn unarchive-us-bank-account
  "Unarchives a US bank account.

   Docs: https://docs.stripe.com/api/v2/core/vault/us-bank-accounts/unarchive"
  ([stripe-client bank-account-id]
   (unarchive-us-bank-account stripe-client bank-account-id {}))
  ([stripe-client bank-account-id opts]
   (request stripe-client :post (str endpoint "/" bank-account-id "/unarchive") {} opts)))

(defn send-microdeposits
  "Sends microdeposits to verify a US bank account. This starts the
   verification process, which is completed with confirm-microdeposits.

   Docs: https://docs.stripe.com/api/v2/core/vault/us-bank-accounts/send-microdeposits"
  ([stripe-client bank-account-id]
   (send-microdeposits stripe-client bank-account-id {}))
  ([stripe-client bank-account-id opts]
   (request stripe-client :post (str endpoint "/" bank-account-id "/send_microdeposits") {} opts)))

(defn confirm-microdeposits
  "Confirms the microdeposit amounts or descriptor code received after
   send-microdeposits. Once confirmed, the US bank account is verified.

   Docs: https://docs.stripe.com/api/v2/core/vault/us-bank-accounts/confirm-microdeposits"
  ([stripe-client bank-account-id params]
   (confirm-microdeposits stripe-client bank-account-id params {}))
  ([stripe-client bank-account-id params opts]
   (request stripe-client :post (str endpoint "/" bank-account-id "/confirm_microdeposits") params opts)))
