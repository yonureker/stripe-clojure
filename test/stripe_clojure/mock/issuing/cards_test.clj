(ns stripe-clojure.mock.issuing.cards-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.issuing.cards :as cards]))

(deftest create-card-test
  (testing "Create an issuing card using stripe‑mock with required parameters"
    (let [params {:currency "usd"
                  :type "physical"
                  :cardholder "user_mock"}
          response (cards/create-card stripe-mock-client params)]
      (is (map? response))
      (is (= "issuing.card" (:object response)))
      (is (string? (:id response))))))

(deftest retrieve-card-test
  (testing "Retrieve an issuing card using a dummy card id"
    (let [dummy-id "card_mock"
          response (cards/retrieve-card stripe-mock-client dummy-id)]
      (is (map? response))
      (is (= "issuing.card" (:object response)))
      (is (string? (:id response))))))

(deftest update-card-test
  (testing "Update an issuing card using stripe‑mock with update parameters"
    (let [dummy-id "card_mock"
          params {:metadata {:updated "true"}}
          response (cards/update-card stripe-mock-client dummy-id params)]
      (is (map? response))
      (is (= "issuing.card" (:object response)))
      (is (string? (:id response))))))

(deftest list-cards-test
  (testing "List issuing cards using stripe‑mock"
    (let [response (cards/list-cards stripe-mock-client {})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [card (:data response)]
        (is (map? card))
        (is (string? (:id card)))))))