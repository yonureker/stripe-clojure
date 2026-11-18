(ns stripe-clojure.three-d-secure.authentications
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-three-d-secure-authentications-endpoint (config/stripe-endpoints :three-d-secure-authentications))

(defn create-authentication
  "Creates a 3D Secure authentication.
   \nStripe API docs: https://stripe.com/docs/api/three_d_secure/authentications/create"
  ([stripe-client params]
   (create-authentication stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-three-d-secure-authentications-endpoint params opts)))

(defn retrieve-authentication
  "Retrieves a 3D Secure authentication.
   \nStripe API docs: https://stripe.com/docs/api/three_d_secure/authentications/retrieve"
  ([stripe-client authentication-id]
   (retrieve-authentication stripe-client authentication-id {}))
  ([stripe-client authentication-id opts]
   (request stripe-client :get
            (str stripe-three-d-secure-authentications-endpoint "/" authentication-id)
            {}
            opts)))

(defn list-authentications
  "Lists all 3D Secure authentications.
   \nStripe API docs: https://stripe.com/docs/api/three_d_secure/authentications/list"
  ([stripe-client]
   (list-authentications stripe-client {} {}))
  ([stripe-client params]
   (list-authentications stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-three-d-secure-authentications-endpoint params opts)))

(defn cancel-authentication
  "Cancels a 3D Secure authentication.
   \nStripe API docs: https://stripe.com/docs/api/three_d_secure/authentications/cancel"
  ([stripe-client authentication-id]
   (cancel-authentication stripe-client authentication-id {} {}))
  ([stripe-client authentication-id params]
   (cancel-authentication stripe-client authentication-id params {}))
  ([stripe-client authentication-id params opts]
   (request stripe-client :post
            (str stripe-three-d-secure-authentications-endpoint "/" authentication-id "/cancel")
            params
            opts)))

(defn submit-authentication
  "Submits a 3D Secure authentication.
   \nStripe API docs: https://stripe.com/docs/api/three_d_secure/authentications/submit"
  ([stripe-client authentication-id]
   (submit-authentication stripe-client authentication-id {} {}))
  ([stripe-client authentication-id params]
   (submit-authentication stripe-client authentication-id params {}))
  ([stripe-client authentication-id params opts]
   (request stripe-client :post
            (str stripe-three-d-secure-authentications-endpoint "/" authentication-id "/submit")
            params
            opts)))
