(ns stripe-clojure.charges
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-charges-endpoint (:charges config/stripe-endpoints))

(defn create-charge
  "Creates a new charge object.
   \nStripe API docs: https://stripe.com/docs/api/charges/create"
  ([stripe-client params]
   (create-charge stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-charges-endpoint params opts)))

(defn retrieve-charge
  "Retrieves the details of a charge that has previously been created.
   \nStripe API docs: https://stripe.com/docs/api/charges/retrieve" 
  ([stripe-client charge-id]
   (retrieve-charge stripe-client charge-id {}))
  ([stripe-client charge-id opts]
   (request stripe-client :get (str stripe-charges-endpoint "/" charge-id) {} opts)))

(defn update-charge
  "Updates the specified charge by setting the values of the parameters passed.
   \nStripe API docs: https://stripe.com/docs/api/charges/update"
  ([stripe-client charge-id params]
   (update-charge stripe-client charge-id params {}))
  ([stripe-client charge-id params opts]
   (request stripe-client :post (str stripe-charges-endpoint "/" charge-id) params opts)))

(defn capture-charge
  "Captures the payment of an existing, uncaptured charge.
   \nStripe API docs: https://stripe.com/docs/api/charges/capture"
  ([stripe-client charge-id]
   (capture-charge stripe-client charge-id {}))
  ([stripe-client charge-id params]
   (capture-charge stripe-client charge-id params {}))
  ([stripe-client charge-id params opts]
   (request stripe-client :post (str stripe-charges-endpoint "/" charge-id "/capture") params opts)))

(defn list-charges
  "Returns a list of charges you've previously created.
   \nStripe API docs: https://stripe.com/docs/api/charges/list"
  ([stripe-client]
   (list-charges stripe-client {}))
  ([stripe-client params]
   (list-charges stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-charges-endpoint params opts)))

(defn search-charges
  "Search for charges you've previously created using Stripe's Search Query Language.
   \nStripe API docs: https://stripe.com/docs/api/charges/search"
  ([stripe-client params]
   (search-charges stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get (str stripe-charges-endpoint "/search") params opts)))
