(ns stripe-clojure.mock.credit-notes-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.credit-notes :as cn]))

(deftest ^:integration create-credit-note-test
  (testing "Create credit note"
    (let [params {:invoice "in_mock"
                  :amount 100}
          response (cn/create-credit-note stripe-client params)]
      (is (map? response))
      (is (= "credit_note" (:object response)))
      (is (string? (:id response)))
      (is (= "in_mock" (:invoice response)))
      (is (= 100 (:amount response))))))

(deftest ^:integration retrieve-credit-note-test
  (testing "Retrieve credit note"
    (let [response (cn/retrieve-credit-note stripe-client "cn_mock")]
      (is (map? response))
      (is (= "credit_note" (:object response)))
      (is (string? (:id response)))
      (is (contains? response :amount))
      (is (number? (:amount response))))))

(deftest ^:integration update-credit-note-test
  (testing "Update credit note"
    (let [params {:memo "test_memo"}
          response (cn/update-credit-note stripe-client "cn_mock" params)]
      (is (map? response))
      (is (= "credit_note" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration list-credit-notes-test
  (testing "List credit notes"
    (let [response (cn/list-credit-notes stripe-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [note (:data response)]
        (is (map? note))
        (is (= "credit_note" (:object note)))
        (is (string? (:id note)))))))

(deftest ^:integration void-credit-note-test
  (testing "Void credit note"
    (let [response (cn/void-credit-note stripe-client "cn_mock")]
      (is (map? response))
      (is (= "credit_note" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration preview-credit-note-test
  (testing "Preview credit note"
    (let [params {:invoice "in_mock" :amount 50}
          response (cn/preview-credit-note stripe-client params)]
      (is (map? response))
      (is (= "credit_note" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration list-line-items-test
  (testing "List credit note line items"
    (let [response (cn/list-line-items stripe-client "cn_mock" {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [item (:data response)]
        (is (map? item))
        (is (= "credit_note_line_item" (:object item)))
        (is (string? (:id item)))))))

(deftest ^:integration preview-lines-test
  (testing "Preview credit note lines"
    (let [params {:invoice "ch_mock" :amount 50}
          response (cn/preview-lines stripe-client params)]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [item (:data response)]
        (is (map? item))
        (is (= "credit_note_line_item" (:object item)))
        (is (string? (:id item)))))))
