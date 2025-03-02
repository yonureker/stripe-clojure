(ns stripe-clojure.mock.terminal.locations-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.terminal.locations :as loc]))

(deftest create-location-test
  (testing "Create terminal location"
    (let [params {:display_name "Test Location"
                  :address {:line1 "1234 Main Street"
                            :city "San Francisco"
                            :postal_code "94111"
                            :state "CA"
                            :country "US"}}
          response (loc/create-location stripe-mock-client params)]
      (is (map? response))
      (is (= "terminal.location" (:object response)))
      (is (string? (:id response)))
      (is (= "Test Location" (:display_name response)))
      (is (= {:line1 "1234 Main Street"
              :city "San Francisco"
              :postal_code "94111"
              :state "CA"
              :country "US"
              :line2 nil} (:address response))))))

(deftest retrieve-location-test
  (testing "Retrieve terminal location"
    (let [response (loc/retrieve-location stripe-mock-client "loc_mock")]
      (is (map? response))
      (is (= "terminal.location" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :display_name))
      (is (string? (:display_name response))))))

(deftest update-location-test
  (testing "Update terminal location"
    (let [params {:display_name "Updated Location"}
          response (loc/update-location stripe-mock-client "loc_mock" params)]
      (is (map? response))
      (is (= "terminal.location" (:object response)))
      (is (string? (:id response))))))

(deftest list-locations-test
  (testing "List terminal locations"
    (let [response (loc/list-locations stripe-mock-client {})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [location (:data response)]
        (is (map? location))
        (is (= "terminal.location" (:object location)))
        (is (string? (:id location)))
        (is (contains? location :display_name))
        (is (string? (:display_name location)))))))

(deftest delete-location-test
  (testing "Delete terminal location"
    (let [response (loc/delete-location stripe-mock-client "loc_mock")]
      (is (map? response))
      (is (= "terminal.location" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :deleted))
      (is (true? (:deleted response)))))) 