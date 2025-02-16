(ns stripe-clojure.terminal.readers
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-terminal-readers-endpoint (config/stripe-endpoints :terminal-readers))

(defn create-reader
  "Creates a new terminal reader.
   \nStripe API docs: https://stripe.com/docs/api/terminal/readers/create"
  ([stripe-client params]
   (create-reader stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-terminal-readers-endpoint params opts)))

(defn retrieve-reader
  "Retrieves a terminal reader.
   \nStripe API docs: https://stripe.com/docs/api/terminal/readers/retrieve"
  ([stripe-client reader-id]
   (retrieve-reader stripe-client reader-id {}))
  ([stripe-client reader-id opts]
   (request stripe-client :get
                (str stripe-terminal-readers-endpoint "/" reader-id)
                {}
                opts)))

(defn update-reader
  "Updates a terminal reader.
   \nStripe API docs: https://stripe.com/docs/api/terminal/readers/update"
  ([stripe-client reader-id params]
   (update-reader stripe-client reader-id params {}))
  ([stripe-client reader-id params opts]
   (request stripe-client :post
                (str stripe-terminal-readers-endpoint "/" reader-id)
                params
                opts)))

(defn list-readers
  "Lists all terminal readers.
   \nStripe API docs: https://stripe.com/docs/api/terminal/readers/list"
  ([stripe-client]
   (list-readers stripe-client {} {}))
  ([stripe-client params]
   (list-readers stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-terminal-readers-endpoint params opts)))

(defn delete-reader
  "Deletes a terminal reader.
   \nStripe API docs: https://stripe.com/docs/api/terminal/readers/delete"
  ([stripe-client reader-id]
   (delete-reader stripe-client reader-id {}))
  ([stripe-client reader-id opts]
   (request stripe-client :delete
                (str stripe-terminal-readers-endpoint "/" reader-id)
                nil
                opts)))

(defn cancel-action
  "Cancels the current reader action.
   \nStripe API docs: https://stripe.com/docs/api/terminal/readers/cancel_action"
  ([stripe-client reader-id]
   (cancel-action stripe-client reader-id {}))
  ([stripe-client reader-id opts]
   (request stripe-client :post
                (str stripe-terminal-readers-endpoint "/" reader-id "/cancel_action")
                {}
                opts)))

(defn process-payment-intent
  "Processes a payment intent on the reader.
   \nStripe API docs: https://stripe.com/docs/api/terminal/readers/process_payment_intent"
  ([stripe-client reader-id params]
   (process-payment-intent stripe-client reader-id params {}))
  ([stripe-client reader-id params opts]
   (request stripe-client :post
                (str stripe-terminal-readers-endpoint "/" reader-id "/process_payment_intent")
                params
                opts)))

(defn process-setup-intent
  "Processes a setup intent on the reader.
   \nStripe API docs: https://stripe.com/docs/api/terminal/readers/process_setup_intent"
  ([stripe-client reader-id params]
   (process-setup-intent stripe-client reader-id params {}))
  ([stripe-client reader-id params opts]
   (request stripe-client :post
                (str stripe-terminal-readers-endpoint "/" reader-id "/process_setup_intent")
                params
                opts)))

(defn refund-payment
  "Refunds a payment on the reader.
   \nStripe API docs: https://stripe.com/docs/api/terminal/readers/refund_payment"
  ([stripe-client reader-id params]
   (refund-payment stripe-client reader-id params {}))
  ([stripe-client reader-id params opts]
   (request stripe-client :post
                (str stripe-terminal-readers-endpoint "/" reader-id "/refund_payment")
                params
                opts)))

(defn set-reader-display
  "Sets the reader display.
   \nStripe API docs: https://stripe.com/docs/api/terminal/readers/set_reader_display"
  ([stripe-client reader-id params]
   (set-reader-display stripe-client reader-id params {}))
  ([stripe-client reader-id params opts]
   (request stripe-client :post
                (str stripe-terminal-readers-endpoint "/" reader-id "/set_reader_display")
                params
                opts))) 