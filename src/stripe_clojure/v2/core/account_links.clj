(ns stripe-clojure.v2.core.account-links
  "V2 Core Account Links API.

   Account Links create single-use URLs that an account can use to access
   a Stripe-hosted flow for collecting or updating required information.

   Stripe docs: https://docs.stripe.com/api/v2/core/account_links"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-core-account-links config/stripe-v2-endpoints))

(defn create-account-link
  "Creates an AccountLink object that includes a single-use URL.

   Docs: https://docs.stripe.com/api/v2/core/account_links/create

   Parameters:
   - stripe-client: The Stripe client instance
   - params: The account link parameters
     - :account (required) - The ID of the account to create a link for
     - :use_case (required) - The use case for the account link
   - opts: Optional request options

   Returns:
   The created account link object.

   Example:
   (create-account-link client
     {:account \"acct_xxx\"
      :use_case {:type \"account_onboarding\"}})"
  ([stripe-client params]
   (create-account-link stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))
