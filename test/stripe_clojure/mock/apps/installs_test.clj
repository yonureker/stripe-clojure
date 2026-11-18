(ns stripe-clojure.mock.apps.installs-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.apps.installs :as installs]))

(deftest create-install-test
  (testing "Create install"
    (let [response (installs/create-install stripe-mock-client {:app "com.example.mock-app"})]
      (is (map? response))
      (is (= "apps.install" (:object response)))
      (is (string? (:id response))))))

(deftest retrieve-install-test
  (testing "Retrieve install"
    (let [response (installs/retrieve-install stripe-mock-client "appins_mock")]
      (is (map? response))
      (is (= "apps.install" (:object response)))
      (is (string? (:id response))))))

(deftest update-install-test
  (testing "Update install"
    (let [response (installs/update-install stripe-mock-client "appins_mock" {})]
      (is (map? response))
      (is (= "apps.install" (:object response)))
      (is (string? (:id response))))))

(deftest list-installs-test
  (testing "List installs"
    (let [response (installs/list-installs stripe-mock-client {:limit 5})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response))))))

(deftest remove-install-test
  (testing "Remove install"
    (let [response (installs/remove-install stripe-mock-client "appins_mock")]
      (is (map? response))
      (is (= "apps.install" (:object response)))
      (is (string? (:id response))))))
