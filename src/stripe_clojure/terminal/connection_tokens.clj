(ns stripe-clojure.terminal.connection-tokens
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-terminal-connection-tokens-endpoint (config/stripe-endpoints :terminal-connection-tokens))

(defn create-connection-token
  "Creates a new terminal connection token.
   \nStripe API docs: https://stripe.com/docs/api/terminal/connection_tokens/create"
  ([stripe-client params]
   (create-connection-token stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-terminal-connection-tokens-endpoint params opts))) 