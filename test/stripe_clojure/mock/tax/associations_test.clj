(ns stripe-clojure.mock.tax.associations-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.tax.associations :as associations]))

(deftest find-association-test
  (testing "Find tax association"
    (let [params {:payment_intent "pi_mock"}
          response (associations/find-association stripe-mock-client params)]
      (is (map? response))
      (is (= "tax.association" (:object response))))))
