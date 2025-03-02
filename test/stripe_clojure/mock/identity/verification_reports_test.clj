(ns stripe-clojure.mock.identity.verification-reports-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.identity.verification-reports :as verification-reports]))

(deftest retrieve-verification-report-test
  (testing "Retrieve an identity verification report using a dummy report id"
    (let [dummy-id "vr_mock"
          response (verification-reports/retrieve-verification-report stripe-mock-client dummy-id)]
      (is (map? response))
      (is (= "identity.verification_report" (:object response)))
      (is (string? (:id response))))))

(deftest list-verification-reports-test
  (testing "List identity verification reports using stripe‑mock"
    (let [response (verification-reports/list-verification-reports stripe-mock-client {})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [report (:data response)]
        (is (map? report))
        (is (string? (:id report))))))) 