(ns stripe-clojure.unit.customers-test
  (:require [clojure.test :refer [deftest is testing]]
            [stripe-clojure.customers :as customers]
            [stripe-clojure.http.client :as client]
            [stripe-clojure.unit.test-helpers :as h]))

(deftest search-customers-request-test
  (testing "1-arity passes params map with empty opts"
    (h/check-request-calls
     [{:api-fn customers/search-customers
       :args [{:query "email:'test@example.com'" :limit 10}]
       :method :get
       :endpoint "/v1/customers/search"
       :params {:query "email:'test@example.com'" :limit 10}
       :opts {}}]))

  (testing "2-arity forwards params map and opts directly"
    (h/check-request-calls
     [{:api-fn customers/search-customers
       :args [{:query "name:'Jane'" :limit 10} {:auto-paginate? true}]
       :method :get
       :endpoint "/v1/customers/search"
       :params {:query "name:'Jane'" :limit 10}
       :opts {:auto-paginate? true}}])))

(deftest customers-function-arities-test
  (h/check-function-arities
   [[#'customers/create-customer #{2 3}]
    [#'customers/retrieve-customer #{2 3}]
    [#'customers/update-customer #{3 4}]
    [#'customers/delete-customer #{2 3}]
    [#'customers/list-customers #{1 2 3}]
    [#'customers/search-customers #{2 3}]]))
