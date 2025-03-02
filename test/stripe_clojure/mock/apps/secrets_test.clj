(ns stripe-clojure.mock.apps.secrets-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.apps.secrets :as secrets]))

(deftest set-secret-test
  (testing "Set secret"
    (let [response (secrets/set-secret
                    stripe-mock-client
                    {:name "secret_test" :payload "s3cr3t"
                     :scope {:type "account"}})]
      (is (map? response))
      (is (string? (:id response)))
      (is (= "secret_test" (:name response))))))

(deftest list-secrets-test
  (testing "List secrets"
    (let [response (secrets/list-secrets
                    stripe-mock-client
                    {:scope {:type "account"}})
          data (:data response)]
      (is (map? response))
      (is (vector? data))
      (doseq [secret data]
        (is (map? secret))
        (is (string? (:id secret)))))))

(deftest delete-secret-test
  (testing "Delete secret"
    (let [response (secrets/delete-secret
                    stripe-mock-client
                    {:name "secret_test"
                     :scope {:type "account"}})]
      (is (map? response))
      (is (string? (:id response))))))

(deftest find-secret-test
  (testing "Find secret"
    (let [response (secrets/find-secret
                    stripe-mock-client
                    {:name "secret_test"
                     :scope {:type "account"}})]
      (is (map? response))
      (is (string? (:id response)))
      (is (= "test-secret" (:name response))))))
