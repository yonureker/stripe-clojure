(ns stripe-clojure.apps.secrets
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-apps-secrets-endpoint (config/stripe-endpoints :apps-secrets))

(defn set-secret
  "Create or replace a secret in the Stripe Apps secret store.
   \nStripe API docs: https://stripe.com/docs/api/apps/secret_store/set"
  ([stripe-client params]
   (set-secret stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-apps-secrets-endpoint params opts)))

(defn list-secrets
  "List all secrets stored on the given scope.
   \nStripe API docs: https://stripe.com/docs/api/apps/secret_store/list"
  ([stripe-client]
   (list-secrets stripe-client {} {}))
  ([stripe-client params]
   (list-secrets stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-apps-secrets-endpoint params opts)))

(defn delete-secret
  "Deletes a secret from the Stripe Apps secret store by name.
   \nStripe API docs: https://stripe.com/docs/api/apps/secret_store/delete"
  ([stripe-client params]
   (delete-secret stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post
                (str stripe-apps-secrets-endpoint "/delete")
                params
                opts)))

(defn find-secret
  "Finds a secret in the Stripe Apps secret store by name.
   \nStripe API docs: https://stripe.com/docs/api/apps/secret_store/find"
  ([stripe-client params]
   (find-secret stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get
                (str stripe-apps-secrets-endpoint "/find" )
                params
                opts)))