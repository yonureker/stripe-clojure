(ns stripe-clojure.unit.files-test
  (:require [clojure.test :refer [deftest is testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.files :as files]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (h/check-endpoint config/stripe-endpoints :files "/v1/files")
  (testing "module-level endpoint matches config"
    (is (= "/v1/files" files/stripe-files-endpoint))))

(deftest function-existence-test
  (testing "all file functions exist"
    (h/check-functions-exist
     [#'files/create-file
      #'files/retrieve-file
      #'files/list-files])))

(deftest function-arities-test
  (h/check-function-arities
   [[#'files/create-file #{3 4}]
    [#'files/retrieve-file #{2 3}]
    [#'files/list-files #{1 2 3}]]))

(deftest docstrings-test
  (h/check-docstrings
   [[#'files/create-file #"stripe.com/docs"]
    [#'files/retrieve-file #"stripe.com/docs"]
    [#'files/list-files #"stripe.com/docs"]]))

(deftest mime-type-detection-test
  (testing "get-mime-type returns correct types"
    (let [get-mime-type #'files/get-mime-type]
      (is (= "application/pdf" (get-mime-type "document.pdf")))
      (is (= "image/jpeg" (get-mime-type "photo.jpg")))
      (is (= "image/jpeg" (get-mime-type "photo.jpeg")))
      (is (= "image/png" (get-mime-type "image.png")))
      (is (= "text/csv" (get-mime-type "data.csv")))))

  (testing "get-mime-type handles uppercase extensions"
    (let [get-mime-type #'files/get-mime-type]
      (is (= "application/pdf" (get-mime-type "DOCUMENT.PDF")))
      (is (= "image/jpeg" (get-mime-type "Photo.JPG")))))

  (testing "get-mime-type returns octet-stream for unknown types"
    (let [get-mime-type #'files/get-mime-type]
      (is (= "application/octet-stream" (get-mime-type "file.xyz")))
      (is (= "application/octet-stream" (get-mime-type "unknown.bin"))))))

(deftest extension-mime-type-map-test
  (testing "extension->mime-type contains expected mappings"
    (let [ext-map (deref #'files/extension->mime-type)]
      (is (= "application/pdf" (get ext-map "pdf")))
      (is (= "image/jpeg" (get ext-map "jpg")))
      (is (= "image/jpeg" (get ext-map "jpeg")))
      (is (= "image/png" (get ext-map "png")))
      (is (= "text/csv" (get ext-map "csv"))))))
