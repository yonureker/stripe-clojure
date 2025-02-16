(ns stripe-clojure.mock.setup-attempts-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.setup-attempts :as setup-attempts]))

(deftest ^:integration list-setup-attempts-test
  (testing "List setup attempts"
    (let [response (setup-attempts/list-setup-attempts stripe-client {:limit 2 :setup_intent "seti_mock"})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [attempt (:data response)]
        (is (map? attempt))
        (is (= "setup_attempt" (:object attempt)))
        (is (string? (:id attempt)))
        (is (contains? attempt :created))
        (is (number? (:created attempt))))))) 