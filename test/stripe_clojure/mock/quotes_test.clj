(ns stripe-clojure.mock.quotes-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.quotes :as quotes]))

(deftest ^:integration create-quote-test
  (testing "Create quote"
    (let [params {:customer "cus_mock" :description "Test quote"}
          response (quotes/create-quote stripe-client params)]
      (is (map? response))
      (is (= "quote" (:object response)))
      (is (string? (:id response)))
      (is (= "cus_mock" (:customer response)))
      (is (= "Test quote" (:description response))))))

(deftest ^:integration retrieve-quote-test
  (testing "Retrieve quote"
    (let [response (quotes/retrieve-quote stripe-client "qt_mock")]
      (is (map? response))
      (is (= "quote" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :customer)))))

(deftest ^:integration update-quote-test
  (testing "Update quote"
    (let [params {:description "Updated quote"}
          response (quotes/update-quote stripe-client "qt_mock" params)]
      (is (map? response))
      (is (= "quote" (:object response)))
      (is (string? (:id response)))
      (is (= "Updated quote" (:description response))))))

(deftest ^:integration list-quotes-test
  (testing "List quotes"
    (let [response (quotes/list-quotes stripe-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [q (:data response)]
        (is (map? q))
        (is (= "quote" (:object q)))
        (is (string? (:id q)))
        (is (contains? q :customer))))))

(deftest ^:integration finalize-quote-test
  (testing "Finalize quote"
    (let [response (quotes/finalize-quote stripe-client "qt_mock")]
      (is (map? response))
      (is (= "quote" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration accept-quote-test
  (testing "Accept quote"
    (let [response (quotes/accept-quote stripe-client "qt_mock")]
      (is (map? response))
      (is (= "quote" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration cancel-quote-test
  (testing "Cancel quote"
    (let [response (quotes/cancel-quote stripe-client "qt_mock")]
      (is (map? response))
      (is (= "quote" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration list-line-items-test
  (testing "List quote line items"
    (let [response (quotes/list-line-items stripe-client "qt_mock" {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [item (:data response)]
        (is (map? item))
        (is (= "item" (:object item)))
        (is (string? (:id item)))))))

(deftest ^:integration list-computed-upfront-line-items-test
  (testing "List computed upfront quote line items"
    (let [response (quotes/list-computed-upfront-line-items stripe-client "qt_mock" {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [item (:data response)]
        (is (map? item))
        (is (= "item" (:object item)))
        (is (string? (:id item)))))))

;; (deftest ^:integration get-pdf-test
;;   (testing "Get quote PDF"
;;     (let [response (quotes/get-pdf "qt_mock")
;;           _ (println response)]
;;       (is (map? response))
;;       (is (= "file" (:object response)))
;;       (is (string? (:id response)))
;;       (is (contains? response :url))
;;       (is (string? (:url response)))))) 