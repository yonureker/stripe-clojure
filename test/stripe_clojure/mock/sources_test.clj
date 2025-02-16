(ns stripe-clojure.mock.sources-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.sources :as sources]))

(deftest ^:integration create-source-test
  (testing "Create source"
    (let [params {:type "card" :currency "usd" :owner {:email "test@example.com"}}
          response (sources/create-source stripe-client params)]
      (is (map? response))
      (is (= "source" (:object response)))
      (is (string? (:id response)))
      (is (= "card" (:type response)))
      (is (= "usd" (:currency response))))))

(deftest ^:integration retrieve-source-test
  (testing "Retrieve source"
    (let [response (sources/retrieve-source stripe-client "src_mock")]
      (is (map? response))
      (is (= "source" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :type))
      (is (string? (:type response))))))

(deftest ^:integration update-source-test
  (testing "Update source"
    (let [params {:owner {:email "updated@example.com"}}
          response (sources/update-source stripe-client "src_mock" params)]
      (is (map? response))
      (is (= "source" (:object response)))
      (is (string? (:id response)))))) 