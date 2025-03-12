(ns stripe-clojure.mock.quotes-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.quotes :as quotes]))

(deftest create-quote-test
  (testing "Create quote"
    (let [params {:customer "cus_mock" :description "Test quote"}
          response (quotes/create-quote stripe-mock-client params)]
      (is (map? response))
      (is (= "quote" (:object response)))
      (is (string? (:id response)))
      (is (= "cus_mock" (:customer response)))
      (is (= "Test quote" (:description response))))))

(deftest retrieve-quote-test
  (testing "Retrieve quote"
    (let [response (quotes/retrieve-quote stripe-mock-client "qt_mock")]
      (is (map? response))
      (is (= "quote" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :customer)))))

(deftest update-quote-test
  (testing "Update quote"
    (let [params {:description "Updated quote"}
          response (quotes/update-quote stripe-mock-client "qt_mock" params)]
      (is (map? response))
      (is (= "quote" (:object response)))
      (is (string? (:id response)))
      (is (= "Updated quote" (:description response))))))

(deftest list-quotes-test
  (testing "List quotes"
    (let [response (quotes/list-quotes stripe-mock-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [q (:data response)]
        (is (map? q))
        (is (= "quote" (:object q)))
        (is (string? (:id q)))
        (is (contains? q :customer))))))

(deftest finalize-quote-test
  (testing "Finalize quote"
    (let [response (quotes/finalize-quote stripe-mock-client "qt_mock")]
      (is (map? response))
      (is (= "quote" (:object response)))
      (is (string? (:id response))))))

(deftest accept-quote-test
  (testing "Accept quote"
    (let [response (quotes/accept-quote stripe-mock-client "qt_mock")]
      (is (map? response))
      (is (= "quote" (:object response)))
      (is (string? (:id response))))))

(deftest cancel-quote-test
  (testing "Cancel quote"
    (let [response (quotes/cancel-quote stripe-mock-client "qt_mock")]
      (is (map? response))
      (is (= "quote" (:object response)))
      (is (string? (:id response))))))

(deftest list-line-items-test
  (testing "List quote line items"
    (let [response (quotes/list-line-items stripe-mock-client "qt_mock" {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [item (:data response)]
        (is (map? item))
        (is (= "item" (:object item)))
        (is (string? (:id item)))))))

(deftest list-computed-upfront-line-items-test
  (testing "List computed upfront quote line items"
    (let [response (quotes/list-computed-upfront-line-items stripe-mock-client "qt_mock" {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [item (:data response)]
        (is (map? item))
        (is (= "item" (:object item)))
        (is (string? (:id item)))))))

;; (deftest get-pdf-test
;;   (testing "Get quote PDF"
;;     (let [response (quotes/get-pdf "qt_mock")
;;           _ (println response)]
;;       (is (map? response))
;;       (is (= "file" (:object response)))
;;       (is (string? (:id response)))
;;       (is (contains? response :url))
;;       (is (string? (:url response)))))) 