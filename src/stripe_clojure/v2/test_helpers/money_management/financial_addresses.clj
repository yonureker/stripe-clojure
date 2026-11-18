(ns stripe-clojure.v2.test-helpers.money-management.financial-addresses
  "V2 Money Management Financial Addresses test helpers.

   Test helpers simulate activity on financial addresses in a sandbox
   environment.

   Stripe docs: https://docs.stripe.com/api/v2/money-management/financial-addresses"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private endpoint (:v2-test-helpers-money-management-financial-addresses config/stripe-v2-endpoints))

(defn credit-financial-address
  "Simulates crediting a financial address in a sandbox environment. This
   adds virtual funds and increases your balance for testing.

   Parameters:
   - stripe-client: The Stripe client instance
   - financial-address-id: The ID of the financial address to credit
   - params: The credit parameters
     - :amount (required) - The amount and currency to credit
       - :value (required) - The amount in the smallest currency unit
       - :currency (required) - The currency of the amount
     - :network (required) - The network to simulate (e.g., \"ach\", \"fedwire\", \"sepa\")
     - :statement_descriptor - The statement descriptor of the resulting received credit
   - opts: Optional request options

   Returns:
   The financial address credit simulation object.

   Example:
   (credit-financial-address client \"finaddr_xxx\"
     {:amount {:value 1000 :currency \"usd\"}
      :network \"ach\"})"
  ([stripe-client financial-address-id params]
   (credit-financial-address stripe-client financial-address-id params {}))
  ([stripe-client financial-address-id params opts]
   (request stripe-client :post (str endpoint "/" financial-address-id "/credit") params opts)))
