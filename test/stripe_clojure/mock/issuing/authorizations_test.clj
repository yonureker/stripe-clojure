(ns stripe-clojure.mock.issuing.authorizations-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.issuing.authorizations :as auth]))

(deftest ^:integration retrieve-authorization-test
  (testing "Retrieve an issuing authorization using stripe‑mock"
    (let [dummy-id "auth_mock"
          response (auth/retrieve-authorization stripe-client dummy-id)]
      (is (map? response))
      (is (= "issuing.authorization" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration update-authorization-test
  (testing "Update an issuing authorization using stripe‑mock"
    (let [dummy-id "auth_mock"
          params {:metadata {:updated "true"}}
          response (auth/update-authorization stripe-client dummy-id params)]
      (is (map? response))
      (is (= "issuing.authorization" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration list-authorizations-test
  (testing "List issuing authorizations using stripe‑mock"
    (let [response (auth/list-authorizations stripe-client {})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [authorization (:data response)]
        (is (map? authorization))
        (is (string? (:id authorization)))))))

(deftest ^:integration approve-authorization-test
  (testing "Approve an issuing authorization using stripe‑mock"
    (let [dummy-id "auth_mock"
          response (auth/approve-authorization stripe-client dummy-id)]
      (is (map? response))
      (is (= "issuing.authorization" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration decline-authorization-test
  (testing "Decline an issuing authorization using stripe‑mock"
    (let [dummy-id "auth_mock"
          response (auth/decline-authorization stripe-client dummy-id)]
      (is (map? response))
      (is (= "issuing.authorization" (:object response)))
      (is (string? (:id response)))))) 