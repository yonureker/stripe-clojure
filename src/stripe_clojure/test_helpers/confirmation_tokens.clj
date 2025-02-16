(ns stripe-clojure.test-helpers.confirmation-tokens
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-test-helpers-confirmation-tokens-endpoint (:test-helpers-confirmation-tokens config/stripe-endpoints))

(defn create-confirmation-token
  "Creates a test-mode confirmation token.
  
  Stripe API docs: https://docs.stripe.com/api/confirmation_tokens/test_create
  
  Accepts:
    - params: a map of parameters to be sent in the request.
    - opts: an optional map for request options.
  
  Example:
    (create-confirmation-token {:payment_method_data {...} ...})
  "
  ([stripe-client params]
   (create-confirmation-token stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-test-helpers-confirmation-tokens-endpoint params opts)))