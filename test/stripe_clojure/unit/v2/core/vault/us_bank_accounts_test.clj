(ns stripe-clojure.unit.v2.core.vault.us-bank-accounts-test
  (:require [clojure.test :refer [deftest testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.v2.core.vault.us-bank-accounts :as v2-us-bank-accounts]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (h/check-endpoint config/stripe-v2-endpoints :v2-core-vault-us-bank-accounts "/v2/core/vault/us_bank_accounts"))

(deftest function-existence-test
  (testing "all functions exist"
    (h/check-functions-exist
     [#'v2-us-bank-accounts/create-us-bank-account
      #'v2-us-bank-accounts/retrieve-us-bank-account
      #'v2-us-bank-accounts/update-us-bank-account
      #'v2-us-bank-accounts/list-us-bank-accounts
      #'v2-us-bank-accounts/archive-us-bank-account
      #'v2-us-bank-accounts/unarchive-us-bank-account
      #'v2-us-bank-accounts/send-microdeposits
      #'v2-us-bank-accounts/confirm-microdeposits])))

(deftest function-arities-test
  (h/check-function-arities
   [[#'v2-us-bank-accounts/create-us-bank-account #{2 3}]
    [#'v2-us-bank-accounts/retrieve-us-bank-account #{2 3}]
    [#'v2-us-bank-accounts/update-us-bank-account #{3 4}]
    [#'v2-us-bank-accounts/list-us-bank-accounts #{1 2 3}]
    [#'v2-us-bank-accounts/archive-us-bank-account #{2 3}]
    [#'v2-us-bank-accounts/unarchive-us-bank-account #{2 3}]
    [#'v2-us-bank-accounts/send-microdeposits #{2 3}]
    [#'v2-us-bank-accounts/confirm-microdeposits #{3 4}]]))

(deftest docstrings-test
  (h/check-docstrings
   [[#'v2-us-bank-accounts/create-us-bank-account #"(?i)create"]
    [#'v2-us-bank-accounts/retrieve-us-bank-account #"(?i)retriev"]
    [#'v2-us-bank-accounts/update-us-bank-account #"(?i)update"]
    [#'v2-us-bank-accounts/list-us-bank-accounts #"(?i)list"]
    [#'v2-us-bank-accounts/archive-us-bank-account #"(?i)archiv"]
    [#'v2-us-bank-accounts/unarchive-us-bank-account #"(?i)unarchiv"]
    [#'v2-us-bank-accounts/send-microdeposits #"(?i)microdeposit"]
    [#'v2-us-bank-accounts/confirm-microdeposits #"(?i)microdeposit"]]))

(deftest create-us-bank-account-request-test
  (testing "create uses POST on base endpoint"
    (h/check-request-calls
     [{:api-fn v2-us-bank-accounts/create-us-bank-account
       :args [{:account_number "000123456789" :currency "usd"} {}]
       :method :post
       :endpoint "/v2/core/vault/us_bank_accounts"
       :params {:account_number "000123456789" :currency "usd"}
       :opts {}}
      {:api-fn v2-us-bank-accounts/create-us-bank-account
       :args [{:account_number "000123456789" :currency "usd"}]
       :method :post
       :endpoint "/v2/core/vault/us_bank_accounts"
       :params {:account_number "000123456789" :currency "usd"}
       :opts {}}])))

(deftest retrieve-us-bank-account-request-test
  (testing "retrieve uses GET with bank account ID"
    (h/check-request-calls
     [{:api-fn v2-us-bank-accounts/retrieve-us-bank-account
       :args ["usba_123" {}]
       :method :get
       :endpoint "/v2/core/vault/us_bank_accounts/usba_123"
       :params {}
       :opts {}}
      {:api-fn v2-us-bank-accounts/retrieve-us-bank-account
       :args ["usba_123"]
       :method :get
       :endpoint "/v2/core/vault/us_bank_accounts/usba_123"
       :params {}
       :opts {}}])))

(deftest update-us-bank-account-request-test
  (testing "update uses POST with bank account ID"
    (h/check-request-calls
     [{:api-fn v2-us-bank-accounts/update-us-bank-account
       :args ["usba_123" {:routing_number "110000000"} {}]
       :method :post
       :endpoint "/v2/core/vault/us_bank_accounts/usba_123"
       :params {:routing_number "110000000"}
       :opts {}}
      {:api-fn v2-us-bank-accounts/update-us-bank-account
       :args ["usba_123" {:routing_number "110000000"}]
       :method :post
       :endpoint "/v2/core/vault/us_bank_accounts/usba_123"
       :params {:routing_number "110000000"}
       :opts {}}])))

(deftest list-us-bank-accounts-request-test
  (testing "list uses GET on base endpoint"
    (h/check-request-calls
     [{:api-fn v2-us-bank-accounts/list-us-bank-accounts
       :args [{:limit 10} {}]
       :method :get
       :endpoint "/v2/core/vault/us_bank_accounts"
       :params {:limit 10}
       :opts {}}
      {:api-fn v2-us-bank-accounts/list-us-bank-accounts
       :args [{:limit 10}]
       :method :get
       :endpoint "/v2/core/vault/us_bank_accounts"
       :params {:limit 10}
       :opts {}}
      {:api-fn v2-us-bank-accounts/list-us-bank-accounts
       :args []
       :method :get
       :endpoint "/v2/core/vault/us_bank_accounts"
       :params {}
       :opts {}}])))

(deftest archive-us-bank-account-request-test
  (testing "archive uses POST on /archive action"
    (h/check-request-calls
     [{:api-fn v2-us-bank-accounts/archive-us-bank-account
       :args ["usba_123" {}]
       :method :post
       :endpoint "/v2/core/vault/us_bank_accounts/usba_123/archive"
       :params {}
       :opts {}}
      {:api-fn v2-us-bank-accounts/archive-us-bank-account
       :args ["usba_123"]
       :method :post
       :endpoint "/v2/core/vault/us_bank_accounts/usba_123/archive"
       :params {}
       :opts {}}])))

(deftest unarchive-us-bank-account-request-test
  (testing "unarchive uses POST on /unarchive action"
    (h/check-request-calls
     [{:api-fn v2-us-bank-accounts/unarchive-us-bank-account
       :args ["usba_123" {}]
       :method :post
       :endpoint "/v2/core/vault/us_bank_accounts/usba_123/unarchive"
       :params {}
       :opts {}}
      {:api-fn v2-us-bank-accounts/unarchive-us-bank-account
       :args ["usba_123"]
       :method :post
       :endpoint "/v2/core/vault/us_bank_accounts/usba_123/unarchive"
       :params {}
       :opts {}}])))

(deftest send-microdeposits-request-test
  (testing "send-microdeposits uses POST on /send_microdeposits action"
    (h/check-request-calls
     [{:api-fn v2-us-bank-accounts/send-microdeposits
       :args ["usba_123" {}]
       :method :post
       :endpoint "/v2/core/vault/us_bank_accounts/usba_123/send_microdeposits"
       :params {}
       :opts {}}
      {:api-fn v2-us-bank-accounts/send-microdeposits
       :args ["usba_123"]
       :method :post
       :endpoint "/v2/core/vault/us_bank_accounts/usba_123/send_microdeposits"
       :params {}
       :opts {}}])))

(deftest confirm-microdeposits-request-test
  (testing "confirm-microdeposits uses POST on /confirm_microdeposits action"
    (h/check-request-calls
     [{:api-fn v2-us-bank-accounts/confirm-microdeposits
       :args ["usba_123" {:amounts [32 45]} {}]
       :method :post
       :endpoint "/v2/core/vault/us_bank_accounts/usba_123/confirm_microdeposits"
       :params {:amounts [32 45]}
       :opts {}}
      {:api-fn v2-us-bank-accounts/confirm-microdeposits
       :args ["usba_123" {:amounts [32 45]}]
       :method :post
       :endpoint "/v2/core/vault/us_bank_accounts/usba_123/confirm_microdeposits"
       :params {:amounts [32 45]}
       :opts {}}])))
