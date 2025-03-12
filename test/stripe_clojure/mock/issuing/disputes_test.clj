(ns stripe-clojure.mock.issuing.disputes-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.issuing.disputes :as disputes]))

(deftest create-dispute-test
  (testing "Create an issuing dispute with required parameters"
    (let [params {:transaction "ipi_mock"
                  :evidence {:reason "other"
                             :other {:explanation "hello"}}}
          response (disputes/create-dispute stripe-mock-client params)]
      (is (map? response))
      (is (= "issuing.dispute" (:object response)))
      (is (string? (:id response))))))

(deftest retrieve-dispute-test
  (testing "Retrieve an issuing dispute using a dummy dispute id"
    (let [response (disputes/retrieve-dispute stripe-mock-client "id_mock")]
      (is (map? response))
      (is (= "issuing.dispute" (:object response)))
      (is (string? (:id response))))))

(deftest update-dispute-test
  (testing "Update an issuing dispute with update parameters"
    (let [response (disputes/update-dispute stripe-mock-client "id_mock" {:metadata {:updated "true"}})]
      (is (map? response))
      (is (= "issuing.dispute" (:object response)))
      (is (string? (:id response))))))

(deftest list-disputes-test
  (testing "List issuing disputes"
    (let [response (disputes/list-disputes stripe-mock-client {})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [d (:data response)]
        (is (map? d))
        (is (= "issuing.dispute" (:object d)))
        (is (string? (:id d)))))))

(deftest submit-dispute-test
  (testing "Submit an issuing dispute"
    (let [response (disputes/submit-dispute stripe-mock-client "id_mock")]
      (is (map? response))
      (is (= "issuing.dispute" (:object response)))
      (is (string? (:id response))))))