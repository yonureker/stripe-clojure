(ns stripe-clojure.v2.core.account-tokens
  "V2 Core Account Tokens API.

   Account Tokens tokenize sensitive account information and can be used
   when creating or updating an Account.

   Docs: https://docs.stripe.com/api/v2/core/account-tokens"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-core-account-tokens config/stripe-v2-endpoints))

(defn create-account-token
  "Creates an Account Token.

   Docs: https://docs.stripe.com/api/v2/core/account-tokens/create"
  ([stripe-client params]
   (create-account-token stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))

(defn retrieve-account-token
  "Retrieves an Account Token by ID.

   Docs: https://docs.stripe.com/api/v2/core/account-tokens/retrieve"
  ([stripe-client token-id]
   (retrieve-account-token stripe-client token-id {}))
  ([stripe-client token-id opts]
   (request stripe-client :get (str endpoint "/" token-id) {} opts)))
