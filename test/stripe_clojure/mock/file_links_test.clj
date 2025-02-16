(ns stripe-clojure.mock.file-links-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.file-links :as file-links]))

(deftest ^:integration create-file-link-test
  (testing "Create file link"
    (let [params {:file "file_mock" :metadata {:order "123"}}
          response (file-links/create-file-link stripe-client params)]
      (is (map? response))
      (is (= "file_link" (:object response)))
      (is (string? (:id response)))
      (is (= "file_mock" (:file response)))
      (is (map? (:metadata response))))))

(deftest ^:integration retrieve-file-link-test
  (testing "Retrieve file link"
    (let [response (file-links/retrieve-file-link stripe-client "fl_mock")]
      (is (map? response))
      (is (= "file_link" (:object response)))
      (is (string? (:id response)))
      (is (string? (:file response)))
      (is (contains? response :metadata)))))

(deftest ^:integration update-file-link-test
  (testing "Update file link"
    (let [params {}
          response (file-links/update-file-link stripe-client "fl_mock" params)]
      (is (map? response))
      (is (= "file_link" (:object response)))
      (is (string? (:id response))))))

(deftest ^:integration list-file-links-test
  (testing "List file links"
    (let [response (file-links/list-file-links stripe-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [fl (:data response)]
        (is (map? fl))
        (is (= "file_link" (:object fl)))
        (is (string? (:id fl))))))) 