(ns stripe-clojure.mock.sigma.saved-queries-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.sigma.saved-queries :as sq]))

;; NOTE: Skipped because the pinned stripe-mock spec does not include this endpoint
(deftest ^:skip-mock update-saved-query-test
  (testing "Update a sigma saved query"
    (let [params {:name "Updated Query" :sql "SELECT * FROM charges"}
          response (sq/update-saved-query stripe-mock-client "sqsav_mock" params)]
      (is (map? response))
      (is (= "sigma.sigma_api_query" (:object response)))
      (is (string? (:id response))))))
