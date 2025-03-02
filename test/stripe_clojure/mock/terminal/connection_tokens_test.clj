(ns stripe-clojure.mock.terminal.connection-tokens-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.terminal.connection-tokens :as ct]))

(deftest create-connection-token-test
  (testing "Create terminal connection token"
    (let [response (ct/create-connection-token stripe-mock-client {})]
      (is (map? response))
      (is (= "terminal.connection_token" (:object response)))
      (is (string? (:secret response))))))