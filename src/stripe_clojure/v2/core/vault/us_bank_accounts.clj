(ns stripe-clojure.v2.core.vault.us-bank-accounts
  "V2 Core Vault US Bank Accounts API.

   US bank accounts are vaulted bank account credentials that can be used
   as destinations for outbound money movement.

   Stripe docs: https://docs.stripe.com/api/v2/core/vault/us_bank_accounts"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-core-vault-us-bank-accounts config/stripe-v2-endpoints))

(defn create-us-bank-account
  "Creates a US bank account.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: The bank account parameters
     - :account_number (required) - The account number of the bank account
     - :currency (required) - The currency of the bank account
     - :routing_number - The ACH routing number of the bank account
     - :fedwire_routing_number - The fedwire routing number of the bank account
     - :bank_account_type - The type of the bank account (checking or savings)
   - opts: Optional request options

   Returns:
   The created US bank account object.

   Example:
   (create-us-bank-account client
     {:account_number \"000123456789\"
      :routing_number \"110000000\"
      :currency \"usd\"})"
  ([stripe-client params]
   (create-us-bank-account stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))

(defn retrieve-us-bank-account
  "Retrieves a US bank account by ID.

   Parameters:
   - stripe-client: The Stripe client instance
   - bank-account-id: The ID of the US bank account
   - opts: Optional request options

   Returns:
   The US bank account object.

   Example:
   (retrieve-us-bank-account client \"usba_xxx\")"
  ([stripe-client bank-account-id]
   (retrieve-us-bank-account stripe-client bank-account-id {}))
  ([stripe-client bank-account-id opts]
   (request stripe-client :get (str endpoint "/" bank-account-id) {} opts)))

(defn update-us-bank-account
  "Updates a US bank account. This is limited to supplying a previously
   empty routing number.

   Parameters:
   - stripe-client: The Stripe client instance
   - bank-account-id: The ID of the US bank account to update
   - params: The parameters to update
     - :routing_number - The ACH routing number of the bank account
     - :fedwire_routing_number - The fedwire routing number of the bank account
   - opts: Optional request options

   Returns:
   The updated US bank account object.

   Example:
   (update-us-bank-account client \"usba_xxx\"
     {:routing_number \"110000000\"})"
  ([stripe-client bank-account-id params]
   (update-us-bank-account stripe-client bank-account-id params {}))
  ([stripe-client bank-account-id params opts]
   (request stripe-client :post (str endpoint "/" bank-account-id) params opts)))

(defn list-us-bank-accounts
  "Lists all US bank accounts.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: Optional query parameters
     - :limit - Maximum number of results per page
     - :verification_status - Filter by verification status
   - opts: Optional request options

   Returns:
   A list object containing US bank accounts.

   Example:
   (list-us-bank-accounts client {:limit 10})"
  ([stripe-client]
   (list-us-bank-accounts stripe-client {}))
  ([stripe-client params]
   (list-us-bank-accounts stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))

(defn archive-us-bank-account
  "Archives a US bank account. Archived bank accounts cannot be used as
   outbound destinations and do not appear in the outbound destination list.

   Parameters:
   - stripe-client: The Stripe client instance
   - bank-account-id: The ID of the US bank account to archive
   - opts: Optional request options

   Returns:
   The archived US bank account object.

   Example:
   (archive-us-bank-account client \"usba_xxx\")"
  ([stripe-client bank-account-id]
   (archive-us-bank-account stripe-client bank-account-id {}))
  ([stripe-client bank-account-id opts]
   (request stripe-client :post (str endpoint "/" bank-account-id "/archive") {} opts)))

(defn unarchive-us-bank-account
  "Unarchives a US bank account.

   Parameters:
   - stripe-client: The Stripe client instance
   - bank-account-id: The ID of the US bank account to unarchive
   - opts: Optional request options

   Returns:
   The unarchived US bank account object.

   Example:
   (unarchive-us-bank-account client \"usba_xxx\")"
  ([stripe-client bank-account-id]
   (unarchive-us-bank-account stripe-client bank-account-id {}))
  ([stripe-client bank-account-id opts]
   (request stripe-client :post (str endpoint "/" bank-account-id "/unarchive") {} opts)))

(defn send-microdeposits
  "Sends microdeposits to verify a US bank account. This starts the
   verification process, which is completed with confirm-microdeposits.

   Parameters:
   - stripe-client: The Stripe client instance
   - bank-account-id: The ID of the US bank account to verify
   - opts: Optional request options

   Returns:
   The US bank account object.

   Example:
   (send-microdeposits client \"usba_xxx\")"
  ([stripe-client bank-account-id]
   (send-microdeposits stripe-client bank-account-id {}))
  ([stripe-client bank-account-id opts]
   (request stripe-client :post (str endpoint "/" bank-account-id "/send_microdeposits") {} opts)))

(defn confirm-microdeposits
  "Confirms the microdeposit amounts or descriptor code received after
   send-microdeposits. Once confirmed, the US bank account is verified.

   Parameters:
   - stripe-client: The Stripe client instance
   - bank-account-id: The ID of the US bank account to verify
   - params: The confirmation parameters
     - :amounts - The two microdeposit amounts received
     - :descriptor_code - The descriptor code received
   - opts: Optional request options

   Returns:
   The US bank account object.

   Example:
   (confirm-microdeposits client \"usba_xxx\"
     {:amounts [32 45]})"
  ([stripe-client bank-account-id params]
   (confirm-microdeposits stripe-client bank-account-id params {}))
  ([stripe-client bank-account-id params opts]
   (request stripe-client :post (str endpoint "/" bank-account-id "/confirm_microdeposits") params opts)))
