(ns stripe-clojure.mock.issuing.disputes-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.issuing.disputes :as disputes]))

(deftest ^:integration create-dispute-test
  (testing "Create an issuing dispute with required parameters"
    (let [params {:transaction "ipi_mock"
                  :evidence {:reason "other"
                             :other {:explanation "hello"}}}
          response (disputes/create-dispute stripe-client params)]
      (is (map? response))
      (is (= "issuing.dispute" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration retrieve-dispute-test
  (testing "Retrieve an issuing dispute using a dummy dispute id"
    (let [response (disputes/retrieve-dispute stripe-client "id_mock")]
      (is (map? response))
      (is (= "issuing.dispute" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration update-dispute-test
  (testing "Update an issuing dispute with update parameters"
    (let [response (disputes/update-dispute stripe-client "id_mock" {:metadata {:updated "true"}})]
      (is (map? response))
      (is (= "issuing.dispute" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration list-disputes-test
  (testing "List issuing disputes"
    (let [response (disputes/list-disputes stripe-client {})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [d (:data response)]
        (is (map? d))
        (is (= "issuing.dispute" (:object d)))
        (is (string? (:id d)))))))

(deftest ^:integration submit-dispute-test
  (testing "Submit an issuing dispute"
    (let [response (disputes/submit-dispute stripe-client "id_mock")]
      (is (map? response))
      (is (= "issuing.dispute" (:object response)))
      (is (string? (:id response))))))