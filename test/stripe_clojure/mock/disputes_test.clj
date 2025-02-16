(ns stripe-clojure.mock.disputes-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.disputes :as disputes]))

(deftest ^:integration retrieve-dispute-test
  (testing "Retrieve dispute"
    (let [response (disputes/retrieve-dispute stripe-client "dp_mock")]
      (is (map? response))
      (is (= "dispute" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :charge)))))

(deftest ^:integration update-dispute-test
  (testing "Update dispute"
    (let [params {:metadata {:order_id "1234567890"}}
          response (disputes/update-dispute stripe-client "dp_mock" params)]
      (is (map? response))
      (is (= "dispute" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration list-disputes-test
  (testing "List disputes"
    (let [response (disputes/list-disputes stripe-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [d (:data response)]
        (is (map? d))
        (is (= "dispute" (:object d)))
        (is (string? (:id d)))
        (is (contains? d :charge))))))

(deftest ^:integration close-dispute-test
  (testing "Close dispute"
    (let [response (disputes/close-dispute stripe-client "dp_mock")]
      (is (map? response))
      (is (= "dispute" (:object response)))
      (is (string? (:id response)))))) 