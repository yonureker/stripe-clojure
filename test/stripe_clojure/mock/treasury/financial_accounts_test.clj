(ns stripe-clojure.mock.treasury.financial-accounts-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.treasury.financial-accounts :as fa]))

(deftest create-financial-account-test
  (testing "Create treasury financial account"
    (let [params {:supported_currencies ["usd"]}
          response (fa/create-financial-account stripe-mock-client params)]
      (is (map? response))
      (is (= "treasury.financial_account" (:object response)))
      (is (string? (:id response)))
      (is (vector? (:supported_currencies response))))))

(deftest retrieve-financial-account-test
  (testing "Retrieve treasury financial account"
    (let [response (fa/retrieve-financial-account stripe-mock-client "fa_mock")]
      (is (map? response))
      (is (= "treasury.financial_account" (:object response)))
      (is (string? (:id response))))))

(deftest update-financial-account-test
  (testing "Update treasury financial account"
    (let [params {:metadata {:order_id "mock_order_id"}}
          response (fa/update-financial-account stripe-mock-client "fa_mock" params)]
      (is (map? response))
      (is (= "treasury.financial_account" (:object response)))
      (is (string? (:id response))))))

(deftest list-financial-accounts-test
  (testing "List treasury financial accounts"
    (let [response (fa/list-financial-accounts stripe-mock-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [acct (:data response)]
        (is (map? acct))
        (is (= "treasury.financial_account" (:object acct)))
        (is (string? (:id acct)))))))

(deftest retrieve-features-test
  (testing "Retrieve treasury financial account features"
    (let [response (fa/retrieve-features stripe-mock-client "fa_mock")]
      (is (map? response))
      (is (= "treasury.financial_account_features" (:object response))))))

(deftest update-features-test
  (testing "Update treasury financial account features"
    (let [params {:card_issuing {:requested false}}
          response (fa/update-features stripe-mock-client "fa_mock" params)]
      (is (map? response))
      (is (= "treasury.financial_account_features" (:object response)))
      (is (= false (get-in response [:card_issuing :requested])))))) 