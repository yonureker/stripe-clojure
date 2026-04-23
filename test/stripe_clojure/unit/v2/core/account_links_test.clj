(ns stripe-clojure.unit.v2.core.account-links-test
  (:require [clojure.test :refer [deftest testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.v2.core.account-links :as v2-account-links]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (h/check-endpoint config/stripe-v2-endpoints :v2-core-account-links "/v2/core/account_links"))

(deftest function-existence-test
  (testing "all account link functions exist"
    (h/check-functions-exist
     [#'v2-account-links/create-account-link])))

(deftest function-arities-test
  (h/check-function-arities
   [[#'v2-account-links/create-account-link #{2 3}]]))

(deftest docstrings-test
  (h/check-docstrings
   [[#'v2-account-links/create-account-link #"docs.stripe.com"]]))

(deftest create-account-link-request-test
  (testing "create-account-link calls request correctly"
    (h/check-request-calls
     [{:api-fn v2-account-links/create-account-link
       :args [{:account "acct_123" :use_case {:type "account_onboarding"}} {}]
       :method :post
       :endpoint "/v2/core/account_links"
       :params {:account "acct_123" :use_case {:type "account_onboarding"}}
       :opts {}}
      ;; 2-arity defaults opts to {}
      {:api-fn v2-account-links/create-account-link
       :args [{:account "acct_123" :use_case {:type "account_onboarding"}}]
       :method :post
       :endpoint "/v2/core/account_links"
       :params {:account "acct_123" :use_case {:type "account_onboarding"}}
       :opts {}}])))
