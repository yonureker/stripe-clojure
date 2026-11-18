(ns stripe-clojure.unit.v2.core.vault.gb-bank-accounts-test
  (:require [clojure.test :refer [deftest testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.v2.core.vault.gb-bank-accounts :as v2-gb-bank-accounts]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (h/check-endpoint config/stripe-v2-endpoints :v2-core-vault-gb-bank-accounts "/v2/core/vault/gb_bank_accounts"))

(deftest function-existence-test
  (testing "all functions exist"
    (h/check-functions-exist
     [#'v2-gb-bank-accounts/create-gb-bank-account
      #'v2-gb-bank-accounts/retrieve-gb-bank-account
      #'v2-gb-bank-accounts/list-gb-bank-accounts
      #'v2-gb-bank-accounts/archive-gb-bank-account
      #'v2-gb-bank-accounts/unarchive-gb-bank-account])))

(deftest function-arities-test
  (h/check-function-arities
   [[#'v2-gb-bank-accounts/create-gb-bank-account #{2 3}]
    [#'v2-gb-bank-accounts/retrieve-gb-bank-account #{2 3}]
    [#'v2-gb-bank-accounts/list-gb-bank-accounts #{1 2 3}]
    [#'v2-gb-bank-accounts/archive-gb-bank-account #{2 3}]
    [#'v2-gb-bank-accounts/unarchive-gb-bank-account #{2 3}]]))

(deftest docstrings-test
  (h/check-docstrings
   [[#'v2-gb-bank-accounts/create-gb-bank-account #"(?i)create"]
    [#'v2-gb-bank-accounts/retrieve-gb-bank-account #"(?i)retriev"]
    [#'v2-gb-bank-accounts/list-gb-bank-accounts #"(?i)list"]
    [#'v2-gb-bank-accounts/archive-gb-bank-account #"(?i)archiv"]
    [#'v2-gb-bank-accounts/unarchive-gb-bank-account #"(?i)unarchiv"]]))

(deftest create-gb-bank-account-request-test
  (testing "create uses POST on base endpoint"
    (h/check-request-calls
     [{:api-fn v2-gb-bank-accounts/create-gb-bank-account
       :args [{:currency "gbp"} {}]
       :method :post
       :endpoint "/v2/core/vault/gb_bank_accounts"
       :params {:currency "gbp"}
       :opts {}}
      {:api-fn v2-gb-bank-accounts/create-gb-bank-account
       :args [{:currency "gbp"}]
       :method :post
       :endpoint "/v2/core/vault/gb_bank_accounts"
       :params {:currency "gbp"}
       :opts {}}])))

(deftest retrieve-gb-bank-account-request-test
  (testing "retrieve uses GET with bank account ID"
    (h/check-request-calls
     [{:api-fn v2-gb-bank-accounts/retrieve-gb-bank-account
       :args ["gbba_123" {}]
       :method :get
       :endpoint "/v2/core/vault/gb_bank_accounts/gbba_123"
       :params {}
       :opts {}}
      {:api-fn v2-gb-bank-accounts/retrieve-gb-bank-account
       :args ["gbba_123"]
       :method :get
       :endpoint "/v2/core/vault/gb_bank_accounts/gbba_123"
       :params {}
       :opts {}}])))

(deftest list-gb-bank-accounts-request-test
  (testing "list uses GET on base endpoint"
    (h/check-request-calls
     [{:api-fn v2-gb-bank-accounts/list-gb-bank-accounts
       :args [{:limit 10} {}]
       :method :get
       :endpoint "/v2/core/vault/gb_bank_accounts"
       :params {:limit 10}
       :opts {}}
      {:api-fn v2-gb-bank-accounts/list-gb-bank-accounts
       :args [{:limit 10}]
       :method :get
       :endpoint "/v2/core/vault/gb_bank_accounts"
       :params {:limit 10}
       :opts {}}
      {:api-fn v2-gb-bank-accounts/list-gb-bank-accounts
       :args []
       :method :get
       :endpoint "/v2/core/vault/gb_bank_accounts"
       :params {}
       :opts {}}])))

(deftest archive-gb-bank-account-request-test
  (testing "archive uses POST on /archive action"
    (h/check-request-calls
     [{:api-fn v2-gb-bank-accounts/archive-gb-bank-account
       :args ["gbba_123" {}]
       :method :post
       :endpoint "/v2/core/vault/gb_bank_accounts/gbba_123/archive"
       :params {}
       :opts {}}
      {:api-fn v2-gb-bank-accounts/archive-gb-bank-account
       :args ["gbba_123"]
       :method :post
       :endpoint "/v2/core/vault/gb_bank_accounts/gbba_123/archive"
       :params {}
       :opts {}}])))

(deftest unarchive-gb-bank-account-request-test
  (testing "unarchive uses POST on /unarchive action"
    (h/check-request-calls
     [{:api-fn v2-gb-bank-accounts/unarchive-gb-bank-account
       :args ["gbba_123" {}]
       :method :post
       :endpoint "/v2/core/vault/gb_bank_accounts/gbba_123/unarchive"
       :params {}
       :opts {}}
      {:api-fn v2-gb-bank-accounts/unarchive-gb-bank-account
       :args ["gbba_123"]
       :method :post
       :endpoint "/v2/core/vault/gb_bank_accounts/gbba_123/unarchive"
       :params {}
       :opts {}}])))
