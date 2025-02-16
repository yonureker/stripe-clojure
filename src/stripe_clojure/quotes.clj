(ns stripe-clojure.quotes
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-quotes-endpoint (config/stripe-endpoints :quotes))

(defn create-quote
  "Creates a quote.
   \nStripe API docs: https://stripe.com/docs/api/quotes/create"
  ([stripe-client params]
   (create-quote stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-quotes-endpoint params opts)))

(defn retrieve-quote
  "Retrieves a quote.
   \nStripe API docs: https://stripe.com/docs/api/quotes/retrieve"
  ([stripe-client quote-id]
   (retrieve-quote stripe-client quote-id {}))
  ([stripe-client quote-id opts]
   (request stripe-client :get (str stripe-quotes-endpoint "/" quote-id) {} opts)))

(defn update-quote
  "Updates a quote.
   \nStripe API docs: https://stripe.com/docs/api/quotes/update"
  ([stripe-client quote-id params]
   (update-quote stripe-client quote-id params {}))
  ([stripe-client quote-id params opts]
   (request stripe-client :post (str stripe-quotes-endpoint "/" quote-id) params opts)))

(defn list-quotes
  "Lists all quotes.
   \nStripe API docs: https://stripe.com/docs/api/quotes/list"
  ([stripe-client]
   (list-quotes stripe-client {}))
  ([stripe-client params]
   (list-quotes stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-quotes-endpoint params opts)))

(defn finalize-quote
  "Finalizes a quote.
   \nStripe API docs: https://stripe.com/docs/api/quotes/finalize"
  ([stripe-client quote-id]
   (finalize-quote stripe-client quote-id {}))
  ([stripe-client quote-id opts]
   (request stripe-client :post (str stripe-quotes-endpoint "/" quote-id "/finalize") {} opts)))

(defn accept-quote
  "Accepts a quote.
   \nStripe API docs: https://stripe.com/docs/api/quotes/accept"
  ([stripe-client quote-id]
   (accept-quote stripe-client quote-id {}))
  ([stripe-client quote-id opts]
   (request stripe-client :post (str stripe-quotes-endpoint "/" quote-id "/accept") {} opts)))

(defn cancel-quote
  "Cancels a quote.
   \nStripe API docs: https://stripe.com/docs/api/quotes/cancel"
  ([stripe-client quote-id]
   (cancel-quote stripe-client quote-id {}))
  ([stripe-client quote-id opts]
   (request stripe-client :post (str stripe-quotes-endpoint "/" quote-id "/cancel") {} opts)))

(defn list-line-items
  "Lists all line items for a quote.
   \nStripe API docs: https://stripe.com/docs/api/quotes/line_items"
  ([stripe-client quote-id]
   (list-line-items stripe-client quote-id {}))
  ([stripe-client quote-id params]
   (list-line-items stripe-client quote-id params {}))
  ([stripe-client quote-id params opts]
   (request stripe-client :get (str stripe-quotes-endpoint "/" quote-id "/line_items") params opts)))

(defn list-computed-upfront-line-items
  "Lists all computed upfront line items for a quote.
   \nStripe API docs: https://stripe.com/docs/api/quotes/computed_upfront_line_items"
  ([stripe-client quote-id]
   (list-computed-upfront-line-items stripe-client quote-id {}))
  ([stripe-client quote-id params]
   (list-computed-upfront-line-items stripe-client quote-id params {}))
  ([stripe-client quote-id params opts]
   (request stripe-client :get 
                (str stripe-quotes-endpoint "/" quote-id "/computed_upfront_line_items")
                params
                opts)))

(defn get-pdf
  "Retrieves the PDF for a quote.
   \nStripe API docs: https://stripe.com/docs/api/quotes/pdf"
  ([stripe-client quote-id]
   (get-pdf stripe-client quote-id {}))
  ([stripe-client quote-id opts]
   (request stripe-client :get (str stripe-quotes-endpoint "/" quote-id "/pdf") {} opts)))