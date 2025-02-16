(ns stripe-clojure.mock.promotion-codes-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.promotion-codes :as promo]))

(deftest ^:integration create-promotion-code-test
  (testing "Create promotion code"
    (let [params {:coupon "cpn_mock" :code "PROMO50"}
          response (promo/create-promotion-code stripe-client params)]
      (is (map? response))
      (is (= "promotion_code" (:object response)))
      (is (string? (:id response)))
      (is (= "PROMO50" (:code response)))
      (is (boolean? (:active response))))))

(deftest ^:integration retrieve-promotion-code-test
  (testing "Retrieve promotion code"
    (let [response (promo/retrieve-promotion-code stripe-client "promo_mock")]
      (is (map? response))
      (is (= "promotion_code" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :code))
      (is (string? (:code response)))
      (is (contains? response :active))
      (is (boolean? (:active response))))))

(deftest ^:integration update-promotion-code-test
  (testing "Update promotion code"
    (let [params {}
          response (promo/update-promotion-code stripe-client "promo_mock" params)]
      (is (map? response))
      (is (= "promotion_code" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration list-promotion-codes-test
  (testing "List promotion codes"
    (let [response (promo/list-promotion-codes stripe-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [promo-code (:data response)]
        (is (map? promo-code))
        (is (= "promotion_code" (:object promo-code)))
        (is (string? (:id promo-code))))))) 