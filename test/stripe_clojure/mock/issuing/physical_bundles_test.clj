(ns stripe-clojure.mock.issuing.physical-bundles-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.issuing.physical-bundles :as pb]))

(deftest retrieve-physical-bundle-test
  (testing "Retrieve an issuing physical bundle"
    (let [response (pb/retrieve-physical-bundle stripe-mock-client "bundle_mock")]
      (is (map? response))
      (is (= "issuing.physical_bundle" (:object response)))
      (is (string? (:id response))))))

(deftest list-physical-bundles-test
  (testing "List all issuing physical bundles"
    (let [response (pb/list-physical-bundles stripe-mock-client {})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (is (contains? response :has_more))
      (doseq [bundle (:data response)]
        (is (map? bundle))
        (is (= "issuing.physical_bundle" (:object bundle)))
        (is (string? (:id bundle))))))) 