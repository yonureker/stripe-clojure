(ns stripe-clojure.unit.files-test
  (:require [clojure.test :refer [deftest is testing]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.files :as files]
            [stripe-clojure.http.client :as client]
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

(deftest request-call-test
  (testing "retrieve-file and list-files call request correctly"
    (h/check-request-calls
     [{:api-fn files/retrieve-file
       :args ["file_123"]
       :method :get
       :endpoint (str (:files config/stripe-endpoints) "/file_123")
       :params {}
       :opts {}}
      {:api-fn files/retrieve-file
       :args ["file_123" {:stripe-account "acct_456"}]
       :method :get
       :endpoint (str (:files config/stripe-endpoints) "/file_123")
       :params {}
       :opts {:stripe-account "acct_456"}}
      {:api-fn files/list-files
       :args []
       :method :get
       :endpoint (:files config/stripe-endpoints)
       :params {}
       :opts {}}
      {:api-fn files/list-files
       :args [{:purpose "dispute_evidence"}]
       :method :get
       :endpoint (:files config/stripe-endpoints)
       :params {:purpose "dispute_evidence"}
       :opts {}}
      {:api-fn files/list-files
       :args [{:purpose "dispute_evidence"} {:auto-paginate? true}]
       :method :get
       :endpoint (:files config/stripe-endpoints)
       :params {:purpose "dispute_evidence"}
       :opts {:auto-paginate? true}}])))

(deftest create-file-request-call-test
  (testing "create-file calls request with :post, multipart, and files.stripe.com base-url"
    (let [captured (atom nil)]
      (with-redefs [stripe-clojure.http.client/request
                    (fn [_client m e p o]
                      (reset! captured {:method m :endpoint e :params p :opts o})
                      {:id "file_mock"})]
        (files/create-file :mock-client "/fake/path/document.pdf" "dispute_evidence"))
      (is (= :post (:method @captured)))
      (is (= (:files config/stripe-endpoints) (:endpoint @captured)))
      (is (= {} (:params @captured)))
      (is (= "https://files.stripe.com" (get-in @captured [:opts :base-url])))
      (let [multipart (get-in @captured [:opts :multipart])]
        (is (= 2 (count multipart)))
        (is (= "purpose" (:name (first multipart))))
        (is (= "dispute_evidence" (:content (first multipart))))
        (is (= "file" (:name (second multipart))))
        (is (= "application/pdf" (:content-type (second multipart)))))))

  (testing "create-file forwards opts and merges multipart/base-url"
    (let [captured (atom nil)]
      (with-redefs [stripe-clojure.http.client/request
                    (fn [_client m e p o]
                      (reset! captured {:method m :endpoint e :params p :opts o})
                      {:id "file_mock"})]
        (files/create-file :mock-client "/fake/path/photo.jpg" "identity_document"
                           {:stripe-account "acct_789"}))
      (is (= :post (:method @captured)))
      (is (= "https://files.stripe.com" (get-in @captured [:opts :base-url])))
      (is (= "acct_789" (get-in @captured [:opts :stripe-account])))
      (let [multipart (get-in @captured [:opts :multipart])]
        (is (= "identity_document" (:content (first multipart))))
        (is (= "image/jpeg" (:content-type (second multipart))))))))

(deftest extension-mime-type-map-test
  (testing "extension->mime-type contains expected mappings"
    (let [ext-map (deref #'files/extension->mime-type)]
      (is (= "application/pdf" (get ext-map "pdf")))
      (is (= "image/jpeg" (get ext-map "jpg")))
      (is (= "image/jpeg" (get ext-map "jpeg")))
      (is (= "image/png" (get ext-map "png")))
      (is (= "text/csv" (get ext-map "csv"))))))
