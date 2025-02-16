(ns stripe-clojure.customers
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-customers-endpoint (:customers config/stripe-endpoints))

;; Core Customer Operations
(defn create-customer
  "Creates a new customer.
   \nStripe API docs: https://stripe.com/docs/api/customers/create"
  ([stripe-client params]
   (create-customer stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-customers-endpoint params opts)))

(defn retrieve-customer
  "Retrieves a customer by ID.
   \nStripe API docs: https://stripe.com/docs/api/customers/retrieve"
  ([stripe-client customer-id]
   (retrieve-customer stripe-client customer-id {}))
  ([stripe-client customer-id opts]
   (request stripe-client :get (str stripe-customers-endpoint "/" customer-id) {} opts)))

(defn update-customer
  "Updates a customer by ID.
   \nStripe API docs: https://stripe.com/docs/api/customers/update"
  ([stripe-client customer-id params]
   (update-customer stripe-client customer-id params {}))
  ([stripe-client customer-id params opts]
   (request stripe-client :post (str stripe-customers-endpoint "/" customer-id) params opts)))

(defn delete-customer
  "Deletes a customer by ID.
   \nStripe API docs: https://stripe.com/docs/api/customers/delete"
  ([stripe-client customer-id]
   (delete-customer stripe-client customer-id {}))
  ([stripe-client customer-id opts]
   (request stripe-client :delete (str stripe-customers-endpoint "/" customer-id) nil opts)))

(defn list-customers
  "Lists all customers.
   \nStripe API docs: https://stripe.com/docs/api/customers/list"
  ([stripe-client]
   (list-customers stripe-client {}))
  ([stripe-client params]
   (list-customers stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-customers-endpoint params opts)))

(defn search-customers
  "Searches for customers.
   \nStripe API docs: https://stripe.com/docs/api/customers/search"
  ([stripe-client query]
   (search-customers stripe-client query {}))
  ([stripe-client params opts]
   (request stripe-client :get
                (str stripe-customers-endpoint "/search")
                params
                opts)))

;; Balance Transactions
(defn list-balance-transactions
  "Lists all balance transactions for a customer.
   \nStripe API docs: https://stripe.com/docs/api/customer_balance_transactions/list"
  ([stripe-client customer-id]
   (list-balance-transactions stripe-client customer-id {}))
  ([stripe-client customer-id params]
   (list-balance-transactions stripe-client customer-id params {}))
  ([stripe-client customer-id params opts]
   (request stripe-client :get 
                (str stripe-customers-endpoint "/" customer-id "/balance_transactions")
                params
                opts)))

(defn create-balance-transaction
  "Creates a balance transaction for a customer.
   \nStripe API docs: https://stripe.com/docs/api/customer_balance_transactions/create"
  ([stripe-client customer-id params]
   (create-balance-transaction stripe-client customer-id params {}))
  ([stripe-client customer-id params opts]
   (request stripe-client :post
                (str stripe-customers-endpoint "/" customer-id "/balance_transactions")
                params
                opts)))

(defn retrieve-balance-transaction
  "Retrieves a customer balance transaction.
   \nStripe API docs: https://stripe.com/docs/api/customer_balance_transactions/retrieve"
  ([stripe-client customer-id transaction-id]
   (retrieve-balance-transaction stripe-client customer-id transaction-id {}))
  ([stripe-client customer-id transaction-id opts]
   (request stripe-client :get
                (str stripe-customers-endpoint "/" customer-id "/balance_transactions/" transaction-id)
                {}
                opts)))

(defn update-balance-transaction
  "Updates a customer balance transaction.
   \nStripe API docs: https://stripe.com/docs/api/customer_balance_transactions/update"
  ([stripe-client customer-id transaction-id params]
   (update-balance-transaction stripe-client customer-id transaction-id params {}))
  ([stripe-client customer-id transaction-id params opts]
   (request stripe-client :post
                (str stripe-customers-endpoint "/" customer-id "/balance_transactions/" transaction-id)
                params
                opts)))

;; Cash Balance
(defn retrieve-cash-balance
  "Retrieves a customer's cash balance.
   \nStripe API docs: https://stripe.com/docs/api/customers/cash_balance"
  ([stripe-client customer-id]
   (retrieve-cash-balance stripe-client customer-id {}))
  ([stripe-client customer-id opts]
   (request stripe-client :get
                (str stripe-customers-endpoint "/" customer-id "/cash_balance")
                {}
                opts)))

(defn update-cash-balance
  "Changes the settings on a customer’s cash balance.
   \nStripe API docs: https://stripe.com/docs/api/cash_balance/update"
  ([stripe-client customer-id params]
   (update-cash-balance stripe-client customer-id params {}))
  ([stripe-client customer-id params opts]
   (request stripe-client :post
                (str stripe-customers-endpoint "/" customer-id "/cash_balance")
                params
                opts)))

(defn list-cash-balance-transactions
  "Returns a list of transactions that modified the customer's cash balance.
   \nStripe API docs: https://stripe.com/docs/api/customers/cash_balance_transactions/list"
  ([stripe-client customer-id]
   (list-cash-balance-transactions stripe-client customer-id {}))
  ([stripe-client customer-id params]
   (list-cash-balance-transactions stripe-client customer-id params {}))
  ([stripe-client customer-id params opts]
   (request stripe-client :get
                (str stripe-customers-endpoint "/" customer-id "/cash_balance_transactions")
                params
                opts)))

(defn retrieve-cash-balance-transaction
  "Retrieves a specific cash balance transaction.
   \nStripe API docs: https://stripe.com/docs/api/customers/cash_balance_transactions/retrieve"
  ([stripe-client customer-id transaction-id]
   (retrieve-cash-balance-transaction stripe-client customer-id transaction-id {}))
  ([stripe-client customer-id transaction-id opts]
   (request stripe-client :get
                (str stripe-customers-endpoint "/" customer-id "/cash_balance_transactions/" transaction-id)
                {}
                opts)))

;; Payment Methods
(defn list-payment-methods
  "Lists a customer's payment methods of a specific type.
   \nStripe API docs: https://stripe.com/docs/api/payment_methods/customer_list"
  ([stripe-client customer-id]
   (list-payment-methods stripe-client customer-id {}))
  ([stripe-client customer-id params]
   (list-payment-methods stripe-client customer-id params {}))
  ([stripe-client customer-id params opts]
   (request stripe-client :get
                (str stripe-customers-endpoint "/" customer-id "/payment_methods")
                params
                opts)))

;; Sources
(defn list-sources
  "Lists all sources for a customer.
   \nStripe API docs: https://stripe.com/docs/api/sources/list"
  ([stripe-client customer-id]
   (list-sources stripe-client customer-id {}))
  ([stripe-client customer-id params]
   (list-sources stripe-client customer-id params {}))
  ([stripe-client customer-id params opts]
   (request stripe-client :get
                (str stripe-customers-endpoint "/" customer-id "/sources")
                params
                opts)))

(defn create-source
  "Creates a new source for a customer.
   \nStripe API docs: https://stripe.com/docs/api/sources/create"
  ([stripe-client customer-id params]
   (create-source stripe-client customer-id params {}))
  ([stripe-client customer-id params opts]
   (request stripe-client :post
                (str stripe-customers-endpoint "/" customer-id "/sources")
                params
                opts)))

(defn update-source
  "Updates an existing source for a customer.
   \nStripe API docs: https://stripe.com/docs/api/sources/update"
  ([stripe-client customer-id source-id params]
   (update-source stripe-client customer-id source-id params {}))
  ([stripe-client customer-id source-id params opts]
   (request stripe-client :post
                (str stripe-customers-endpoint "/" customer-id "/sources/" source-id)
                params
                opts)))

(defn delete-source
  "Deletes (detaches) a source from a customer.
   \nStripe API docs: https://docs.stripe.com/api/sources/detach"
  ([stripe-client customer-id source-id]
   (delete-source stripe-client customer-id source-id {}))
  ([stripe-client customer-id source-id opts]
   (request stripe-client :delete
                (str stripe-customers-endpoint "/" customer-id "/sources/" source-id)
                nil
                opts)))

;; Tax IDs
(defn list-tax-ids
  "Lists all tax IDs for a customer.
   \nStripe API docs: https://stripe.com/docs/api/customer_tax_ids/list"
  ([stripe-client customer-id]
   (list-tax-ids stripe-client customer-id {}))
  ([stripe-client customer-id params]
   (list-tax-ids stripe-client customer-id params {}))
  ([stripe-client customer-id params opts]
   (request stripe-client :get
                (str stripe-customers-endpoint "/" customer-id "/tax_ids")
                params
                opts)))

;; Bank Account Verification
(defn verify-source
  "Verifies a customer's bank account using the provided amounts.
   \nStripe API docs: https://stripe.com/docs/api/customer_bank_accounts/verify"
  ([stripe-client customer-id bank-account-id params]
   (verify-source stripe-client customer-id bank-account-id params {}))
  ([stripe-client customer-id bank-account-id params opts]
   (request stripe-client :post
            (str stripe-customers-endpoint "/" customer-id "/sources/" bank-account-id "/verify")
            params
            opts)))

;; Cards
(defn retrieve-card
  "Retrieves a customer's card.
   \nStripe API docs: https://stripe.com/docs/api/cards/retrieve"
  ([stripe-client customer-id card-id]
   (retrieve-card stripe-client customer-id card-id {}))
  ([stripe-client customer-id card-id opts]
   (request stripe-client :get
                (str stripe-customers-endpoint "/" customer-id "/cards/" card-id)
                {}
                opts)))

(defn list-cards
  "Lists all cards attached to a customer.
   \nStripe API docs: https://stripe.com/docs/api/cards/list"
  ([stripe-client customer-id]
   (list-cards stripe-client customer-id {}))
  ([stripe-client customer-id params]
   (list-cards stripe-client customer-id params {}))
  ([stripe-client customer-id params opts]
   (request stripe-client :get
                (str stripe-customers-endpoint "/" customer-id "/cards")
                params
                opts)))

(defn create-tax-id
  "Creates a tax ID for a customer.
   \nStripe API docs: https://stripe.com/docs/api/customers/create_tax_id"
  ([stripe-client customer-id params]
   (create-tax-id stripe-client customer-id params {}))
  ([stripe-client customer-id params opts]
   (request stripe-client :post
                (str stripe-customers-endpoint "/" customer-id "/tax_ids")
                params
                opts)))

(defn delete-discount
  "Deletes a customer's discount.
   \nStripe API docs: https://stripe.com/docs/api/customers/delete_discount"
  ([stripe-client customer-id]
   (delete-discount stripe-client customer-id {}))
  ([stripe-client customer-id opts]
   (request stripe-client :delete
                (str stripe-customers-endpoint "/" customer-id "/discount")
                nil
                opts)))

(defn delete-tax-id
  "Deletes a customer's tax ID.
   \nStripe API docs: https://stripe.com/docs/api/customers/delete_tax_id"
  ([stripe-client customer-id tax-id]
   (delete-tax-id stripe-client customer-id tax-id {}))
  ([stripe-client customer-id tax-id opts]
   (request stripe-client :delete
                (str stripe-customers-endpoint "/" customer-id "/tax_ids/" tax-id)
                nil
                opts)))

(defn retrieve-payment-method
  "Retrieves a customer's payment method.
   \nStripe API docs: https://stripe.com/docs/api/customers/retrieve_payment_method"
  ([stripe-client customer-id payment-method-id]
   (retrieve-payment-method stripe-client customer-id payment-method-id {}))
  ([stripe-client customer-id payment-method-id opts]
   (request stripe-client :get
                (str stripe-customers-endpoint "/" customer-id "/payment_methods/" payment-method-id)
                {}
                opts)))

(defn retrieve-source
  "Retrieves a customer's source.
   \nStripe API docs: https://stripe.com/docs/api/customers/retrieve_source"
  ([stripe-client customer-id source-id]
   (retrieve-source stripe-client customer-id source-id {}))
  ([stripe-client customer-id source-id opts]
   (request stripe-client :get
                (str stripe-customers-endpoint "/" customer-id "/sources/" source-id)
                {}
                opts)))

(defn retrieve-tax-id
  "Retrieves a customer's tax ID.
   \nStripe API docs: https://stripe.com/docs/api/customers/retrieve_tax_id"
  ([stripe-client customer-id tax-id]
   (retrieve-tax-id stripe-client customer-id tax-id {}))
  ([stripe-client customer-id tax-id opts]
   (request stripe-client :get
                (str stripe-customers-endpoint "/" customer-id "/tax_ids/" tax-id)
                {}
                opts)))