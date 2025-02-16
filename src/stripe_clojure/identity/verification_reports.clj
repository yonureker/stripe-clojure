(ns stripe-clojure.identity.verification-reports
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-identity-verification-reports-endpoint (config/stripe-endpoints :identity-verification-reports))

(defn retrieve-verification-report
  "Retrieves an identity verification report.
   \nStripe API docs: https://stripe.com/docs/api/identity/verification_reports/retrieve"
  ([stripe-client report-id]
   (retrieve-verification-report stripe-client report-id {}))
  ([stripe-client report-id opts]
   (request stripe-client :get
            (str stripe-identity-verification-reports-endpoint "/" report-id)
            {}
            opts)))

(defn list-verification-reports
  "Lists all identity verification reports.
   \nStripe API docs: https://stripe.com/docs/api/identity/verification_reports/list"
  ([stripe-client]
   (list-verification-reports stripe-client {} {}))
  ([stripe-client params]
   (list-verification-reports stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-identity-verification-reports-endpoint params opts))) 