(ns stripe-clojure.v2.money-management.financial-accounts
  "V2 Money Management Financial Accounts API.

   A financial account represents a balance and can be the source or
   destination of money movement.

   Stripe docs: https://docs.stripe.com/api/v2/money-management/financial-accounts"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-money-management-financial-accounts config/stripe-v2-endpoints))

(defn retrieve-financial-account
  "Retrieves a financial account by ID.

   Parameters:
   - stripe-client: The Stripe client instance
   - financial-account-id: The ID of the financial account
   - opts: Optional request options
     - :include - Fields to include in the response

   Returns:
   The financial account object.

   Example:
   (retrieve-financial-account client \"fa_xxx\")"
  ([stripe-client financial-account-id]
   (retrieve-financial-account stripe-client financial-account-id {}))
  ([stripe-client financial-account-id opts]
   (request stripe-client :get (str endpoint "/" financial-account-id) {} opts)))

(defn update-financial-account
  "Updates a financial account.

   Parameters:
   - stripe-client: The Stripe client instance
   - financial-account-id: The ID of the financial account to update
   - params: The parameters to update
     - :display_name - A descriptive name for the financial account, up to 50 characters
     - :metadata - Set of key-value pairs to attach to the financial account
   - opts: Optional request options

   Returns:
   The updated financial account object.

   Example:
   (update-financial-account client \"fa_xxx\"
     {:display_name \"Operating account\"})"
  ([stripe-client financial-account-id params]
   (update-financial-account stripe-client financial-account-id params {}))
  ([stripe-client financial-account-id params opts]
   (request stripe-client :post (str endpoint "/" financial-account-id) params opts)))

(defn list-financial-accounts
  "Lists all financial accounts.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: Optional query parameters
     - :limit - The page limit
     - :statuses - Return financial accounts whose status matches any of the provided values
   - opts: Optional request options
     - :include - Fields to include in the response

   Returns:
   A list object containing financial accounts.

   Example:
   (list-financial-accounts client {:limit 10})"
  ([stripe-client]
   (list-financial-accounts stripe-client {}))
  ([stripe-client params]
   (list-financial-accounts stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))
