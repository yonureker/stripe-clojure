(ns stripe-clojure.mock.ephemeral-keys-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.ephemeral-keys :as ek]))

(deftest ^:integration create-ephemeral-key-test
  (testing "Create ephemeral key"
    (let [params {:customer "cus_mock"}
          response (ek/create-ephemeral-key stripe-client params)]
      (is (map? response))
      (is (= "ephemeral_key" (:object response)))
      (is (string? (:id response)))
      (is (number? (:created response)))
      (is (number? (:expires response))))))

(deftest ^:integration delete-ephemeral-key-test
  (testing "Delete ephemeral key"
    (let [response (ek/delete-ephemeral-key stripe-client "ephkey_mock")]
      (is (map? response))
      (is (= "ephemeral_key" (:object response)))
      (is (string? (:id response))))))
