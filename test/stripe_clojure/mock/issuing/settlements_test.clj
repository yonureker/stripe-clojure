(ns stripe-clojure.mock.issuing.settlements-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.issuing.settlements :as settlements]))

;; NOTE: Skipped because the pinned stripe-mock spec does not include this endpoint
(deftest ^:skip-mock retrieve-settlement-test
  (testing "Retrieve an issuing settlement"
    (let [response (settlements/retrieve-settlement stripe-mock-client "ist_mock")]
      (is (map? response))
      (is (= "issuing.settlement" (:object response)))
      (is (string? (:id response))))))

;; NOTE: Skipped because the pinned stripe-mock spec does not include this endpoint
(deftest ^:skip-mock update-settlement-test
  (testing "Update an issuing settlement"
    (let [params {:metadata {:key "value"}}
          response (settlements/update-settlement stripe-mock-client "ist_mock" params)]
      (is (map? response))
      (is (= "issuing.settlement" (:object response)))
      (is (string? (:id response))))))
