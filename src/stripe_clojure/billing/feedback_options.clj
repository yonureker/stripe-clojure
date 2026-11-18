(ns stripe-clojure.billing.feedback-options
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-billing-feedback-options-endpoint (config/stripe-endpoints :billing-feedback-options))

(defn create-feedback-option
  "Creates a new feedback option.
   \nStripe API docs: https://stripe.com/docs/api/billing/feedback_option/create"
  ([stripe-client params]
   (create-feedback-option stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-billing-feedback-options-endpoint params opts)))

(defn retrieve-feedback-option
  "Retrieves a feedback option.
   \nStripe API docs: https://stripe.com/docs/api/billing/feedback_option/retrieve"
  ([stripe-client feedback-option-id]
   (retrieve-feedback-option stripe-client feedback-option-id {}))
  ([stripe-client feedback-option-id opts]
   (request stripe-client :get
            (str stripe-billing-feedback-options-endpoint "/" feedback-option-id)
            {}
            opts)))

(defn update-feedback-option
  "Updates a feedback option.
   \nStripe API docs: https://stripe.com/docs/api/billing/feedback_option/update"
  ([stripe-client feedback-option-id params]
   (update-feedback-option stripe-client feedback-option-id params {}))
  ([stripe-client feedback-option-id params opts]
   (request stripe-client :post
            (str stripe-billing-feedback-options-endpoint "/" feedback-option-id)
            params
            opts)))

(defn list-feedback-options
  "Lists all feedback options.
   \nStripe API docs: https://stripe.com/docs/api/billing/feedback_option/list"
  ([stripe-client]
   (list-feedback-options stripe-client {} {}))
  ([stripe-client params]
   (list-feedback-options stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-billing-feedback-options-endpoint params opts)))

(defn deactivate-feedback-option
  "Deactivates a feedback option.
   \nStripe API docs: https://stripe.com/docs/api/billing/feedback_option/deactivate"
  ([stripe-client feedback-option-id]
   (deactivate-feedback-option stripe-client feedback-option-id {}))
  ([stripe-client feedback-option-id opts]
   (request stripe-client :post
            (str stripe-billing-feedback-options-endpoint "/" feedback-option-id "/deactivate")
            {}
            opts)))
