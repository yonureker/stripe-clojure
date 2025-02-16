(ns stripe-clojure.mock.identity.verification-sessions-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.identity.verification-sessions :as verification-sessions]))

(deftest ^:integration create-verification-session-test
  (testing "Create a verification session using stripe‑mock with required parameters"
    (let [params {:type "document"       ;; dummy required parameter
                  :return_url "https://example.com"}
          response (verification-sessions/create-verification-session stripe-client params)]
      (is (map? response))
      (is (= "identity.verification_session" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration retrieve-verification-session-test
  (testing "Retrieve a verification session using a dummy session id"
    (let [dummy-id "vsess_mock"
          response (verification-sessions/retrieve-verification-session stripe-client dummy-id)]
      (is (map? response))
      (is (= "identity.verification_session" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration update-verification-session-test
  (testing "Update a verification session using stripe‑mock"
    (let [dummy-id "vsess_mock"
          params {:metadata {:updated "true"}}
          response (verification-sessions/update-verification-session stripe-client dummy-id params)]
      (is (map? response))
      (is (= "identity.verification_session" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration list-verification-sessions-test
  (testing "List verification sessions using stripe‑mock"
    (let [response (verification-sessions/list-verification-sessions stripe-client {})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [session (:data response)]
        (is (map? session))
        (is (string? (:id session)))))))

(deftest ^:integration cancel-verification-session-test
  (testing "Cancel a verification session using stripe‑mock"
    (let [dummy-id "vsess_mock"
          response (verification-sessions/cancel-verification-session stripe-client dummy-id)]
      (is (map? response))
      (is (= "identity.verification_session" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration redact-verification-session-test
  (testing "Redact a verification session using stripe‑mock"
    (let [dummy-id "vsess_mock"
          response (verification-sessions/redact-verification-session stripe-client dummy-id)]
      (is (map? response))
      (is (= "identity.verification_session" (:object response)))
      (is (string? (:id response)))))) 