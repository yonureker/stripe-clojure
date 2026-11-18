(ns stripe-clojure.mock.tax.locations-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.tax.locations :as locations]))

(deftest create-location-test
  (testing "Create tax location"
    (let [params {:type "performance"
                  :address {:country "US"
                            :state "CA"
                            :postal_code "94103"}}
          response (locations/create-location stripe-mock-client params)]
      (is (map? response))
      (is (= "tax.location" (:object response)))
      (is (string? (:id response))))))

(deftest retrieve-location-test
  (testing "Retrieve tax location"
    (let [response (locations/retrieve-location stripe-mock-client "taxloc_mock")]
      (is (map? response))
      (is (= "tax.location" (:object response)))
      (is (string? (:id response))))))

(deftest list-locations-test
  (testing "List tax locations"
    (let [response (locations/list-locations stripe-mock-client {:type "performance" :limit 5})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response))))))
