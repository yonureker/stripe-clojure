(ns stripe-clojure.mock.billing.feedback-options-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.billing.feedback-options :as feedback-options]))

(deftest create-feedback-option-test
  (testing "Create a feedback option"
    (let [params {:description "Missing a feature I need"}
          response (feedback-options/create-feedback-option stripe-mock-client params)]
      (is (map? response))
      (is (= "billing.feedback_option" (:object response)))
      (is (string? (:id response))))))

(deftest retrieve-feedback-option-test
  (testing "Retrieve a feedback option"
    (let [response (feedback-options/retrieve-feedback-option stripe-mock-client "fbo_mock")]
      (is (map? response))
      (is (= "billing.feedback_option" (:object response)))
      (is (string? (:id response))))))

(deftest update-feedback-option-test
  (testing "Update a feedback option"
    (let [params {:description "Updated description"}
          response (feedback-options/update-feedback-option stripe-mock-client "fbo_mock" params)]
      (is (map? response))
      (is (= "billing.feedback_option" (:object response)))
      (is (string? (:id response))))))

(deftest list-feedback-options-test
  (testing "List feedback options"
    (let [response (feedback-options/list-feedback-options stripe-mock-client {:limit 5})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response))))))

(deftest deactivate-feedback-option-test
  (testing "Deactivate a feedback option"
    (let [response (feedback-options/deactivate-feedback-option stripe-mock-client "fbo_mock")]
      (is (map? response))
      (is (= "billing.feedback_option" (:object response)))
      (is (string? (:id response))))))
