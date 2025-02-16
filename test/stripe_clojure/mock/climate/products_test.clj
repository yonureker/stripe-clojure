(ns stripe-clojure.mock.climate.products-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.climate.products :as products]))

(deftest ^:integration retrieve-product-test
  (testing "Retrieve a climate product using a dummy product id"
    (let [dummy-id "prod_mock"
          response (products/retrieve-product stripe-client dummy-id)]
      (is (map? response))
      (is (= "climate.product" (:object response)))
      (when (:id response)
        (is (string? (:id response)))))))

(deftest ^:integration list-products-test
  (testing "List climate products using stripe‑mock"
    (let [response (products/list-products stripe-client)]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response))))))