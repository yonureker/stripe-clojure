(ns stripe-clojure.test-helpers.customers
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-test-helpers-customers-endpoint (:test-helpers-customers config/stripe-endpoints))

(defn fund-cash-balance
  "Simulates funding a customer's cash balance in test mode.
  Parameters:
  - customer-id: The ID of the customer to fund.
  - params: A map containing:
    - :amount - The amount to fund in the smallest currency unit (e.g., 100 cents to fund $1.00).
    - :currency - The three-letter ISO currency code (e.g., 'usd').
  - opts: Optional map of additional options for the request."
  ([stripe-client customer-id params]
   (fund-cash-balance stripe-client customer-id params {}))
  ([stripe-client customer-id params opts]
   (let [endpoint (str stripe-test-helpers-customers-endpoint "/" customer-id "/fund_cash_balance")]
     (request stripe-client :post endpoint params opts))))