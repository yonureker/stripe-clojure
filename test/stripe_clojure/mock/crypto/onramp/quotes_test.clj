(ns stripe-clojure.mock.crypto.onramp.quotes-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.crypto.onramp.quotes :as quotes]))

(deftest ^:integration list-quotes-test
  (testing "List crypto onramp quotes using stripe‑mock"
    (let [response (quotes/list-quotes stripe-client)]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response))))))