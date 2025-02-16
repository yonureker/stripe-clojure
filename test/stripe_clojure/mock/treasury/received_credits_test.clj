(ns stripe-clojure.mock.treasury.received-credits-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.treasury.received-credits :as rc]))

(deftest ^:integration retrieve-received-credit-test
  (testing "Retrieve treasury received credit"
    (let [response (rc/retrieve-received-credit stripe-client "rc_mock")]
      (is (map? response))
      (is (= "treasury.received_credit" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :amount))
      (is (number? (:amount response))))))

(deftest ^:integration list-received-credits-test
  (testing "List treasury received credits"
    (let [response (rc/list-received-credits stripe-client {:limit 2 :financial_account "fa_mock"})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [credit (:data response)]
        (is (map? credit))
        (is (= "treasury.received_credit" (:object credit)))
        (is (string? (:id credit)))
        (is (contains? credit :amount))
        (is (number? (:amount credit))))))) 