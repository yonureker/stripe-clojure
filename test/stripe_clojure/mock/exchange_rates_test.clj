(ns stripe-clojure.mock.exchange-rates-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.exchange-rates :as exchange-rates]))

(deftest retrieve-exchange-rate-test
  (testing "Retrieve exchange rate"
    (let [response (exchange-rates/retrieve-exchange-rate stripe-mock-client "usd")]
      (is (map? response))
      (is (= "exchange_rate" (:object response)))
      (is (string? (:id response)))
      (is (= "usd" (:id response)))
      (is (contains? response :rates))
      (is (map? (:rates response))))))

(deftest list-exchange-rates-test
  (testing "List exchange rates"
    (let [response (exchange-rates/list-exchange-rates stripe-mock-client)]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (is (boolean? (:has_more response))))))

(deftest list-exchange-rates-with-params-test
  (testing "List exchange rates with params"
    (let [params {:limit 10}
          response (exchange-rates/list-exchange-rates stripe-mock-client params)]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (is (boolean? (:has_more response))))))