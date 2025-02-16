(ns stripe-clojure.tokens
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-tokens-endpoint (:tokens config/stripe-endpoints))

(defn create-card-token
  "Creates a single-use token that represents a credit card's details.
   \nStripe API docs: https://stripe.com/docs/api/tokens/create_card"
  ([stripe-client params]
   (create-card-token stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-tokens-endpoint params opts)))

(defn create-bank-account-token
  "Creates a single-use token that represents a bank account's details.
   \nStripe API docs: https://stripe.com/docs/api/tokens/create_bank_account"
  ([stripe-client params]
   (create-bank-account-token stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-tokens-endpoint params opts)))

(defn create-pii-token
  "Creates a single-use token that represents personally identifiable information (PII).
   \nStripe API docs: https://stripe.com/docs/api/tokens/create_pii"
  ([stripe-client params]
   (create-pii-token stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-tokens-endpoint params opts)))

(defn create-account-token
  "Creates a single-use token that represents the details for an account.
   \nStripe API docs: https://stripe.com/docs/api/tokens/create_account"
  ([stripe-client params]
   (create-account-token stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-tokens-endpoint params opts)))

(defn create-person-token
  "Creates a single-use token that represents the details for a person.
   \nStripe API docs: https://stripe.com/docs/api/tokens/create_person"
  ([stripe-client params]
   (create-person-token stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-tokens-endpoint params opts)))

(defn create-cvc-update-token
  "Creates a single-use token that represents a CVC update.
   \nStripe API docs: https://stripe.com/docs/api/tokens/create_cvc_update"
  ([stripe-client params]
   (create-cvc-update-token stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-tokens-endpoint params opts)))

(defn retrieve-token
  "Retrieves the token with the given ID.
   \nStripe API docs: https://stripe.com/docs/api/tokens/retrieve"
  ([stripe-client token-id]
   (retrieve-token stripe-client token-id {}))
  ([stripe-client token-id opts]
   (request stripe-client :get (str stripe-tokens-endpoint "/" token-id) {} opts)))