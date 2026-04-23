(ns stripe-clojure.mock.issuing.settlements-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.issuing.settlements :as settlements]))

(deftest retrieve-settlement-test
  (testing "Retrieve an issuing settlement"
    (let [response (settlements/retrieve-settlement stripe-mock-client "ist_mock")]
      (is (map? response))
      (is (= "issuing.settlement" (:object response)))
      (is (string? (:id response))))))

(deftest update-settlement-test
  (testing "Update an issuing settlement"
    (let [params {:metadata {:key "value"}}
          response (settlements/update-settlement stripe-mock-client "ist_mock" params)]
      (is (map? response))
      (is (= "issuing.settlement" (:object response)))
      (is (string? (:id response))))))
