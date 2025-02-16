(ns stripe-clojure.mock.shipping-rates-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.shipping-rates :as shipping-rates]))

(deftest ^:integration create-shipping-rate-test
  (testing "Create shipping rate"
    (let [params {:display_name "Test Shipping"
                  :type "fixed_amount"
                  :fixed_amount {:amount 500 :currency "usd"}}
          response (shipping-rates/create-shipping-rate stripe-client params)]
      (is (map? response))
      (is (= "shipping_rate" (:object response)))
      (is (string? (:id response)))
      (is (= "Test Shipping" (:display_name response)))
      (is (= "fixed_amount" (:type response)))
      (is (= 500 (get-in response [:fixed_amount :amount])))
      (is (= "usd" (get-in response [:fixed_amount :currency]))))))

(deftest ^:integration retrieve-shipping-rate-test
  (testing "Retrieve shipping rate"
    (let [response (shipping-rates/retrieve-shipping-rate stripe-client "sr_mock")]
      (is (map? response))
      (is (= "shipping_rate" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :display_name))
      (is (contains? response :fixed_amount))
      (is (contains? response :type)))))

(deftest ^:integration update-shipping-rate-test
  (testing "Update shipping rate"
    (let [params {:metadata {:test "test"}}
          response (shipping-rates/update-shipping-rate stripe-client "sr_mock" params)]
      (is (map? response))
      (is (= "shipping_rate" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration list-shipping-rates-test
  (testing "List shipping rates"
    (let [response (shipping-rates/list-shipping-rates stripe-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [sr (:data response)]
        (is (map? sr))
        (is (= "shipping_rate" (:object sr)))
        (is (string? (:id sr)))
        (is (contains? sr :display_name))
        (is (string? (:display_name sr))))))) 