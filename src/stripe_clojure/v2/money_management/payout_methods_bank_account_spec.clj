(ns stripe-clojure.v2.money-management.payout-methods-bank-account-spec
  "V2 Money Management Payout Methods Bank Account Spec API.

   The bank account spec describes which credential fields are required
   for a set of countries and the validations for each field.

   Stripe docs: https://docs.stripe.com/api/v2/money-management/payout-methods-bank-account-spec"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-money-management-payout-methods-bank-account-spec config/stripe-v2-endpoints))

(defn retrieve-payout-methods-bank-account-spec
  "Retrieves the bank account specifications for a set of countries.

   Parameters:
   - stripe-client: The Stripe client instance
   - params: Optional query parameters
     - :countries - The countries to fetch the bank account spec for
   - opts: Optional request options

   Returns:
   The payout methods bank account spec object.

   Example:
   (retrieve-payout-methods-bank-account-spec client {:countries [\"US\" \"GB\"]})"
  ([stripe-client]
   (retrieve-payout-methods-bank-account-spec stripe-client {}))
  ([stripe-client params]
   (retrieve-payout-methods-bank-account-spec stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get endpoint params opts)))
