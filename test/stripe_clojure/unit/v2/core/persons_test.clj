(ns stripe-clojure.unit.v2.core.persons-test
  (:require [clojure.test :refer [deftest testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.v2.core.persons :as v2-persons]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (h/check-endpoint config/stripe-v2-endpoints :v2-core-accounts "/v2/core/accounts"))

(deftest function-existence-test
  (testing "all person functions exist"
    (h/check-functions-exist
     [#'v2-persons/create-person
      #'v2-persons/retrieve-person
      #'v2-persons/update-person
      #'v2-persons/list-persons
      #'v2-persons/delete-person])))

(deftest function-arities-test
  (h/check-function-arities
   [[#'v2-persons/create-person #{3 4}]
    [#'v2-persons/retrieve-person #{3 4}]
    [#'v2-persons/update-person #{4 5}]
    [#'v2-persons/list-persons #{2 3 4}]
    [#'v2-persons/delete-person #{3 4}]]))

(deftest docstrings-test
  (h/check-docstrings
   [[#'v2-persons/create-person #"docs.stripe.com"]
    [#'v2-persons/retrieve-person #"docs.stripe.com"]
    [#'v2-persons/update-person #"docs.stripe.com"]
    [#'v2-persons/list-persons #"docs.stripe.com"]
    [#'v2-persons/delete-person #"docs.stripe.com"]]))

(deftest create-person-request-test
  (testing "create-person calls request with correct nested URL"
    (h/check-request-calls
     [{:api-fn v2-persons/create-person
       :args ["acct_123" {:first-name "Jane"} {:idempotency-key "k1"}]
       :method :post
       :endpoint "/v2/core/accounts/acct_123/persons"
       :params {:first-name "Jane"}
       :opts {:idempotency-key "k1"}}
      ;; 3-arity defaults opts to {}
      {:api-fn v2-persons/create-person
       :args ["acct_123" {:first-name "Jane"}]
       :method :post
       :endpoint "/v2/core/accounts/acct_123/persons"
       :params {:first-name "Jane"}
       :opts {}}])))

(deftest retrieve-person-request-test
  (testing "retrieve-person constructs correct URL with person ID"
    (h/check-request-calls
     [{:api-fn v2-persons/retrieve-person
       :args ["acct_123" "person_456" {:include ["identity"]}]
       :method :get
       :endpoint "/v2/core/accounts/acct_123/persons/person_456"
       :params {}
       :opts {:include ["identity"]}}
      {:api-fn v2-persons/retrieve-person
       :args ["acct_123" "person_456"]
       :method :get
       :endpoint "/v2/core/accounts/acct_123/persons/person_456"
       :params {}
       :opts {}}])))

(deftest update-person-request-test
  (testing "update-person uses POST with correct URL"
    (h/check-request-calls
     [{:api-fn v2-persons/update-person
       :args ["acct_123" "person_456" {:last-name "Doe"} {}]
       :method :post
       :endpoint "/v2/core/accounts/acct_123/persons/person_456"
       :params {:last-name "Doe"}
       :opts {}}])))

(deftest list-persons-request-test
  (testing "list-persons uses GET on persons endpoint"
    (h/check-request-calls
     [{:api-fn v2-persons/list-persons
       :args ["acct_123" {:limit 5} {}]
       :method :get
       :endpoint "/v2/core/accounts/acct_123/persons"
       :params {:limit 5}
       :opts {}}
      ;; 2-arity defaults params and opts
      {:api-fn v2-persons/list-persons
       :args ["acct_123"]
       :method :get
       :endpoint "/v2/core/accounts/acct_123/persons"
       :params {}
       :opts {}}])))

(deftest delete-person-request-test
  (testing "delete-person uses DELETE with correct URL"
    (h/check-request-calls
     [{:api-fn v2-persons/delete-person
       :args ["acct_123" "person_456" {}]
       :method :delete
       :endpoint "/v2/core/accounts/acct_123/persons/person_456"
       :params {}
       :opts {}}
      {:api-fn v2-persons/delete-person
       :args ["acct_123" "person_456"]
       :method :delete
       :endpoint "/v2/core/accounts/acct_123/persons/person_456"
       :params {}
       :opts {}}])))
