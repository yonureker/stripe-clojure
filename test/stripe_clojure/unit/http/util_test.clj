(ns stripe-clojure.unit.http.util-test
  (:require [clojure.test :refer [deftest is testing]]
            [stripe-clojure.http.util :as util]))

(deftest kebab-to-snake-test
  (testing "converts kebab-case to snake_case"
    (is (= "hello_world" (util/kebab-to-snake "hello-world")))
    (is (= "first_name" (util/kebab-to-snake "first-name")))
    (is (= "some_long_variable_name" (util/kebab-to-snake "some-long-variable-name"))))

  (testing "leaves non-kebab strings unchanged"
    (is (= "hello" (util/kebab-to-snake "hello")))
    (is (= "already_snake" (util/kebab-to-snake "already_snake")))
    (is (= "" (util/kebab-to-snake "")))))

(deftest flatten-params-test
  (testing "flattens simple maps"
    (is (= {"name" "John"} (util/flatten-params {:name "John"})))
    (is (= {"email" "test@example.com"} (util/flatten-params {:email "test@example.com"}))))

  (testing "converts kebab-case keys to snake_case"
    (is (= {"first_name" "John"} (util/flatten-params {:first-name "John"})))
    (is (= {"last_name" "Doe"} (util/flatten-params {:last-name "Doe"}))))

  (testing "flattens nested maps with bracket notation"
    (is (= {"customer[name]" "John"} (util/flatten-params {:customer {:name "John"}})))
    (is (= {"address[city]" "NYC" "address[zip]" "10001"}
           (util/flatten-params {:address {:city "NYC" :zip "10001"}}))))

  (testing "flattens deeply nested maps"
    (is (= {"a[b][c]" "value"}
           (util/flatten-params {:a {:b {:c "value"}}}))))

  (testing "flattens arrays with indexed notation"
    (is (= {"items[0]" "a" "items[1]" "b"}
           (util/flatten-params {:items ["a" "b"]}))))

  (testing "flattens arrays of maps"
    (is (= {"items[0][price]" "price_123" "items[0][quantity]" 2}
           (util/flatten-params {:items [{:price "price_123" :quantity 2}]}))))

  (testing "handles nil params"
    (is (nil? (util/flatten-params nil))))

  (testing "handles empty params"
    (is (= {} (util/flatten-params {})))))

(deftest format-indexed-params-test
  (testing "formats with 'include' prefix (v2 use case)"
    (is (= {"include[0]" "data.payload"} (util/format-indexed-params "include" "data.payload")))
    (is (= {"include[0]" "data.payload" "include[1]" "data.detail"}
           (util/format-indexed-params "include" ["data.payload" "data.detail"]))))

  (testing "formats with 'expand' prefix (v1 use case)"
    (is (= {"expand[0]" "customer"} (util/format-indexed-params "expand" "customer")))
    (is (= {"expand[0]" "customer" "expand[1]" "invoice.subscription"}
           (util/format-indexed-params "expand" ["customer" "invoice.subscription"]))))

  (testing "formats with arbitrary prefix"
    (is (= {"items[0]" "a" "items[1]" "b" "items[2]" "c"}
           (util/format-indexed-params "items" ["a" "b" "c"]))))

  (testing "returns empty map for nil"
    (is (= {} (util/format-indexed-params "expand" nil))))

  (testing "returns empty map for empty vector"
    (is (= {} (util/format-indexed-params "include" []))))

  (testing "handles lazy sequences (non-counted)"
    (is (= {"expand[0]" "a" "expand[1]" "b"}
           (util/format-indexed-params "expand" (map identity ["a" "b"])))))

  (testing "handles single-element vector"
    (is (= {"include[0]" "payload"}
           (util/format-indexed-params "include" ["payload"])))))

(deftest format-expand-test
  (testing "formats single string expand"
    (is (= {"expand[0]" "customer"} (util/format-expand "customer"))))

  (testing "formats vector of expand fields"
    (is (= {"expand[0]" "customer" "expand[1]" "invoice"}
           (util/format-expand ["customer" "invoice"]))))

  (testing "formats multiple expand fields"
    (is (= {"expand[0]" "a" "expand[1]" "b" "expand[2]" "c"}
           (util/format-expand ["a" "b" "c"]))))

  (testing "returns empty map for nil"
    (is (= {} (util/format-expand nil))))

  (testing "returns empty map for empty vector"
    (is (= {} (util/format-expand [])))))

(deftest underscore-to-kebab-test
  (testing "converts underscore_case to kebab-case"
    (is (= "hello-world" (util/underscore-to-kebab "hello_world")))
    (is (= "first-name" (util/underscore-to-kebab "first_name")))
    (is (= "api-version" (util/underscore-to-kebab "api_version"))))

  (testing "leaves non-underscore strings unchanged"
    (is (= "hello" (util/underscore-to-kebab "hello")))
    (is (= "already-kebab" (util/underscore-to-kebab "already-kebab")))))

(deftest transform-keys-test
  (testing "transforms simple map keys"
    (is (= {:first-name "John"} (util/transform-keys {:first_name "John"})))
    (is (= {:api-version "v1"} (util/transform-keys {:api_version "v1"}))))

  (testing "transforms nested map keys"
    (is (= {:customer {:first-name "John" :last-name "Doe"}}
           (util/transform-keys {:customer {:first_name "John" :last_name "Doe"}}))))

  (testing "transforms keys in vectors"
    (is (= {:items [{:product-id "prod_123"}]}
           (util/transform-keys {:items [{:product_id "prod_123"}]}))))

  (testing "handles non-map input"
    (is (= "string" (util/transform-keys "string")))
    (is (= 123 (util/transform-keys 123)))
    (is (nil? (util/transform-keys nil)))))
