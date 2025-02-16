(ns stripe-clojure.invoice-rendering-templates
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-invoice-rendering-templates-endpoint 
  (config/stripe-endpoints :invoice-rendering-templates))

(defn retrieve-invoice-rendering-template
  "Retrieves an invoice rendering template with the given ID.
   \nStripe API docs: https://stripe.com/docs/api/invoice_rendering_templates/retrieve"
  ([stripe-client template-id]
   (retrieve-invoice-rendering-template stripe-client template-id {}))
  ([stripe-client template-id opts]
   (request stripe-client :get 
                (str stripe-invoice-rendering-templates-endpoint "/" template-id)
                {}
                opts)))

(defn list-invoice-rendering-templates
  "Lists all invoice rendering templates.
   \nStripe API docs: https://stripe.com/docs/api/invoice_rendering_templates/list"
  ([stripe-client]
   (list-invoice-rendering-templates stripe-client {}))
  ([stripe-client params]
   (list-invoice-rendering-templates stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get
                stripe-invoice-rendering-templates-endpoint
                params
                opts)))

(defn archive-invoice-rendering-template
  "Archives an invoice rendering template.
   \nStripe API docs: https://stripe.com/docs/api/invoice_rendering_templates/archive"
  ([stripe-client template-id]
   (archive-invoice-rendering-template stripe-client template-id {}))
  ([stripe-client template-id opts]
   (request stripe-client :post
                (str stripe-invoice-rendering-templates-endpoint "/" template-id "/archive")
                {}
                opts)))

(defn unarchive-invoice-rendering-template
  "Unarchives an invoice rendering template.
   \nStripe API docs: https://stripe.com/docs/api/invoice_rendering_templates/unarchive"
  ([stripe-client template-id]
   (unarchive-invoice-rendering-template stripe-client template-id {}))
  ([stripe-client template-id opts]
   (request stripe-client :post
            (str stripe-invoice-rendering-templates-endpoint "/" template-id "/unarchive")
            {}
            opts)))