(ns stripe-clojure.v2.core.person-tokens
  "Stripe V2 Core Person Tokens API.

   Person Tokens are single-use tokens that tokenize person information,
   used for creating or updating a Person.

   Docs: https://docs.stripe.com/api/v2/person-tokens"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private accounts-endpoint (:v2-core-accounts config/stripe-v2-endpoints))

(defn- person-tokens-endpoint [account-id]
  (str accounts-endpoint "/" account-id "/person_tokens"))

(defn create-person-token
  "Creates a Person Token for an Account.

   Person Tokens tokenize sensitive person information and can be used
   when creating or updating a Person.

   Docs: https://docs.stripe.com/api/v2/person-tokens/create"
  ([stripe-client account-id params]
   (create-person-token stripe-client account-id params {}))
  ([stripe-client account-id params opts]
   (request stripe-client :post (person-tokens-endpoint account-id) params opts)))

(defn retrieve-person-token
  "Retrieves a Person Token by ID.

   Docs: https://docs.stripe.com/api/v2/person-tokens/retrieve"
  ([stripe-client account-id token-id]
   (retrieve-person-token stripe-client account-id token-id {}))
  ([stripe-client account-id token-id opts]
   (request stripe-client :get (str (person-tokens-endpoint account-id) "/" token-id) {} opts)))
