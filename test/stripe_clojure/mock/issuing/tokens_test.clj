(ns stripe-clojure.mock.issuing.tokens-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.issuing.tokens :as tokens]))

(deftest ^:integration retrieve-token-test
  (testing "Retrieve an issuing token"
    (let [response (tokens/retrieve-token stripe-client "token_mock")]
      (is (map? response))
      (is (= "issuing.token" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :card)))))

(deftest ^:integration update-token-test
  (testing "Update an issuing token"
    (let [params {:status "active"}
          response (tokens/update-token stripe-client "token_mock" params)]
      (is (map? response))
      (is (= "issuing.token" (:object response)))
      (is (string? (:id response)))
      (is (= "active" (get-in response [:status]))))))

(deftest ^:integration list-tokens-test
  (testing "List issuing tokens"
    (let [response (tokens/list-tokens stripe-client {:card "mock_card"})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (is (contains? response :has_more))
      (doseq [token (:data response)]
        (is (map? token))
        (is (= "issuing.token" (:object token)))
        (is (string? (:id token))))))) 