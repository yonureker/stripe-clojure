(ns stripe-clojure.test-util
  (:require [stripe-clojure.core :as stripe]))

(def stripe-client (stripe/init-stripe {:mock true}))

