(ns stripe-clojure.reporting.report-types
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-reporting-report-types-endpoint (config/stripe-endpoints :reporting-report-types))

(defn retrieve-report-type
  "Retrieves a report type.
   \nStripe API docs: https://stripe.com/docs/api/reporting/report_type/retrieve"
  ([stripe-client report-type-id]
   (retrieve-report-type stripe-client report-type-id {}))
  ([stripe-client report-type-id opts]
   (request stripe-client :get
                (str stripe-reporting-report-types-endpoint "/" report-type-id)
                {}
                opts)))

(defn list-report-types
  "Lists all report types.
   \nStripe API docs: https://stripe.com/docs/api/reporting/report_type/list"
  ([stripe-client]
   (list-report-types stripe-client {}))
  ([stripe-client opts]
   (request stripe-client :get stripe-reporting-report-types-endpoint {} opts))) 