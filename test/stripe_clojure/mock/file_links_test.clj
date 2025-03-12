(ns stripe-clojure.mock.file-links-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.file-links :as file-links]))

(deftest create-file-link-test
  (testing "Create file link"
    (let [params {:file "file_mock" :metadata {:order "123"}}
          response (file-links/create-file-link stripe-mock-client params)]
      (is (map? response))
      (is (= "file_link" (:object response)))
      (is (string? (:id response)))
      (is (= "file_mock" (:file response)))
      (is (map? (:metadata response))))))

(deftest retrieve-file-link-test
  (testing "Retrieve file link"
    (let [response (file-links/retrieve-file-link stripe-mock-client "fl_mock")]
      (is (map? response))
      (is (= "file_link" (:object response)))
      (is (string? (:id response)))
      (is (string? (:file response)))
      (is (contains? response :metadata)))))

(deftest update-file-link-test
  (testing "Update file link"
    (let [params {}
          response (file-links/update-file-link stripe-mock-client "fl_mock" params)]
      (is (map? response))
      (is (= "file_link" (:object response)))
      (is (string? (:id response))))))

(deftest list-file-links-test
  (testing "List file links"
    (let [response (file-links/list-file-links stripe-mock-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [fl (:data response)]
        (is (map? fl))
        (is (= "file_link" (:object fl)))
        (is (string? (:id fl))))))) 