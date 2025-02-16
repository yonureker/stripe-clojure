(ns stripe-clojure.mock.issuing.cards-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.issuing.cards :as cards]))

(deftest ^:integration create-card-test
  (testing "Create an issuing card using stripe‑mock with required parameters"
    (let [params {:currency "usd"
                  :type "physical"
                  :cardholder "user_mock"}
          response (cards/create-card stripe-client params)]
      (is (map? response))
      (is (= "issuing.card" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration retrieve-card-test
  (testing "Retrieve an issuing card using a dummy card id"
    (let [dummy-id "card_mock"
          response (cards/retrieve-card stripe-client dummy-id)]
      (is (map? response))
      (is (= "issuing.card" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration update-card-test
  (testing "Update an issuing card using stripe‑mock with update parameters"
    (let [dummy-id "card_mock"
          params {:metadata {:updated "true"}}
          response (cards/update-card stripe-client dummy-id params)]
      (is (map? response))
      (is (= "issuing.card" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration list-cards-test
  (testing "List issuing cards using stripe‑mock"
    (let [response (cards/list-cards stripe-client {})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [card (:data response)]
        (is (map? card))
        (is (string? (:id card)))))))