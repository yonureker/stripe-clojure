(ns stripe-clojure.mock.entitlements.features-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.entitlements.features :as features]))

(deftest create-feature-test
  (testing "Create feature using stripe‑mock with required parameters"
    (let [params {:name "Test Feature"
                  :lookup_key "my_super_awesome_feature"}
          response (features/create-feature stripe-mock-client params)]
      (is (map? response))
      (is (= "entitlements.feature" (:object response)))
      (is (= "Test Feature" (:name response)))
      ;; Instead of checking for a specific id, just check that an id is present (if provided) and is a string.
      (when (:id response)
        (is (string? (:id response)))))))

(deftest update-feature-test
  (testing "Update feature using stripe‑mock"
    (let [dummy-id "feat_mock"
          params {:active false}
          response (features/update-feature stripe-mock-client dummy-id params)]
      (is (map? response))
      (is (= "entitlements.feature" (:object response)))
      ;; Optionally check that the updated field is echoed back.
      (is (= false (:active response)))
      (when (:id response)
        (is (string? (:id response)))))))

(deftest list-features-test
  (testing "List features using stripe‑mock"
    (let [response (features/list-features stripe-mock-client)]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))))) 