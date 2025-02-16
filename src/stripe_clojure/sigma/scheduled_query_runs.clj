(ns stripe-clojure.sigma.scheduled-query-runs
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-sigma-scheduled-query-runs-endpoint (config/stripe-endpoints :sigma-scheduled-query-runs))

(defn retrieve-scheduled-query-run
  "Retrieves a sigma scheduled query run.
   \nStripe API docs: https://stripe.com/docs/api/sigma/scheduled_queries/retrieve"
  ([stripe-client scheduled-query-run-id]
   (retrieve-scheduled-query-run stripe-client scheduled-query-run-id {}))
  ([stripe-client scheduled-query-run-id opts]
   (request stripe-client :get
                (str stripe-sigma-scheduled-query-runs-endpoint "/" scheduled-query-run-id)
                {}
                opts)))

(defn list-scheduled-query-runs
  "Lists all sigma scheduled query runs.
   \nStripe API docs: https://stripe.com/docs/api/sigma/scheduled_queries/list"
  ([stripe-client]
   (list-scheduled-query-runs stripe-client {} {}))
  ([stripe-client params]
   (list-scheduled-query-runs stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-sigma-scheduled-query-runs-endpoint params opts))) 