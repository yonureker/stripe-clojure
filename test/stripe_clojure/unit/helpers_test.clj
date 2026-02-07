(ns stripe-clojure.unit.helpers-test
  (:require [clojure.test :refer [deftest is testing]]
            [stripe-clojure.http.util :as util]))

(deftest test-flatten-params
  (testing "flatten-params with nested map"
    (let [params {:customer {:name "John Doe" :address {:line1 "123 Main St" :city "Anytown"}}}
          expected {"customer[name]" "John Doe" "customer[address][line1]" "123 Main St" "customer[address][city]" "Anytown"}]
      (is (= expected (util/flatten-params params)))))

  (testing "flatten-params with empty map"
    (let [params {}
          expected {}]
      (is (= expected (util/flatten-params params)))))
  
  (testing "flatten-params with multiple data types"
    (let [params {:custom_fields [{:key "github-username"
                                   :label {:type "custom"
                                           :custom "GitHub Username"}
                                   :type "text"
                                   :optional false}]
                  :success_url "test"
                  :cancel_url "test"
                  :mode "payment" 
                  :line_items [{:price "hello"
                                :quantity 1}]
                  :metadata {:user_id "user_123"
                             :price_id "price_123"
                             :product_id "prod_123"}}
          expected {"custom_fields[0][key]" "github-username"
                    "custom_fields[0][label][type]" "custom"
                    "custom_fields[0][label][custom]" "GitHub Username"
                    "custom_fields[0][type]" "text"
                    "custom_fields[0][optional]" false
                    "success_url" "test"
                    "cancel_url" "test"
                    "mode" "payment"
                    "line_items[0][price]" "hello"
                    "line_items[0][quantity]" 1
                    "metadata[user_id]" "user_123"
                    "metadata[price_id]" "price_123"
                    "metadata[product_id]" "prod_123"}
          result (util/flatten-params params)]
      (is (= expected result))))

    (testing "flatten-params with non-nested map"
      (let [params {:name "John Doe" :email "john@example.com"}
            expected {"name" "John Doe" "email" "john@example.com"}]
        (is (= expected (util/flatten-params params))))))

(deftest test-format-expand
  (testing "format-expand with list of fields"
    (let [expand ["field1" "field2"]
          expected {"expand[0]" "field1" "expand[1]" "field2"}]
      (is (= expected (util/format-expand expand)))))

  (testing "format-expand with single field as string"
    (let [expand "field1"
          expected {"expand[0]" "field1"}]
      (is (= expected (util/format-expand expand)))))

  (testing "format-expand with empty list"
    (let [expand []
          expected {}]
      (is (= expected (util/format-expand expand))))))

