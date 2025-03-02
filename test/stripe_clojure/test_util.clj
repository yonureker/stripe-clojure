(ns stripe-clojure.test-util
  (:require [stripe-clojure.core :as stripe]
            [stripe-clojure.config :as config]))

;; For mock tests, we use the stripe-mock server
(def stripe-mock-client (stripe/init-stripe {:mock true}))

;; For integration tests, we need to use a real Stripe API test key
(def stripe-integration-client (stripe/init-stripe {:api-key (or (System/getenv "STRIPE_TEST_API_KEY") "test_key")
                                                    :api-version config/base-api-version}))
