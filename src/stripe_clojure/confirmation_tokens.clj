(ns stripe-clojure.confirmation-tokens
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-confirmation-tokens-endpoint (:confirmation-tokens config/stripe-endpoints))

(defn retrieve-confirmation-token
  "Retrieves an existing ConfirmationToken object.
   \nStripe API docs: https://stripe.com/docs/api/confirmation_tokens/retrieve"
  ([stripe-client token-id]
   (retrieve-confirmation-token stripe-client token-id {}))
  ([stripe-client token-id opts]
   (request stripe-client :get (str stripe-confirmation-tokens-endpoint "/" token-id) {} opts)))