(ns stripe-clojure.v2.core.account-links
  "V2 Core Account Links API.

   Account Links create single-use URLs that an account can use to access
   a Stripe-hosted flow for collecting or updating required information.

   Docs: https://docs.stripe.com/api/v2/core/account-links"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-core-account-links config/stripe-v2-endpoints))

(defn create-account-link
  "Creates an AccountLink object that includes a single-use URL.

   Docs: https://docs.stripe.com/api/v2/core/account-links/create"
  ([stripe-client params]
   (create-account-link stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post endpoint params opts)))
