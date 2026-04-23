(ns stripe-clojure.v2.core.account-tokens
  "V2 Core Account Tokens API.

   Account Tokens tokenize sensitive account information and can be used
   when creating or updating an Account.

   Stripe docs: https://docs.stripe.com/api/v2/core/account_tokens"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-core-account-tokens config/stripe-v2-endpoints))

(defn create-account-token
  "Creates an Account Token.

   Docs: https://docs.stripe.com/api/v2/core/account_tokens/create

   Parameters:
   - stripe-client: The Stripe client instance
   - params: The account token parameters
     - :contact_email - The contact email for the account
     - :contact_phone - The contact phone for the account
     - :display_name - The display name for the account
     - :identity - Identity information for the account
   - opts: Optional request options

   Returns:
   The created account token object.

   Example:
   (create-account-token client
     {:display_name \"My Business\"
      :contact_email \"business@example.com\"})"
  ([stripe-client params]
   (create-account-token stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))

(defn retrieve-account-token
  "Retrieves an Account Token by ID.

   Docs: https://docs.stripe.com/api/v2/core/account_tokens/retrieve

   Parameters:
   - stripe-client: The Stripe client instance
   - token-id: The ID of the account token
   - opts: Optional request options

   Returns:
   The account token object.

   Example:
   (retrieve-account-token client \"actok_xxx\")"
  ([stripe-client token-id]
   (retrieve-account-token stripe-client token-id {}))
  ([stripe-client token-id opts]
   (request stripe-client :get (str endpoint "/" token-id) {} opts)))
