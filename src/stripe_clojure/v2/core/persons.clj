(ns stripe-clojure.v2.core.persons
  "Stripe V2 Core Persons API.

   Persons represent individuals associated with an Account for identity
   verification and compliance purposes.

   Docs: https://docs.stripe.com/api/v2/core/persons"
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def ^:private accounts-endpoint (:v2-core-accounts config/stripe-v2-endpoints))

(defn- persons-endpoint [account-id]
  (str accounts-endpoint "/" account-id "/persons"))

(defn create-person
  "Creates a new Person for an Account.

   Docs: https://docs.stripe.com/api/v2/core/persons/create"
  ([stripe-client account-id params]
   (create-person stripe-client account-id params {}))
  ([stripe-client account-id params opts]
   (request stripe-client :post (persons-endpoint account-id) params opts)))

(defn retrieve-person
  "Retrieves a Person by ID.

   Docs: https://docs.stripe.com/api/v2/core/persons/retrieve"
  ([stripe-client account-id person-id]
   (retrieve-person stripe-client account-id person-id {}))
  ([stripe-client account-id person-id opts]
   (request stripe-client :get (str (persons-endpoint account-id) "/" person-id) {} opts)))

(defn update-person
  "Updates a Person.

   Docs: https://docs.stripe.com/api/v2/core/persons/update"
  ([stripe-client account-id person-id params]
   (update-person stripe-client account-id person-id params {}))
  ([stripe-client account-id person-id params opts]
   (request stripe-client :post (str (persons-endpoint account-id) "/" person-id) params opts)))

(defn list-persons
  "Lists all Persons for an Account.

   Docs: https://docs.stripe.com/api/v2/core/persons/list"
  ([stripe-client account-id]
   (list-persons stripe-client account-id {}))
  ([stripe-client account-id params]
   (list-persons stripe-client account-id params {}))
  ([stripe-client account-id params opts]
   (request stripe-client :get (persons-endpoint account-id) params opts)))

(defn delete-person
  "Deletes a Person.

   Docs: https://docs.stripe.com/api/v2/core/persons/delete"
  ([stripe-client account-id person-id]
   (delete-person stripe-client account-id person-id {}))
  ([stripe-client account-id person-id opts]
   (request stripe-client :delete (str (persons-endpoint account-id) "/" person-id) {} opts)))
