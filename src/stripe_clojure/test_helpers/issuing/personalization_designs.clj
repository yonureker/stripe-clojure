(ns stripe-clojure.test-helpers.issuing.personalization-designs
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-test-helpers-issuing-personalization-designs-endpoint (:test-helpers-issuing-personalization-designs config/stripe-endpoints))

(defn activate-personalization-design
  "Updates the status of a test mode personalization design to active.
   \nStripe API docs: https://stripe.com/docs/api/issuing/personalization_designs/activate_testmode"
  ([stripe-client personalization-design-id]
   (activate-personalization-design stripe-client personalization-design-id {}))
  ([stripe-client personalization-design-id opts]
   (request stripe-client :post
            (str stripe-test-helpers-issuing-personalization-designs-endpoint "/" personalization-design-id "/activate")
            {}
            opts)))

(defn deactivate-personalization-design
  "Updates the status of a test mode personalization design to inactive.
   \nStripe API docs: https://stripe.com/docs/api/issuing/personalization_designs/deactivate_testmode"
  ([stripe-client personalization-design-id]
   (deactivate-personalization-design stripe-client personalization-design-id {}))
  ([stripe-client personalization-design-id opts]
   (request stripe-client :post
            (str stripe-test-helpers-issuing-personalization-designs-endpoint "/" personalization-design-id "/deactivate")
            {}
            opts)))

(defn reject-personalization-design
  "Updates the status of a test mode personalization design to rejected.
   \nStripe API docs: https://stripe.com/docs/api/issuing/personalization_designs/reject_testmode"
  ([stripe-client personalization-design-id params]
   (reject-personalization-design stripe-client personalization-design-id params {}))
  ([stripe-client personalization-design-id params opts]
   (request stripe-client :post
            (str stripe-test-helpers-issuing-personalization-designs-endpoint "/" personalization-design-id "/reject")
            params
            opts)))
