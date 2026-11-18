(ns stripe-clojure.mock.test-helpers.issuing.personalization-designs-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.test-helpers.issuing.personalization-designs :as test-personalization-designs]))

(deftest activate-personalization-design-test
  (testing "Updates the status of a test mode personalization design to active"
    (let [response (test-personalization-designs/activate-personalization-design stripe-mock-client "pd_mock")]
      (is (map? response))
      (is (= "issuing.personalization_design" (:object response)))
      (is (string? (:id response))))))

(deftest deactivate-personalization-design-test
  (testing "Updates the status of a test mode personalization design to inactive"
    (let [response (test-personalization-designs/deactivate-personalization-design stripe-mock-client "pd_mock")]
      (is (map? response))
      (is (= "issuing.personalization_design" (:object response)))
      (is (string? (:id response))))))

(deftest reject-personalization-design-test
  (testing "Updates the status of a test mode personalization design to rejected"
    (let [response (test-personalization-designs/reject-personalization-design stripe-mock-client "pd_mock" {:rejection_reasons {:card_logo ["other"]}})]
      (is (map? response))
      (is (= "issuing.personalization_design" (:object response)))
      (is (string? (:id response))))))
