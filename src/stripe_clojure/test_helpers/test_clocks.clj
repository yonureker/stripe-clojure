(ns stripe-clojure.test-helpers.test-clocks
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-test-helpers-test-clocks-endpoint (:test-helpers-test-clocks config/stripe-endpoints))

(defn create-test-clock
  "Creates a new test clock.
   \nStripe API docs: https://stripe.com/docs/api/test_clocks/create"
  ([stripe-client params]
   (create-test-clock stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-test-helpers-test-clocks-endpoint params opts)))

(defn retrieve-test-clock
  "Retrieves a test clock.
   \nStripe API docs: https://stripe.com/docs/api/test_clocks/retrieve"
  ([stripe-client test-clock-id]
   (retrieve-test-clock stripe-client test-clock-id {}))
  ([stripe-client test-clock-id opts]
   (request stripe-client :get
            (str stripe-test-helpers-test-clocks-endpoint "/" test-clock-id)
            {}
            opts)))

(defn list-test-clocks
  "Lists all test clocks.
   \nStripe API docs: https://stripe.com/docs/api/test_clocks/list"
  ([stripe-client]
   (list-test-clocks stripe-client {} {}))
  ([stripe-client params]
   (list-test-clocks stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-test-helpers-test-clocks-endpoint params opts)))

(defn delete-test-clock
  "Deletes a test clock.
   \nStripe API docs: https://stripe.com/docs/api/test_clocks/delete"
  ([stripe-client test-clock-id]
   (delete-test-clock stripe-client test-clock-id {}))
  ([stripe-client test-clock-id opts]
   (request stripe-client :delete
            (str stripe-test-helpers-test-clocks-endpoint "/" test-clock-id)
            {}
            opts)))

(defn advance-test-clock
  "Starts advancing a test clock to a specified time in the future.
   \nStripe API docs: https://stripe.com/docs/api/test_clocks/advance"
  ([stripe-client test-clock-id params]
   (advance-test-clock stripe-client test-clock-id params {}))
  ([stripe-client test-clock-id params opts]
   (request stripe-client :post
            (str stripe-test-helpers-test-clocks-endpoint "/" test-clock-id "/advance")
            params
            opts)))
