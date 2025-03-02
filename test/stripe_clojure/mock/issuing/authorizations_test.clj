(ns stripe-clojure.mock.issuing.authorizations-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.issuing.authorizations :as auth]))

(deftest retrieve-authorization-test
  (testing "Retrieve an issuing authorization using stripe‑mock"
    (let [dummy-id "auth_mock"
          response (auth/retrieve-authorization stripe-mock-client dummy-id)]
      (is (map? response))
      (is (= "issuing.authorization" (:object response)))
      (is (string? (:id response))))))

(deftest update-authorization-test
  (testing "Update an issuing authorization using stripe‑mock"
    (let [dummy-id "auth_mock"
          params {:metadata {:updated "true"}}
          response (auth/update-authorization stripe-mock-client dummy-id params)]
      (is (map? response))
      (is (= "issuing.authorization" (:object response)))
      (is (string? (:id response))))))

(deftest list-authorizations-test
  (testing "List issuing authorizations using stripe‑mock"
    (let [response (auth/list-authorizations stripe-mock-client {})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [authorization (:data response)]
        (is (map? authorization))
        (is (string? (:id authorization)))))))

(deftest approve-authorization-test
  (testing "Approve an issuing authorization using stripe‑mock"
    (let [dummy-id "auth_mock"
          response (auth/approve-authorization stripe-mock-client dummy-id)]
      (is (map? response))
      (is (= "issuing.authorization" (:object response)))
      (is (string? (:id response))))))

(deftest decline-authorization-test
  (testing "Decline an issuing authorization using stripe‑mock"
    (let [dummy-id "auth_mock"
          response (auth/decline-authorization stripe-mock-client dummy-id)]
      (is (map? response))
      (is (= "issuing.authorization" (:object response)))
      (is (string? (:id response)))))) 