(ns stripe-clojure.mock.issuing.settlements-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.issuing.settlements :as settlements]))

(deftest retrieve-settlement-test
  (testing "Retrieve issuing settlement"
    (let [response (settlements/retrieve-settlement stripe-mock-client "isl_mock")]
      (is (map? response))
      (is (= "issuing.settlement" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :currency))
      (is (contains? response :net_total_amount)))))

(deftest list-settlements-test
  (testing "List issuing settlements"
    (let [response (settlements/list-settlements stripe-mock-client)]
      (is (map? response))
      ;; stripe-mock doesn't support list settlements endpoint
      (is (= "invalid_request_error" (:type response)))
      (is (string? (:message response))))))

(deftest list-settlements-with-params-test
  (testing "List issuing settlements with params"
    (let [params {:limit 10}
          response (settlements/list-settlements stripe-mock-client params)]
      (is (map? response))
      ;; stripe-mock doesn't support list settlements endpoint
      (is (= "invalid_request_error" (:type response)))
      (is (string? (:message response))))))
