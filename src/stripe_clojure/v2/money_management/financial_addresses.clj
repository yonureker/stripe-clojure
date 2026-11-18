(ns stripe-clojure.v2.money-management.financial-addresses
  "V2 Money Management Financial Addresses API.

   A financial address contains the information needed to transfer money
   to a financial account. A financial account can have more than one
   financial address.

   Stripe docs: https://docs.stripe.com/api/v2/money-management/financial-addresses"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-money-management-financial-addresses config/stripe-v2-endpoints))

(defn create-financial-address
  "Creates a financial address for a financial account.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: The financial address parameters
     - :financial_account (required) - The ID of the financial account to associate the address with
     - :type (required) - The type of financial address to create (e.g., \"bank_account\")
     - :bank_account - Properties for creating a bank account financial address
       - :country - The country of the bank account
       - :currency - The currency of the bank account
   - opts: Optional request options

   Returns:
   The created financial address object.

   Example:
   (create-financial-address client
     {:financial_account \"fa_xxx\"
      :type \"bank_account\"
      :bank_account {:country \"US\" :currency \"usd\"}})"
  ([stripe-client params]
   (create-financial-address stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))

(defn retrieve-financial-address
  "Retrieves a financial address by ID.

   Parameters:
   - stripe-client: The Stripe client instance
   - financial-address-id: The ID of the financial address
   - opts: Optional request options

   Returns:
   The financial address object.

   Example:
   (retrieve-financial-address client \"finaddr_xxx\")"
  ([stripe-client financial-address-id]
   (retrieve-financial-address stripe-client financial-address-id {}))
  ([stripe-client financial-address-id opts]
   (request stripe-client :get (str endpoint "/" financial-address-id) {} opts)))

(defn list-financial-addresses
  "Lists all financial addresses.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: Optional query parameters
     - :financial_account - The ID of the financial account to list financial addresses for
     - :limit - The page limit
   - opts: Optional request options

   Returns:
   A list object containing financial addresses.

   Example:
   (list-financial-addresses client {:financial_account \"fa_xxx\"})"
  ([stripe-client]
   (list-financial-addresses stripe-client {}))
  ([stripe-client params]
   (list-financial-addresses stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))
