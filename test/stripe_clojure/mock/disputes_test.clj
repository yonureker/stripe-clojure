(ns stripe-clojure.mock.disputes-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.disputes :as disputes]))

(deftest retrieve-dispute-test
  (testing "Retrieve dispute"
    (let [response (disputes/retrieve-dispute stripe-mock-client "dp_mock")]
      (is (map? response))
      (is (= "dispute" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :charge)))))

(deftest update-dispute-test
  (testing "Update dispute"
    (let [params {:metadata {:order_id "1234567890"}}
          response (disputes/update-dispute stripe-mock-client "dp_mock" params)]
      (is (map? response))
      (is (= "dispute" (:object response)))
      (is (string? (:id response))))))

(deftest list-disputes-test
  (testing "List disputes"
    (let [response (disputes/list-disputes stripe-mock-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [d (:data response)]
        (is (map? d))
        (is (= "dispute" (:object d)))
        (is (string? (:id d)))
        (is (contains? d :charge))))))

(deftest close-dispute-test
  (testing "Close dispute"
    (let [response (disputes/close-dispute stripe-mock-client "dp_mock")]
      (is (map? response))
      (is (= "dispute" (:object response)))
      (is (string? (:id response)))))) 