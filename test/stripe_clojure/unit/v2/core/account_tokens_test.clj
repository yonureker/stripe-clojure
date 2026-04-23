(ns stripe-clojure.unit.v2.core.account-tokens-test
  (:require [clojure.test :refer [deftest testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.v2.core.account-tokens :as v2-account-tokens]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (h/check-endpoint config/stripe-v2-endpoints :v2-core-account-tokens "/v2/core/account_tokens"))

(deftest function-existence-test
  (testing "all account token functions exist"
    (h/check-functions-exist
     [#'v2-account-tokens/create-account-token
      #'v2-account-tokens/retrieve-account-token])))

(deftest function-arities-test
  (h/check-function-arities
   [[#'v2-account-tokens/create-account-token #{2 3}]
    [#'v2-account-tokens/retrieve-account-token #{2 3}]]))

(deftest docstrings-test
  (h/check-docstrings
   [[#'v2-account-tokens/create-account-token #"docs.stripe.com" #"(?i)token"]
    [#'v2-account-tokens/retrieve-account-token #"docs.stripe.com" #"(?i)token"]]))

(deftest create-account-token-request-test
  (testing "create-account-token calls request correctly"
    (h/check-request-calls
     [{:api-fn v2-account-tokens/create-account-token
       :args [{:display_name "My Business"} {:idempotency-key "key_1"}]
       :method :post
       :endpoint "/v2/core/account_tokens"
       :params {:display_name "My Business"}
       :opts {:idempotency-key "key_1"}}
      ;; 2-arity defaults opts to {}
      {:api-fn v2-account-tokens/create-account-token
       :args [{:display_name "My Business"}]
       :method :post
       :endpoint "/v2/core/account_tokens"
       :params {:display_name "My Business"}
       :opts {}}])))

(deftest retrieve-account-token-request-test
  (testing "retrieve-account-token calls request correctly"
    (h/check-request-calls
     [{:api-fn v2-account-tokens/retrieve-account-token
       :args ["actok_123" {}]
       :method :get
       :endpoint "/v2/core/account_tokens/actok_123"
       :params {}
       :opts {}}
      ;; 2-arity defaults opts to {}
      {:api-fn v2-account-tokens/retrieve-account-token
       :args ["actok_123"]
       :method :get
       :endpoint "/v2/core/account_tokens/actok_123"
       :params {}
       :opts {}}])))
