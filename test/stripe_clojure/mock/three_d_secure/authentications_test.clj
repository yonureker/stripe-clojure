(ns stripe-clojure.mock.three-d-secure.authentications-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.three-d-secure.authentications :as authentications]))

(deftest create-authentication-test
  (testing "Create 3D Secure authentication"
    (let [params {:channel {:type "three_r_i"}
                  :message_category "payment_authentication"
                  :amount 1000
                  :currency "usd"
                  :payment_method "pm_mock"}
          response (authentications/create-authentication stripe-mock-client params)]
      (is (map? response))
      (is (= "three_d_secure.authentication" (:object response)))
      (is (string? (:id response))))))

(deftest retrieve-authentication-test
  (testing "Retrieve 3D Secure authentication"
    (let [response (authentications/retrieve-authentication stripe-mock-client "tdsa_mock")]
      (is (map? response))
      (is (= "three_d_secure.authentication" (:object response)))
      (is (string? (:id response))))))

(deftest list-authentications-test
  (testing "List 3D Secure authentications"
    (let [response (authentications/list-authentications stripe-mock-client {:limit 5})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response))))))

(deftest cancel-authentication-test
  (testing "Cancel 3D Secure authentication"
    (let [response (authentications/cancel-authentication stripe-mock-client "tdsa_mock")]
      (is (map? response))
      (is (= "three_d_secure.authentication" (:object response)))
      (is (string? (:id response))))))

(deftest submit-authentication-test
  (testing "Submit 3D Secure authentication"
    (let [response (authentications/submit-authentication stripe-mock-client "tdsa_mock")]
      (is (map? response))
      (is (= "three_d_secure.authentication" (:object response)))
      (is (string? (:id response))))))
