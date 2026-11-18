(ns stripe-clojure.v2.test-helpers.money-management.financial-addresses
  "V2 Money Management Financial Addresses test helpers.

   Test helpers simulate activity on financial addresses in a sandbox
   environment.

   Docs: https://docs.stripe.com/api/v2/money-management/financial-addresses"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-test-helpers-money-management-financial-addresses config/stripe-v2-endpoints))

(defn credit-financial-address
  "Simulates crediting a financial address in a sandbox environment. This
   adds virtual funds and increases your balance for testing.

   Docs: https://docs.stripe.com/api/v2/money-management/financial-addresses/credit"
  ([stripe-client financial-address-id params]
   (credit-financial-address stripe-client financial-address-id params {}))
  ([stripe-client financial-address-id params opts]
   (request stripe-client :post (str endpoint "/" financial-address-id "/credit") params opts)))
