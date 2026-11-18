(ns stripe-clojure.mock.product-catalog.trial-offers-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.product-catalog.trial-offers :as trial-offers]))

(deftest create-trial-offer-test
  (testing "Create trial offer"
    (let [params {:price "price_mock"
                  :duration {:type "relative"
                             :relative {:iterations 1}}
                  :end_behavior {:transition {:price "price_mock_paid"}}}
          response (trial-offers/create-trial-offer stripe-mock-client params)]
      (is (map? response))
      (is (= "product_catalog.trial_offer" (:object response)))
      (is (string? (:id response))))))

(deftest retrieve-trial-offer-test
  (testing "Retrieve trial offer"
    (let [response (trial-offers/retrieve-trial-offer stripe-mock-client "tro_mock")]
      (is (map? response))
      (is (= "product_catalog.trial_offer" (:object response)))
      (is (string? (:id response))))))

(deftest update-trial-offer-test
  (testing "Update trial offer"
    (let [response (trial-offers/update-trial-offer stripe-mock-client "tro_mock" {:active false})]
      (is (map? response))
      (is (= "product_catalog.trial_offer" (:object response)))
      (is (string? (:id response))))))

(deftest list-trial-offers-test
  (testing "List trial offers"
    (let [response (trial-offers/list-trial-offers stripe-mock-client {:limit 5})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response))))))
