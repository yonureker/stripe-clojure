(ns stripe-clojure.unit.v2.commerce.product-catalog.imports-test
  (:require [clojure.test :refer [deftest testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.v2.commerce.product-catalog.imports :as v2-imports]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (h/check-endpoint config/stripe-v2-endpoints :v2-commerce-product-catalog-imports "/v2/commerce/product_catalog/imports"))

(deftest function-existence-test
  (testing "all functions exist"
    (h/check-functions-exist
     [#'v2-imports/create-import
      #'v2-imports/retrieve-import
      #'v2-imports/list-imports])))

(deftest function-arities-test
  (h/check-function-arities
   [[#'v2-imports/create-import #{2 3}]
    [#'v2-imports/retrieve-import #{2 3}]
    [#'v2-imports/list-imports #{1 2 3}]]))

(deftest docstrings-test
  (h/check-docstrings
   [[#'v2-imports/create-import #"(?i)create"]
    [#'v2-imports/retrieve-import #"(?i)retriev"]
    [#'v2-imports/list-imports #"(?i)list"]]))

(deftest create-import-request-test
  (testing "create uses POST on base endpoint"
    (h/check-request-calls
     [{:api-fn v2-imports/create-import
       :args [{:feed_type "product" :mode "upsert" :metadata {}} {}]
       :method :post
       :endpoint "/v2/commerce/product_catalog/imports"
       :params {:feed_type "product" :mode "upsert" :metadata {}}
       :opts {}}
      {:api-fn v2-imports/create-import
       :args [{:feed_type "product" :mode "upsert" :metadata {}}]
       :method :post
       :endpoint "/v2/commerce/product_catalog/imports"
       :params {:feed_type "product" :mode "upsert" :metadata {}}
       :opts {}}])))

(deftest retrieve-import-request-test
  (testing "retrieve uses GET with product catalog import ID"
    (h/check-request-calls
     [{:api-fn v2-imports/retrieve-import
       :args ["pci_123" {}]
       :method :get
       :endpoint "/v2/commerce/product_catalog/imports/pci_123"
       :params {}
       :opts {}}
      {:api-fn v2-imports/retrieve-import
       :args ["pci_123"]
       :method :get
       :endpoint "/v2/commerce/product_catalog/imports/pci_123"
       :params {}
       :opts {}}])))

(deftest list-imports-request-test
  (testing "list uses GET on base endpoint"
    (h/check-request-calls
     [{:api-fn v2-imports/list-imports
       :args [{:feed_type "product"} {}]
       :method :get
       :endpoint "/v2/commerce/product_catalog/imports"
       :params {:feed_type "product"}
       :opts {}}
      {:api-fn v2-imports/list-imports
       :args [{:feed_type "product"}]
       :method :get
       :endpoint "/v2/commerce/product_catalog/imports"
       :params {:feed_type "product"}
       :opts {}}
      {:api-fn v2-imports/list-imports
       :args []
       :method :get
       :endpoint "/v2/commerce/product_catalog/imports"
       :params {}
       :opts {}}])))
