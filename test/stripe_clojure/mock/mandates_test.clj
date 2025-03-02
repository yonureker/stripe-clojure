(ns stripe-clojure.mock.mandates-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.mandates :as mandates]))

(deftest retrieve-mandate-test
  (testing "retrieve-mandate returns a valid mandate"
    (let [dummy-mandate-id "mand_mock_123"
          mandate (mandates/retrieve-mandate stripe-mock-client dummy-mandate-id)]
      (is (string? (:id mandate)) "Mandate should have a string id")
      (is (= "mandate" (:object mandate)) "Returned object should be 'mandate'")))) 