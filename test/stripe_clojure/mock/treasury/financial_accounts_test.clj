(ns stripe-clojure.mock.treasury.financial-accounts-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.treasury.financial-accounts :as fa]))

(deftest ^:integration create-financial-account-test
  (testing "Create treasury financial account"
    (let [params {:supported_currencies ["usd"]}
          response (fa/create-financial-account stripe-client params)]
      (is (map? response))
      (is (= "treasury.financial_account" (:object response)))
      (is (string? (:id response)))
      (is (vector? (:supported_currencies response))))))

(deftest ^:integration retrieve-financial-account-test
  (testing "Retrieve treasury financial account"
    (let [response (fa/retrieve-financial-account stripe-client "fa_mock")]
      (is (map? response))
      (is (= "treasury.financial_account" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration update-financial-account-test
  (testing "Update treasury financial account"
    (let [params {:metadata {:order_id "mock_order_id"}}
          response (fa/update-financial-account stripe-client "fa_mock" params)]
      (is (map? response))
      (is (= "treasury.financial_account" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration list-financial-accounts-test
  (testing "List treasury financial accounts"
    (let [response (fa/list-financial-accounts stripe-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [acct (:data response)]
        (is (map? acct))
        (is (= "treasury.financial_account" (:object acct)))
        (is (string? (:id acct)))))))

(deftest ^:integration retrieve-features-test
  (testing "Retrieve treasury financial account features"
    (let [response (fa/retrieve-features stripe-client "fa_mock")]
      (is (map? response))
      (is (= "treasury.financial_account_features" (:object response))))))

(deftest ^:integration update-features-test
  (testing "Update treasury financial account features"
    (let [params {:card_issuing {:requested false}}
          response (fa/update-features stripe-client "fa_mock" params)]
      (is (map? response))
      (is (= "treasury.financial_account_features" (:object response)))
      (is (= false (get-in response [:card_issuing :requested])))))) 