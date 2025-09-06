(ns stripe-clojure.mock.country-specs-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.country-specs :as country-specs]))

(deftest retrieve-country-spec-test
  (testing "Retrieve country spec"
    (let [response (country-specs/retrieve-country-spec stripe-mock-client "US")]
      (is (map? response))
      (is (= "country_spec" (:object response)))
      (is (string? (:id response)))
      (is (= "US" (:id response)))
      (is (contains? response :default_currency))
      (is (contains? response :supported_bank_account_currencies)))))

(deftest list-country-specs-test
  (testing "List country specs"
    (let [response (country-specs/list-country-specs stripe-mock-client)]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (is (boolean? (:has_more response))))))

(deftest list-country-specs-with-params-test
  (testing "List country specs with params"
    (let [params {:limit 10}
          response (country-specs/list-country-specs stripe-mock-client params)]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (is (boolean? (:has_more response))))))
