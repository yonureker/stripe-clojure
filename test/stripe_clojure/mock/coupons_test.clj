(ns stripe-clojure.mock.coupons-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.coupons :as coupons]))

(deftest ^:integration create-coupon-test
  (testing "Create coupon"
    (let [params {:duration "once" :percent_off 25}
          response (coupons/create-coupon stripe-client params)]
      (is (map? response))
      (is (= "coupon" (:object response)))
      (is (string? (:id response)))
      (is (= "once" (:duration response)))
      (is (= 25 (:percent_off response)))
      (is (boolean? (:livemode response))))))

(deftest ^:integration retrieve-coupon-test
  (testing "Retrieve coupon"
    (let [response (coupons/retrieve-coupon stripe-client "cpn_mock")]
      (is (map? response))
      (is (= "coupon" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :duration))
      (is (string? (:duration response))))))

(deftest ^:integration update-coupon-test
  (testing "Update coupon"
    (let [params {:metadata {:foo "bar"}}
          response (coupons/update-coupon stripe-client "cpn_mock" params)]
      (is (map? response))
      (is (= "coupon" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration delete-coupon-test
  (testing "Delete coupon"
    (let [response (coupons/delete-coupon stripe-client "cpn_mock")]
      (is (map? response))
      (is (= "coupon" (:object response)))
      (is (string? (:id response)))
      (is (true? (:deleted response))))))

(deftest ^:integration list-coupons-test
  (testing "List coupons"
    (let [response (coupons/list-coupons stripe-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [c (:data response)]
        (is (map? c))
        (is (= "coupon" (:object c)))
        (is (string? (:id c)))
        (is (contains? c :duration))
        (is (string? (:duration c)))))))
