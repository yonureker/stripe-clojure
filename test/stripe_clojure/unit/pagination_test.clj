(ns stripe-clojure.unit.pagination-test
  (:require [clojure.test :refer [deftest is testing]]
            [stripe-clojure.core :as stripe]
            [stripe-clojure.config :as config]
            [stripe-clojure.customers :as customers]))

;; Create a Stripe instance using your test API key from config.
(def instance (stripe/init-stripe {:api-key (get config/api-keys :test)}))

(deftest integration-auto-pagination-test
  (testing "Auto-pagination returns a lazy sequence of customer maps"
    (let [customers-seq (customers/list-customers instance {:limit 1}
                                                    {:auto-paginate? true
                                                     :full-response? false})
          fetched (doall (take 3 customers-seq))]
      (is (seq? customers-seq) "Expected a lazy sequence for auto-pagination")
      (is (every? map? fetched) "Every element in the sequence should be a customer map")
      (is (>= (count fetched) 1) "Should fetch at least one customer"))))

(deftest integration-non-auto-pagination-test
  (testing "Non-auto-pagination returns a single page response"
    (let [response (customers/list-customers instance {:limit 10}
                                               {:auto-paginate? false
                                                :full-response? false})]
      (is (coll? response) "Response should be a collection (i.e. a single page of customers)")
      (is (<= (count response) 10)
          "Response count should be at most the limit provided (10)"))))

(deftest integration-auto-pagination-full-response-forced-test
  (testing "When auto-pagination is enabled, the full-response flag is forced to be false"
    (let [customers-seq (customers/list-customers instance {:limit 1}
                                                    {:auto-paginate? true
                                                     :full-response? true}) ; even though true is supplied…
          fetched (doall (take 2 customers-seq))]
      (is (seq? customers-seq)
          "Expected a lazy sequence even if full-response? is set to true due to auto-pagination")
      (is (every? map? fetched)
          "Each item in the lazy sequence should be a customer map"))))

(deftest integration-full-response-single-page-test
  (testing "Full-response returns a complete response envelope when auto-pagination is disabled"
    (let [response (customers/list-customers instance {:limit 10}
                                               {:auto-paginate? false
                                                :full-response? true})]
      (is (map? response) "Expected a merged response map when full-response is requested")
      (is (contains? (response :body) :data) "Full response should contain a :data key")
      (is (contains? (response :body) :has_more) "Full response should include a :has_more key"))))