(ns stripe-clojure.issuing.tokens
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-issuing-tokens-endpoint (config/stripe-endpoints :issuing-tokens))

(defn retrieve-token
  "Retrieves an issuing token by ID.
   \nStripe API docs: https://stripe.com/docs/api/issuing/tokens/retrieve"
  ([stripe-client token-id]
   (retrieve-token stripe-client token-id {}))
  ([stripe-client token-id opts]
   (request stripe-client :get
                (str stripe-issuing-tokens-endpoint "/" token-id)
                {}
                opts)))

(defn update-token
  "Updates an issuing token.
   \nStripe API docs: https://stripe.com/docs/api/issuing/tokens/update"
  ([stripe-client token-id params]
   (update-token stripe-client token-id params {}))
  ([stripe-client token-id params opts]
   (request stripe-client :post
                (str stripe-issuing-tokens-endpoint "/" token-id)
                params
                opts)))

(defn list-tokens
  "Lists all issuing tokens.
   \nStripe API docs: https://stripe.com/docs/api/issuing/tokens/list"
  ([stripe-client params]
   (list-tokens stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-issuing-tokens-endpoint params opts))) 