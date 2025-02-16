(ns stripe-clojure.credit-notes
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-credit-notes-endpoint (:credit-notes config/stripe-endpoints))

(defn create-credit-note
  "Creates a new credit note.
   \nStripe API docs: https://stripe.com/docs/api/credit_notes/create"
  ([stripe-client params]
   (create-credit-note stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-credit-notes-endpoint params opts)))

(defn retrieve-credit-note
  "Retrieves the credit note object with the given identifier.
   \nStripe API docs: https://stripe.com/docs/api/credit_notes/retrieve"
  ([stripe-client credit-note-id]
   (retrieve-credit-note stripe-client credit-note-id {}))
  ([stripe-client credit-note-id opts]
   (request stripe-client :get (str stripe-credit-notes-endpoint "/" credit-note-id) {} opts)))

(defn update-credit-note
  "Updates an existing credit note.
   \nStripe API docs: https://stripe.com/docs/api/credit_notes/update"
  ([stripe-client credit-note-id params]
   (update-credit-note stripe-client credit-note-id params {}))
  ([stripe-client credit-note-id params opts]
   (request stripe-client :post (str stripe-credit-notes-endpoint "/" credit-note-id) params opts)))

(defn list-credit-notes
  "Returns a list of credit notes.
   \nStripe API docs: https://stripe.com/docs/api/credit_notes/list"
  ([stripe-client]
   (list-credit-notes stripe-client {}))
  ([stripe-client params]
   (list-credit-notes stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-credit-notes-endpoint params opts)))

(defn void-credit-note
  "Marks a credit note as void.
   \nStripe API docs: https://stripe.com/docs/api/credit_notes/void"
  ([stripe-client credit-note-id]
   (void-credit-note stripe-client credit-note-id {}))
  ([stripe-client credit-note-id opts]
   (request stripe-client :post (str stripe-credit-notes-endpoint "/" credit-note-id "/void") {} opts)))

(defn preview-credit-note
  "Get a preview of a credit note without creating it.
   \nStripe API docs: https://stripe.com/docs/api/credit_notes/preview"
  ([stripe-client params]
   (preview-credit-note stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get (str stripe-credit-notes-endpoint "/preview") params opts)))

(defn list-line-items
  "Lists all lines on a credit note.
   \nStripe API docs: https://stripe.com/docs/api/credit_notes/lines"
  ([stripe-client credit-note-id]
   (list-line-items stripe-client credit-note-id {}))
  ([stripe-client credit-note-id params]
   (list-line-items stripe-client credit-note-id params {}))
  ([stripe-client credit-note-id params opts]
   (request stripe-client :get (str stripe-credit-notes-endpoint "/" credit-note-id "/lines")
                params
                opts)))

(defn preview-lines
  "Get a preview of credit note lines without creating a credit note.
   \nStripe API docs: https://stripe.com/docs/api/credit_notes/preview_lines"
  ([stripe-client params]
   (preview-lines stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get (str stripe-credit-notes-endpoint "/preview/lines") params opts)))