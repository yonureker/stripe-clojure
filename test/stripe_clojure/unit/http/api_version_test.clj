(ns stripe-clojure.unit.http.api-version-test
  (:require [clojure.test :refer [deftest is testing]]
            [stripe-clojure.http.api-version :as api-version]))

(deftest detect-version-test
  (testing "detects v1 from path"
    (is (= :v1 (api-version/detect-version "/v1/customers")))
    (is (= :v1 (api-version/detect-version "/v1/billing/meters")))
    (is (= :v1 (api-version/detect-version "/v1/checkout/sessions"))))

  (testing "detects v2 from path"
    (is (= :v2 (api-version/detect-version "/v2/core/events")))
    (is (= :v2 (api-version/detect-version "/v2/billing/meter_events")))
    (is (= :v2 (api-version/detect-version "/v2/core/event_destinations"))))

  (testing "defaults to v1 for unknown paths"
    (is (= :v1 (api-version/detect-version "/something/else")))
    (is (= :v1 (api-version/detect-version "/v3/future/endpoint"))))

  (testing "handles nil and empty safely"
    (is (= :v1 (api-version/detect-version nil)))
    (is (= :v1 (api-version/detect-version "")))))

(deftest v1?-test
  (testing "returns true for v1"
    (is (api-version/v1? :v1))
    (is (api-version/v1? api-version/V1)))

  (testing "returns false for v2"
    (is (not (api-version/v1? :v2)))
    (is (not (api-version/v1? api-version/V2)))))

(deftest v2?-test
  (testing "returns true for v2"
    (is (api-version/v2? :v2))
    (is (api-version/v2? api-version/V2)))

  (testing "returns false for v1"
    (is (not (api-version/v2? :v1)))
    (is (not (api-version/v2? api-version/V1)))))

(deftest version-constants-test
  (testing "V1 constant is :v1"
    (is (= :v1 api-version/V1)))

  (testing "V2 constant is :v2"
    (is (= :v2 api-version/V2))))
