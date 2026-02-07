(ns stripe-clojure.unit.v2.core.person-tokens-test
  (:require [clojure.test :refer [deftest testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.v2.core.person-tokens :as v2-tokens]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (h/check-endpoint config/stripe-v2-endpoints :v2-core-accounts "/v2/core/accounts"))

(deftest function-existence-test
  (testing "all person token functions exist"
    (h/check-functions-exist
     [#'v2-tokens/create-person-token
      #'v2-tokens/retrieve-person-token])))

(deftest function-arities-test
  (h/check-function-arities
   [[#'v2-tokens/create-person-token #{3 4}]
    [#'v2-tokens/retrieve-person-token #{3 4}]]))

(deftest docstrings-test
  (h/check-docstrings
   [[#'v2-tokens/create-person-token #"docs.stripe.com" #"(?i)token"]
    [#'v2-tokens/retrieve-person-token #"docs.stripe.com" #"(?i)token"]]))

(deftest create-person-token-request-test
  (testing "create-person-token uses POST with correct nested URL"
    (h/check-request-calls
     [{:api-fn v2-tokens/create-person-token
       :args ["acct_123" {:person {:first-name "Jane"}} {}]
       :method :post
       :endpoint "/v2/core/accounts/acct_123/person_tokens"
       :params {:person {:first-name "Jane"}}
       :opts {}}
      ;; 3-arity defaults opts
      {:api-fn v2-tokens/create-person-token
       :args ["acct_123" {:person {:first-name "Jane"}}]
       :method :post
       :endpoint "/v2/core/accounts/acct_123/person_tokens"
       :params {:person {:first-name "Jane"}}
       :opts {}}])))

(deftest retrieve-person-token-request-test
  (testing "retrieve-person-token uses GET with token ID in URL"
    (h/check-request-calls
     [{:api-fn v2-tokens/retrieve-person-token
       :args ["acct_123" "ptok_456" {}]
       :method :get
       :endpoint "/v2/core/accounts/acct_123/person_tokens/ptok_456"
       :params {}
       :opts {}}
      {:api-fn v2-tokens/retrieve-person-token
       :args ["acct_123" "ptok_456"]
       :method :get
       :endpoint "/v2/core/accounts/acct_123/person_tokens/ptok_456"
       :params {}
       :opts {}}])))
