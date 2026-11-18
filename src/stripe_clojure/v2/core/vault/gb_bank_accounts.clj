(ns stripe-clojure.v2.core.vault.gb-bank-accounts
  "V2 Core Vault GB Bank Accounts API.

   GB bank accounts are vaulted bank account credentials that can be used
   as destinations for outbound money movement.

   Stripe docs: https://docs.stripe.com/api/v2/core/vault/gb_bank_accounts"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-core-vault-gb-bank-accounts config/stripe-v2-endpoints))

(defn create-gb-bank-account
  "Creates a GB bank account.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: The bank account parameters
     - :currency (required) - The currency of the bank account
     - :account_number - The account number of the bank account
     - :sort_code - The sort code of the bank account
     - :iban - The IBAN of the bank account
     - :bank_account_type - The type of the bank account (checking or savings)
   - opts: Optional request options

   Returns:
   The created GB bank account object.

   Example:
   (create-gb-bank-account client
     {:currency \"gbp\"
      :account_number \"00012345\"
      :sort_code \"108800\"})"
  ([stripe-client params]
   (create-gb-bank-account stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))

(defn retrieve-gb-bank-account
  "Retrieves a GB bank account by ID.

   Parameters:
   - stripe-client: The Stripe client instance
   - bank-account-id: The ID of the GB bank account
   - opts: Optional request options

   Returns:
   The GB bank account object.

   Example:
   (retrieve-gb-bank-account client \"gbba_xxx\")"
  ([stripe-client bank-account-id]
   (retrieve-gb-bank-account stripe-client bank-account-id {}))
  ([stripe-client bank-account-id opts]
   (request stripe-client :get (str endpoint "/" bank-account-id) {} opts)))

(defn list-gb-bank-accounts
  "Lists all GB bank accounts.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: Optional query parameters
     - :limit - Maximum number of results per page
   - opts: Optional request options

   Returns:
   A list object containing GB bank accounts.

   Example:
   (list-gb-bank-accounts client {:limit 10})"
  ([stripe-client]
   (list-gb-bank-accounts stripe-client {}))
  ([stripe-client params]
   (list-gb-bank-accounts stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))

(defn archive-gb-bank-account
  "Archives a GB bank account. Archived bank accounts cannot be used as
   outbound destinations and do not appear in the outbound destination list.

   Parameters:
   - stripe-client: The Stripe client instance
   - bank-account-id: The ID of the GB bank account to archive
   - opts: Optional request options

   Returns:
   The archived GB bank account object.

   Example:
   (archive-gb-bank-account client \"gbba_xxx\")"
  ([stripe-client bank-account-id]
   (archive-gb-bank-account stripe-client bank-account-id {}))
  ([stripe-client bank-account-id opts]
   (request stripe-client :post (str endpoint "/" bank-account-id "/archive") {} opts)))

(defn unarchive-gb-bank-account
  "Unarchives a GB bank account.

   Parameters:
   - stripe-client: The Stripe client instance
   - bank-account-id: The ID of the GB bank account to unarchive
   - opts: Optional request options

   Returns:
   The unarchived GB bank account object.

   Example:
   (unarchive-gb-bank-account client \"gbba_xxx\")"
  ([stripe-client bank-account-id]
   (unarchive-gb-bank-account stripe-client bank-account-id {}))
  ([stripe-client bank-account-id opts]
   (request stripe-client :post (str endpoint "/" bank-account-id "/unarchive") {} opts)))
