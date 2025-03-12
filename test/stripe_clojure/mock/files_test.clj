(ns stripe-clojure.mock.files-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.files :as files]))

;; (deftest create-file-test
;;   (testing "Create file"
;;     (let [temp-file (io/file "test/resources/dummy.pdf")
;;           _ (spit temp-file "dummy content")
;;           file-path (.getAbsolutePath temp-file)
;;           purpose "dispute_evidence"
;;           response (files/create-file file-path purpose)]
;;       (is (map? response))
;;       (is (= "file" (:object response)))
;;       (is (string? (:id response)))
;;       (is (= purpose (:purpose response)))
;;       (is (number? (:created response))))))

(deftest retrieve-file-test
  (testing "Retrieve file"
    (let [response (files/retrieve-file stripe-mock-client "file_mock")]
      (is (map? response))
      (is (= "file" (:object response)))
      (is (string? (:id response)))
      (is (string? (:purpose response))))))

(deftest list-files-test
  (testing "List files"
    (let [response (files/list-files stripe-mock-client {:limit 2})]
      (is (map? response))
      (is (= "list" (:object response)))
      (is (vector? (:data response)))
      (doseq [f (:data response)]
        (is (map? f))
        (is (= "file" (:object f)))
        (is (string? (:id f))))))) 