(ns stripe-clojure.mock.accounts-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.accounts :as accounts]))

(deftest ^:integration create-account-test
  (testing "Create account"
    (let [params {:email "acct@example.com" :business_type "individual"
                  :controller {:stripe_dashboard {:type "express"}}}
          response (accounts/create-account stripe-client params)]
      (is (map? response))
      (is (= "account" (:object response)))
      (is (string? (:id response)))
      (is (= "acct@example.com" (:email response)))
      (is (= "individual" (:business_type response))))))

(deftest ^:integration retrieve-account-test
  (testing "Retrieve account"
    (let [response (accounts/retrieve-account stripe-client "acct_mock")]
      (is (map? response))
      (is (= "account" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :email)))))

(deftest ^:integration update-account-test
  (testing "Update account"
    (let [params {:email "new@example.com"}
          response (accounts/update-account stripe-client "acct_mock" params)]
      (is (map? response))
      (is (= "account" (:object response)))
      (is (string? (:id response)))
      (is (= "new@example.com" (:email response))))))

(deftest ^:integration delete-account-test
  (testing "Delete account"
    (let [response (accounts/delete-account stripe-client "acct_mock")]
      (is (map? response))
      (is (= "account" (:object response)))
      (is (string? (:id response)))
      (is (true? (:deleted response))))))

(deftest ^:integration list-accounts-test
  (testing "List accounts"
    (let [response (accounts/list-accounts stripe-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [acct (:data response)]
        (is (map? acct))
        (is (= "account" (:object acct)))
        (is (string? (:id acct)))
        (is (contains? acct :email))))))

(deftest ^:integration reject-account-test
  (testing "Reject account"
    (let [response (accounts/reject-account stripe-client "acct_mock" "fraud")]
      (is (map? response))
      (is (= "account" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration retrieve-capability-test
  (testing "Retrieve capability"
    (let [response (accounts/retrieve-capability stripe-client "acct_mock" "cap_mock")]
      (is (map? response))
      (is (= "capability" (:object response)))
      (is (string? (:id response)))
      (is (boolean? (:requested response))))))

(deftest ^:integration update-capability-test
  (testing "Update capability"
    (let [params {:requested false}
          response (accounts/update-capability stripe-client "acct_mock" "cap_mock" params)]
      (is (map? response))
      (is (= "capability" (:object response)))
      (is (string? (:id response)))
      (is (= false (:requested response))))))

(deftest ^:integration list-capabilities-test
  (testing "List capabilities"
    (let [response (accounts/list-capabilities stripe-client "acct_mock" {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [cap (:data response)]
        (is (map? cap))
        (is (= "capability" (:object cap)))
        (is (string? (:id cap)))
        (is (contains? cap :requested))))))

(deftest ^:integration create-external-account-test
  (testing "Create external account"
    (let [payload {:external_account "ba_mock"}
          response (accounts/create-external-account stripe-client "acct_mock" payload)]
      (is (map? response))
      (is (= "bank_account" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration retrieve-external-account-test
  (testing "Retrieve external account"
    (let [response (accounts/retrieve-external-account stripe-client "acct_mock" "ba_mock")]
      (is (map? response))
      (is (= "bank_account" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :bank_name)))))

(deftest ^:integration update-external-account-test
  (testing "Update external account"
    (let [payload {:metadata {:bank_name "New Bank"}}
          response (accounts/update-external-account stripe-client "acct_mock" "ba_mock" payload)]
      (is (map? response))
      (is (= "bank_account" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration delete-external-account-test
  (testing "Delete external account"
    (let [response (accounts/delete-external-account stripe-client "acct_mock" "ba_mock")]
      (is (map? response))
      (is (= "bank_account" (:object response)))
      (is (string? (:id response)))
      (is (true? (:deleted response))))))

(deftest ^:integration list-external-accounts-test
  (testing "List external accounts"
    (let [response (accounts/list-external-accounts stripe-client "acct_mock" {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [ea (:data response)]
        (is (map? ea))
        (is (= "bank_account" (:object ea)))
        (is (string? (:id ea)))))))

(deftest ^:integration create-login-link-test
  (testing "Create login link"
    (let [response (accounts/create-login-link stripe-client "acct_mock")]
      (is (map? response))
      (is (= "login_link" (:object response)))
      (is (string? (:url response))))))

(deftest ^:integration create-person-test
  (testing "Create person"
    (let [payload {:first_name "John" :last_name "Doe"}
          response (accounts/create-person stripe-client "acct_mock" payload)]
      (is (map? response))
      (is (= "person" (:object response)))
      (is (string? (:id response)))
      (is (= "John" (:first_name response)))
      (is (= "Doe" (:last_name response))))))

(deftest ^:integration retrieve-person-test
  (testing "Retrieve person"
    (let [response (accounts/retrieve-person stripe-client "acct_mock" "pers_mock")]
      (is (map? response))
      (is (= "person" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :first_name)))))

(deftest ^:integration update-person-test
  (testing "Update person"
    (let [payload {:first_name "Jane"}
          response (accounts/update-person stripe-client "acct_mock" "pers_mock" payload)]
      (is (map? response))
      (is (= "person" (:object response)))
      (is (string? (:id response)))
      (is (= "Jane" (:first_name response))))))

(deftest ^:integration delete-person-test
  (testing "Delete person"
    (let [response (accounts/delete-person stripe-client "acct_mock" "pers_mock")]
      (is (map? response))
      (is (= "person" (:object response)))
      (is (string? (:id response)))
      (is (true? (:deleted response))))))

(deftest ^:integration list-persons-test
  (testing "List persons"
    (let [response (accounts/list-persons stripe-client "acct_mock" {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [person (:data response)]
        (is (map? person))
        (is (= "person" (:object person)))
        (is (string? (:id person)))
        (is (contains? person :first_name))))))
