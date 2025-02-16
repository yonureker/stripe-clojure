(ns stripe-clojure.reporting.report-runs
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-reporting-report-runs-endpoint (config/stripe-endpoints :reporting-report-runs))

(defn create-report-run
  "Creates a new report run.
   \nStripe API docs: https://stripe.com/docs/api/reporting/report_run/create"
  ([stripe-client params]
   (create-report-run stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-reporting-report-runs-endpoint params opts)))

(defn retrieve-report-run
  "Retrieves a report run.
   \nStripe API docs: https://stripe.com/docs/api/reporting/report_run/retrieve"
  ([stripe-client report-run-id]
   (retrieve-report-run stripe-client report-run-id {}))
  ([stripe-client report-run-id opts]
   (request stripe-client :get
                (str stripe-reporting-report-runs-endpoint "/" report-run-id)
                {}
                opts)))

(defn list-report-runs
  "Lists all report runs.
   \nStripe API docs: https://stripe.com/docs/api/reporting/report_run/list"
  ([stripe-client]
   (list-report-runs stripe-client {} {}))
  ([stripe-client params]
   (list-report-runs stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-reporting-report-runs-endpoint params opts))) 