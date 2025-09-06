(ns stripe-clojure.accounts
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-accounts-endpoint (:accounts config/stripe-endpoints))

;; Core Account Operations
(defn create-account
  "Creates a new Stripe account.
   \nStripe API docs: https://stripe.com/docs/api/accounts/create"
  ([stripe-client account-payload]
   (create-account stripe-client account-payload {}))
  ([stripe-client account-payload opts]
   (request stripe-client :post stripe-accounts-endpoint account-payload opts)))

(defn retrieve-account
  "Retrieves a Stripe account.
   \nStripe API docs: https://stripe.com/docs/api/accounts/retrieve"
  ([stripe-client account-id]
   (retrieve-account stripe-client account-id {}))
  ([stripe-client account-id opts]
   (request stripe-client :get (str stripe-accounts-endpoint "/" account-id) {} opts)))

(defn update-account
  "Updates a Stripe account.
   \nStripe API docs: https://stripe.com/docs/api/accounts/update"
  ([stripe-client account-id params]
   (update-account stripe-client account-id params {}))
  ([stripe-client account-id params opts]
   (request stripe-client :post (str stripe-accounts-endpoint "/" account-id) params opts)))

(defn delete-account
  "Deletes a Stripe account.
   \nStripe API docs: https://stripe.com/docs/api/accounts/delete"
  ([stripe-client account-id]
   (delete-account stripe-client account-id {}))
  ([stripe-client account-id opts]
   (request stripe-client :delete 
                (str stripe-accounts-endpoint "/" account-id)
                nil 
                opts)))

(defn list-accounts
  "Lists all Stripe accounts.
   \nStripe API docs: https://stripe.com/docs/api/accounts/list"
  ([stripe-client]
   (list-accounts stripe-client {} {}))
  ([stripe-client params]
   (list-accounts stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-accounts-endpoint params opts)))

(defn reject-account
  "Rejects a Stripe account.
   \nStripe API docs: https://stripe.com/docs/api/accounts/reject"
  ([stripe-client account-id reason]
   (reject-account stripe-client account-id reason {}))
  ([stripe-client account-id reason opts]
   (request stripe-client :post 
                (str stripe-accounts-endpoint "/" account-id "/reject")
                {:reason reason}
                opts)))

;; Capabilities
(defn retrieve-capability
  "Retrieves an account capability.
   \nStripe API docs: https://stripe.com/docs/api/capabilities/retrieve"
  ([stripe-client account-id capability-id]
   (retrieve-capability stripe-client account-id capability-id {}))
  ([stripe-client account-id capability-id opts]
   (request stripe-client :get 
                (str stripe-accounts-endpoint "/" account-id "/capabilities/" capability-id)
                {}
                opts)))

(defn update-capability
  "Updates an account capability.
   \nStripe API docs: https://stripe.com/docs/api/capabilities/update"
  ([stripe-client account-id capability-id capability-payload]
   (update-capability stripe-client account-id capability-id capability-payload {}))
  ([stripe-client account-id capability-id capability-payload opts]
   (request stripe-client :post 
                (str stripe-accounts-endpoint "/" account-id "/capabilities/" capability-id)
                capability-payload
                opts)))

(defn list-capabilities
  "Lists all capabilities for an account.
   \nStripe API docs: https://stripe.com/docs/api/capabilities/list"
  ([stripe-client account-id]
   (list-capabilities stripe-client account-id {}))
  ([stripe-client account-id opts]
   (request stripe-client :get 
                (str stripe-accounts-endpoint "/" account-id "/capabilities")
                {}
                opts)))

;; External Accounts (Bank Accounts & Cards)
(defn create-external-account
  "Creates an external account for a Stripe account.
   \nStripe API docs: https://stripe.com/docs/api/external_account_bank_accounts/create"
  ([stripe-client account-id external-account-payload]
   (create-external-account stripe-client account-id external-account-payload {}))
  ([stripe-client account-id external-account-payload opts]
   (request stripe-client :post 
                (str stripe-accounts-endpoint "/" account-id "/external_accounts")
                external-account-payload
                opts)))

(defn retrieve-external-account
  "Retrieves an external account.
   \nStripe API docs: https://stripe.com/docs/api/external_account_bank_accounts/retrieve"
  ([stripe-client account-id external-account-id]
   (retrieve-external-account stripe-client account-id external-account-id {}))
  ([stripe-client account-id external-account-id opts]
   (request stripe-client :get 
                (str stripe-accounts-endpoint "/" account-id "/external_accounts/" external-account-id)
                {}
                opts)))

(defn update-external-account
  "Updates an external account.
   \nStripe API docs: https://stripe.com/docs/api/external_account_bank_accounts/update"
  ([stripe-client account-id external-account-id external-account-payload]
   (update-external-account stripe-client account-id external-account-id external-account-payload {}))
  ([stripe-client account-id external-account-id external-account-payload opts]
   (request stripe-client :post 
                (str stripe-accounts-endpoint "/" account-id "/external_accounts/" external-account-id)
                external-account-payload
                opts)))

(defn delete-external-account
  "Deletes an external account.
   \nStripe API docs: https://stripe.com/docs/api/external_account_bank_accounts/delete"
  ([stripe-client account-id external-account-id]
   (delete-external-account stripe-client account-id external-account-id {}))
  ([stripe-client account-id external-account-id opts]
   (request stripe-client :delete 
                (str stripe-accounts-endpoint "/" account-id "/external_accounts/" external-account-id)
                nil
                opts)))

(defn list-external-accounts
  "Lists all external accounts.
   \nStripe API docs: https://stripe.com/docs/api/external_account_bank_accounts/list"
  ([stripe-client account-id]
   (list-external-accounts stripe-client account-id {}))
  ([stripe-client account-id params]
   (list-external-accounts stripe-client account-id params {}))
  ([stripe-client account-id params opts]
   (request stripe-client :get 
                (str stripe-accounts-endpoint "/" account-id "/external_accounts")
                params
                opts)))

;; Bank Accounts (separate from external accounts)
(defn create-bank-account
  "Creates a bank account for an account.
   \nStripe API docs: https://stripe.com/docs/api/external_account_bank_accounts/create"
  ([stripe-client account-id bank-account-payload]
   (create-bank-account stripe-client account-id bank-account-payload {}))
  ([stripe-client account-id bank-account-payload opts]
   (request stripe-client :post 
                (str stripe-accounts-endpoint "/" account-id "/bank_accounts")
                bank-account-payload
                opts)))

(defn retrieve-bank-account
  "Retrieves a bank account.
   \nStripe API docs: https://stripe.com/docs/api/external_account_bank_accounts/retrieve"
  ([stripe-client account-id bank-account-id]
   (retrieve-bank-account stripe-client account-id bank-account-id {}))
  ([stripe-client account-id bank-account-id opts]
   (request stripe-client :get 
                (str stripe-accounts-endpoint "/" account-id "/bank_accounts/" bank-account-id)
                {}
                opts)))

(defn update-bank-account
  "Updates a bank account.
   \nStripe API docs: https://stripe.com/docs/api/external_account_bank_accounts/update"
  ([stripe-client account-id bank-account-id bank-account-payload]
   (update-bank-account stripe-client account-id bank-account-id bank-account-payload {}))
  ([stripe-client account-id bank-account-id bank-account-payload opts]
   (request stripe-client :post 
                (str stripe-accounts-endpoint "/" account-id "/bank_accounts/" bank-account-id)
                bank-account-payload
                opts)))

(defn delete-bank-account
  "Deletes a bank account.
   \nStripe API docs: https://stripe.com/docs/api/external_account_bank_accounts/delete"
  ([stripe-client account-id bank-account-id]
   (delete-bank-account stripe-client account-id bank-account-id {}))
  ([stripe-client account-id bank-account-id opts]
   (request stripe-client :delete 
                (str stripe-accounts-endpoint "/" account-id "/bank_accounts/" bank-account-id)
                nil
                opts)))

(defn list-bank-accounts
  "Lists all bank accounts for an account.
   \nStripe API docs: https://stripe.com/docs/api/external_account_bank_accounts/list"
  ([stripe-client account-id]
   (list-bank-accounts stripe-client account-id {}))
  ([stripe-client account-id params]
   (list-bank-accounts stripe-client account-id params {}))
  ([stripe-client account-id params opts]
   (request stripe-client :get 
                (str stripe-accounts-endpoint "/" account-id "/bank_accounts")
                params
                opts)))

(defn verify-bank-account
  "Verifies a bank account.
   \nStripe API docs: https://stripe.com/docs/api/external_account_bank_accounts/verify"
  ([stripe-client account-id bank-account-id params]
   (verify-bank-account stripe-client account-id bank-account-id params {}))
  ([stripe-client account-id bank-account-id params opts]
   (request stripe-client :post 
                (str stripe-accounts-endpoint "/" account-id "/bank_accounts/" bank-account-id "/verify")
                params
                opts)))

;; Login Links
(defn create-login-link
  "Creates a login link for an Express account.
   \nStripe API docs: https://stripe.com/docs/api/login_links/create"
  ([stripe-client account-id]
   (create-login-link stripe-client account-id {}))
  ([stripe-client account-id params]
   (create-login-link stripe-client account-id params {}))
  ([stripe-client account-id params opts]
   (request stripe-client :post 
                (str stripe-accounts-endpoint "/" account-id "/login_links")
                params
                opts)))

;; Persons
(defn create-person
  "Creates a person for an account.
   \nStripe API docs: https://stripe.com/docs/api/persons/create"
  ([stripe-client account-id person-payload]
   (create-person stripe-client account-id person-payload {}))
  ([stripe-client account-id person-payload opts]
   (request stripe-client :post 
                (str stripe-accounts-endpoint "/" account-id "/persons")
                person-payload
                opts)))

(defn retrieve-person
  "Retrieves an existing person.
   \nStripe API docs: https://stripe.com/docs/api/persons/retrieve"
  ([stripe-client account-id person-id]
   (retrieve-person stripe-client account-id person-id {}))
  ([stripe-client account-id person-id opts]
   (request stripe-client :get 
                (str stripe-accounts-endpoint "/" account-id "/persons/" person-id)
                {}
                opts)))

(defn update-person
  "Updates an existing person.
   \nStripe API docs: https://stripe.com/docs/api/persons/update"
  ([stripe-client account-id person-id person-payload]
   (update-person stripe-client account-id person-id person-payload {}))
  ([stripe-client account-id person-id person-payload opts]
   (request stripe-client :post 
                (str stripe-accounts-endpoint "/" account-id "/persons/" person-id)
                person-payload
                opts)))

(defn delete-person
  "Deletes an existing person.
   \nStripe API docs: https://stripe.com/docs/api/persons/delete"
  ([stripe-client account-id person-id]
   (delete-person stripe-client account-id person-id {}))
  ([stripe-client account-id person-id opts]
   (request stripe-client :delete 
                (str stripe-accounts-endpoint "/" account-id "/persons/" person-id)
                nil
                opts)))

(defn list-persons
  "Lists all persons.
   \nStripe API docs: https://stripe.com/docs/api/persons/list"
  ([stripe-client account-id]
   (list-persons stripe-client account-id {}))
  ([stripe-client account-id params]
   (list-persons stripe-client account-id params {}))
  ([stripe-client account-id params opts]
   (request stripe-client :get 
                (str stripe-accounts-endpoint "/" account-id "/persons")
                params
                opts)))

;; People (different from persons)
(defn create-people
  "Creates a person for an account.
   \nStripe API docs: https://stripe.com/docs/api/persons/create"
  ([stripe-client account-id person-payload]
   (create-people stripe-client account-id person-payload {}))
  ([stripe-client account-id person-payload opts]
   (request stripe-client :post 
                (str stripe-accounts-endpoint "/" account-id "/people")
                person-payload
                opts)))

(defn retrieve-people
  "Retrieves an existing person.
   \nStripe API docs: https://stripe.com/docs/api/persons/retrieve"
  ([stripe-client account-id person-id]
   (retrieve-people stripe-client account-id person-id {}))
  ([stripe-client account-id person-id opts]
   (request stripe-client :get 
                (str stripe-accounts-endpoint "/" account-id "/people/" person-id)
                {}
                opts)))

(defn update-people
  "Updates an existing person.
   \nStripe API docs: https://stripe.com/docs/api/persons/update"
  ([stripe-client account-id person-id person-payload]
   (update-people stripe-client account-id person-id person-payload {}))
  ([stripe-client account-id person-id person-payload opts]
   (request stripe-client :post 
                (str stripe-accounts-endpoint "/" account-id "/people/" person-id)
                person-payload
                opts)))

(defn delete-people
  "Deletes an existing person.
   \nStripe API docs: https://stripe.com/docs/api/persons/delete"
  ([stripe-client account-id person-id]
   (delete-people stripe-client account-id person-id {}))
  ([stripe-client account-id person-id opts]
   (request stripe-client :delete 
                (str stripe-accounts-endpoint "/" account-id "/people/" person-id)
                nil
                opts)))

(defn list-people
  "Lists all people.
   \nStripe API docs: https://stripe.com/docs/api/persons/list"
  ([stripe-client account-id]
   (list-people stripe-client account-id {}))
  ([stripe-client account-id params]
   (list-people stripe-client account-id params {}))
  ([stripe-client account-id params opts]
   (request stripe-client :get 
                (str stripe-accounts-endpoint "/" account-id "/people")
                params
                opts)))