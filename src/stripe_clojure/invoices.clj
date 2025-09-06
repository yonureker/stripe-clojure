(ns stripe-clojure.invoices
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-invoices-endpoint (config/stripe-endpoints :invoices))

(defn create-invoice
  "Creates a new invoice.
   Stripe API docs: https://stripe.com/docs/api/invoices/create

   - `payload`: A map of invoice data to send to Stripe.
   - `opts`: (Optional) A map of additional options:
     :api-key, :idempotency-key, :stripe-account, etc."
  ([stripe-client payload]
   (create-invoice stripe-client payload {}))
  ([stripe-client payload opts]
   (request stripe-client :post stripe-invoices-endpoint payload opts)))

(defn create-preview-invoice
  "Gets a preview for a new invoice.
   \nStripe API docs: https://stripe.com/docs/api/invoices/create_preview"
  ([stripe-client params]
   (create-preview-invoice stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post (str stripe-invoices-endpoint "/create_preview") params opts)))

(defn update-invoice
  "Updates an existing invoice.
   Stripe API docs: https://stripe.com/docs/api/invoices/update

   - `invoice-id`: The ID of the invoice to update.
   - `payload`: A map of invoice data to update.
   - `opts`: (Optional) A map of additional options."
  ([stripe-client invoice-id payload]
   (update-invoice stripe-client invoice-id payload {}))
  ([stripe-client invoice-id payload opts]
   (request stripe-client :post (str stripe-invoices-endpoint "/" invoice-id) payload opts)))

(defn retrieve-invoice
  "Retrieves the details of an existing invoice.
   Stripe API docs: https://stripe.com/docs/api/invoices/retrieve

   - `invoice-id`: The ID of the invoice to retrieve.
   - `opts`: (Optional) A map of additional options."
  ([stripe-client invoice-id]
   (retrieve-invoice stripe-client invoice-id {}))
  ([stripe-client invoice-id opts]
   (request stripe-client :get (str stripe-invoices-endpoint "/" invoice-id) {} opts)))

(defn list-invoices
  "Returns a list of invoices.
   Stripe API docs: https://stripe.com/docs/api/invoices/list

   - `params`: (Optional) A map of query parameters.
   - `opts`: (Optional) A map of additional options."
  ([stripe-client]
   (list-invoices stripe-client {}))
  ([stripe-client params]
   (list-invoices stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-invoices-endpoint params opts)))

(defn delete-invoice
  "Deletes a draft invoice.
   Stripe API docs: https://stripe.com/docs/api/invoices/delete

   - `invoice-id`: The ID of the invoice to delete.
   - `opts`: (Optional) A map of additional options."
  ([stripe-client invoice-id]
   (delete-invoice stripe-client invoice-id {}))
  ([stripe-client invoice-id opts]
   (request stripe-client :delete (str stripe-invoices-endpoint "/" invoice-id) {} opts)))

(defn finalize-invoice
  "Finalizes a draft invoice.
   Stripe API docs: https://stripe.com/docs/api/invoices/finalize

   - `invoice-id`: The ID of the invoice to finalize.
   - `payload`: (Optional) A map of finalization options.
   - `opts`: (Optional) A map of additional options."
  ([stripe-client invoice-id]
   (finalize-invoice stripe-client invoice-id {} {}))
  ([stripe-client invoice-id payload]
   (finalize-invoice stripe-client invoice-id payload {}))
  ([stripe-client invoice-id payload opts]
   (request stripe-client :post (str stripe-invoices-endpoint "/" invoice-id "/finalize") payload opts)))

(defn mark-invoice-uncollectible
  "Marks an invoice as uncollectible.
   Stripe API docs: https://stripe.com/docs/api/invoices/mark_uncollectible

   - `invoice-id`: The ID of the invoice to mark as uncollectible.
   - `opts`: (Optional) A map of additional options."
  ([stripe-client invoice-id]
   (mark-invoice-uncollectible stripe-client invoice-id {}))
  ([stripe-client invoice-id opts]
   (request stripe-client :post (str stripe-invoices-endpoint "/" invoice-id "/mark_uncollectible") {} opts)))

(defn pay-invoice
  "Pays an invoice.
   Stripe API docs: https://stripe.com/docs/api/invoices/pay

   - `invoice-id`: The ID of the invoice to pay.
   - `payload`: (Optional) A map of payment options.
   - `opts`: (Optional) A map of additional options."
  ([stripe-client invoice-id]
   (pay-invoice stripe-client invoice-id {} {}))
  ([stripe-client invoice-id payload]
   (pay-invoice stripe-client invoice-id payload {}))
  ([stripe-client invoice-id payload opts]
   (request stripe-client :post (str stripe-invoices-endpoint "/" invoice-id "/pay") payload opts)))

(defn search-invoices
  "Searches for invoices.
   \nStripe API docs: https://stripe.com/docs/api/invoices/search"
  ([stripe-client params]
   (search-invoices stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get
                (str stripe-invoices-endpoint "/search")
                params
                opts)))

(defn send-invoice
  "Sends an invoice to the customer.
   Stripe API docs: https://stripe.com/docs/api/invoices/send

   - `invoice-id`: The ID of the invoice to send.
   - `opts`: (Optional) A map of additional options."
  ([stripe-client invoice-id]
   (send-invoice stripe-client invoice-id {}))
  ([stripe-client invoice-id opts]
   (request stripe-client :post (str stripe-invoices-endpoint "/" invoice-id "/send") {} opts)))

(defn void-invoice
  "Voids an invoice.
   Stripe API docs: https://stripe.com/docs/api/invoices/void

   - `invoice-id`: The ID of the invoice to void.
   - `opts`: (Optional) A map of additional options."
  ([stripe-client invoice-id]
   (void-invoice stripe-client invoice-id {}))
  ([stripe-client invoice-id opts]
   (request stripe-client :post (str stripe-invoices-endpoint "/" invoice-id "/void") {} opts)))

;; Line Items

(defn list-line-items
  "Lists all line items for an invoice.
   \nStripe API docs: https://stripe.com/docs/api/invoices/lines"
  ([stripe-client invoice-id]
   (list-line-items stripe-client invoice-id {}))
  ([stripe-client invoice-id params]
   (list-line-items stripe-client invoice-id params {}))
  ([stripe-client invoice-id params opts]
   (request stripe-client :get
                (str stripe-invoices-endpoint "/" invoice-id "/lines")
                params
                opts)))

(defn update-line-item
  "Updates an invoice's line item.
   \nStripe API docs: https://www.stripe.com/docs/api/invoice-line-item/update"
  ([stripe-client invoice-id line-item-id params]
   (update-line-item stripe-client invoice-id line-item-id params {}))
  ([stripe-client invoice-id line-item-id params opts]
   (request stripe-client :post
                (str stripe-invoices-endpoint "/" invoice-id "/lines/" line-item-id)
                params
                opts)))

(defn add-line-items
  "Adds line items to an invoice.
   \nStripe API docs: https://stripe.com/docs/api/invoice-line-item/bulk"
  ([stripe-client invoice-id params]
   (add-line-items stripe-client invoice-id params {}))
  ([stripe-client invoice-id params opts]
   (request stripe-client :post
                (str stripe-invoices-endpoint "/" invoice-id "/add_lines")
                params
                opts)))

(defn remove-line-items
  "Removes line items from an invoice.
   \nStripe API docs: https://stripe.com/docs/api/invoice-line-item/invoices/remove-lines/bulk"
  ([stripe-client invoice-id params]
   (remove-line-items stripe-client invoice-id params {}))
  ([stripe-client invoice-id params opts]
   (request stripe-client :post
                (str stripe-invoices-endpoint "/" invoice-id "/remove_lines")
                params
                opts)))

(defn update-line-items
  "Updates line items on an invoice.
   \nStripe API docs: https://stripe.com/docs/api/invoice-line-item/invoices/update-lines/bulk"
  ([stripe-client invoice-id params]
   (update-line-items stripe-client invoice-id params {}))
  ([stripe-client invoice-id params opts]
   (request stripe-client :post
                (str stripe-invoices-endpoint "/" invoice-id "/update_lines")
                params
                opts)))

(defn attach-payment
  "Attaches a payment to an invoice.
   \nStripe API docs: https://stripe.com/docs/api/invoices/attach_payment"
  ([stripe-client invoice-id params]
   (attach-payment stripe-client invoice-id params {}))
  ([stripe-client invoice-id params opts]
   (request stripe-client :post
                (str stripe-invoices-endpoint "/" invoice-id "/attach_payment")
                params
                opts)))
