(ns stripe-clojure.mock.sources-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.sources :as sources]))

(deftest create-source-test
  (testing "Create source"
    (let [params {:type "card" :currency "usd" :owner {:email "test@example.com"}}
          response (sources/create-source stripe-mock-client params)]
      (is (map? response))
      (is (= "source" (:object response)))
      (is (string? (:id response)))
      (is (= "card" (:type response)))
      (is (= "usd" (:currency response))))))

(deftest retrieve-source-test
  (testing "Retrieve source"
    (let [response (sources/retrieve-source stripe-mock-client "src_mock")]
      (is (map? response))
      (is (= "source" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :type))
      (is (string? (:type response))))))

(deftest update-source-test
  (testing "Update source"
    (let [params {:owner {:email "updated@example.com"}}
          response (sources/update-source stripe-mock-client "src_mock" params)]
      (is (map? response))
      (is (= "source" (:object response)))
      (is (string? (:id response)))))) 