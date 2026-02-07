(ns stripe-clojure.unit.v2.core.accounts-test
  (:require [clojure.test :refer [deftest testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.v2.core.accounts :as v2-accounts]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (h/check-endpoint config/stripe-v2-endpoints :v2-core-accounts "/v2/core/accounts"))

(deftest function-existence-test
  (testing "all account functions exist"
    (h/check-functions-exist
     [#'v2-accounts/create-account
      #'v2-accounts/retrieve-account
      #'v2-accounts/update-account
      #'v2-accounts/list-accounts
      #'v2-accounts/close-account])))

(deftest function-arities-test
  (h/check-function-arities
   [[#'v2-accounts/create-account #{2 3}]
    [#'v2-accounts/retrieve-account #{2 3}]
    [#'v2-accounts/update-account #{3 4}]
    [#'v2-accounts/list-accounts #{1 2 3}]
    [#'v2-accounts/close-account #{2 3}]]))

(deftest docstrings-test
  (h/check-docstrings
   [[#'v2-accounts/create-account #"docs.stripe.com"]
    [#'v2-accounts/retrieve-account #"docs.stripe.com"]
    [#'v2-accounts/update-account #"docs.stripe.com"]
    [#'v2-accounts/list-accounts #"docs.stripe.com"]
    [#'v2-accounts/close-account #"docs.stripe.com"]]))

(deftest create-account-request-test
  (testing "create-account calls request correctly"
    (h/check-request-calls
     [{:api-fn v2-accounts/create-account
       :args [{:email "test@example.com"} {:include ["identity"]}]
       :method :post
       :endpoint "/v2/core/accounts"
       :params {:email "test@example.com"}
       :opts {:include ["identity"]}}
      ;; 2-arity defaults opts to {}
      {:api-fn v2-accounts/create-account
       :args [{:email "test@example.com"}]
       :method :post
       :endpoint "/v2/core/accounts"
       :params {:email "test@example.com"}
       :opts {}}])))

(deftest retrieve-account-request-test
  (testing "retrieve-account calls request correctly"
    (h/check-request-calls
     [{:api-fn v2-accounts/retrieve-account
       :args ["acct_123" {:include ["identity"]}]
       :method :get
       :endpoint "/v2/core/accounts/acct_123"
       :params {}
       :opts {:include ["identity"]}}
      ;; 2-arity defaults opts to {}
      {:api-fn v2-accounts/retrieve-account
       :args ["acct_123"]
       :method :get
       :endpoint "/v2/core/accounts/acct_123"
       :params {}
       :opts {}}])))

(deftest update-account-request-test
  (testing "update-account calls request correctly"
    (h/check-request-calls
     [{:api-fn v2-accounts/update-account
       :args ["acct_123" {:email "new@example.com"} {:idempotency-key "key_1"}]
       :method :post
       :endpoint "/v2/core/accounts/acct_123"
       :params {:email "new@example.com"}
       :opts {:idempotency-key "key_1"}}
      ;; 3-arity defaults opts to {}
      {:api-fn v2-accounts/update-account
       :args ["acct_123" {:email "new@example.com"}]
       :method :post
       :endpoint "/v2/core/accounts/acct_123"
       :params {:email "new@example.com"}
       :opts {}}])))

(deftest list-accounts-request-test
  (testing "list-accounts calls request correctly"
    (h/check-request-calls
     [{:api-fn v2-accounts/list-accounts
       :args [{:limit 10} {:include ["identity"]}]
       :method :get
       :endpoint "/v2/core/accounts"
       :params {:limit 10}
       :opts {:include ["identity"]}}
      ;; 2-arity defaults opts to {}
      {:api-fn v2-accounts/list-accounts
       :args [{:limit 10}]
       :method :get
       :endpoint "/v2/core/accounts"
       :params {:limit 10}
       :opts {}}
      ;; 1-arity defaults params to {} and opts to {}
      {:api-fn v2-accounts/list-accounts
       :args []
       :method :get
       :endpoint "/v2/core/accounts"
       :params {}
       :opts {}}])))

(deftest close-account-request-test
  (testing "close-account calls request correctly"
    (h/check-request-calls
     [{:api-fn v2-accounts/close-account
       :args ["acct_123" {:idempotency-key "key_1"}]
       :method :post
       :endpoint "/v2/core/accounts/acct_123/close"
       :params {}
       :opts {:idempotency-key "key_1"}}
      ;; 2-arity defaults opts to {}
      {:api-fn v2-accounts/close-account
       :args ["acct_123"]
       :method :post
       :endpoint "/v2/core/accounts/acct_123/close"
       :params {}
       :opts {}}])))
