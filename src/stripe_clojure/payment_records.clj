(ns stripe-clojure.payment-records
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-payment-records-endpoint (config/stripe-endpoints :payment-records))

(defn report-payment
  "Report a new payment record.
   \nStripe API docs: https://docs.stripe.com/api/payment-record/report-payment/report"
  ([stripe-client params]
   (report-payment stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post
            (str stripe-payment-records-endpoint "/report_payment")
            params
            opts)))

(defn retrieve-payment-record
  "Retrieves a payment record with the given ID.
   \nStripe API docs: https://docs.stripe.com/api/payment-record/retrieve"
  ([stripe-client payment-record-id]
   (retrieve-payment-record stripe-client payment-record-id {}))
  ([stripe-client payment-record-id opts]
   (request stripe-client :get
            (str stripe-payment-records-endpoint "/" payment-record-id)
            {}
            opts)))

(defn report-payment-attempt
  "Report a new payment attempt on the specified payment record.
   \nStripe API docs: https://docs.stripe.com/api/payment-record/report-payment-attempt/report"
  ([stripe-client payment-record-id params]
   (report-payment-attempt stripe-client payment-record-id params {}))
  ([stripe-client payment-record-id params opts]
   (request stripe-client :post
            (str stripe-payment-records-endpoint "/" payment-record-id "/report_payment_attempt")
            params
            opts)))

(defn report-payment-attempt-canceled
  "Report that the most recent payment attempt on the specified payment record was canceled.
   \nStripe API docs: https://docs.stripe.com/api/payment-record/report"
  ([stripe-client payment-record-id params]
   (report-payment-attempt-canceled stripe-client payment-record-id params {}))
  ([stripe-client payment-record-id params opts]
   (request stripe-client :post
            (str stripe-payment-records-endpoint "/" payment-record-id "/report_payment_attempt_canceled")
            params
            opts)))

(defn report-payment-attempt-failed
  "Report that the most recent payment attempt on the specified payment record failed.
   \nStripe API docs: https://docs.stripe.com/api/payment-record/report-payment-attempt-failed/report"
  ([stripe-client payment-record-id params]
   (report-payment-attempt-failed stripe-client payment-record-id params {}))
  ([stripe-client payment-record-id params opts]
   (request stripe-client :post
            (str stripe-payment-records-endpoint "/" payment-record-id "/report_payment_attempt_failed")
            params
            opts)))

(defn report-payment-attempt-guaranteed
  "Report that the most recent payment attempt on the specified payment record was guaranteed.
   \nStripe API docs: https://docs.stripe.com/api/payment-record/report-payment-attempt-guaranteed/report"
  ([stripe-client payment-record-id params]
   (report-payment-attempt-guaranteed stripe-client payment-record-id params {}))
  ([stripe-client payment-record-id params opts]
   (request stripe-client :post
            (str stripe-payment-records-endpoint "/" payment-record-id "/report_payment_attempt_guaranteed")
            params
            opts)))

(defn report-payment-attempt-informational
  "Report informational updates on the specified payment record.
   \nStripe API docs: https://docs.stripe.com/api/payment-record/report-payment-attempt-informational/report"
  ([stripe-client payment-record-id params]
   (report-payment-attempt-informational stripe-client payment-record-id params {}))
  ([stripe-client payment-record-id params opts]
   (request stripe-client :post
            (str stripe-payment-records-endpoint "/" payment-record-id "/report_payment_attempt_informational")
            params
            opts)))

(defn report-refund
  "Report that the most recent payment attempt on the specified payment record was refunded.
   \nStripe API docs: https://docs.stripe.com/api/payment-record/report-refund/report"
  ([stripe-client payment-record-id params]
   (report-refund stripe-client payment-record-id params {}))
  ([stripe-client payment-record-id params opts]
   (request stripe-client :post
            (str stripe-payment-records-endpoint "/" payment-record-id "/report_refund")
            params
            opts)))
