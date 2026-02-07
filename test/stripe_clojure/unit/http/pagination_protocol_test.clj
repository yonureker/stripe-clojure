(ns stripe-clojure.unit.http.pagination-protocol-test
  (:require [clojure.test :refer [deftest is testing]]
            [stripe-clojure.http.pagination.protocol :as protocol]
            [stripe-clojure.http.pagination.v1 :as v1]
            [stripe-clojure.http.pagination.v2 :as v2]))

;; V1 Paginator Tests
(deftest v1-paginator-has-more-test
  (let [paginator (v1/create-paginator)]
    (testing "returns true when has_more is true"
      (is (protocol/has-more? paginator {:has_more true :data []})))

    (testing "returns true when has-more is true (kebab-case)"
      (is (protocol/has-more? paginator {:has-more true :data []})))

    (testing "returns false when has_more is false"
      (is (not (protocol/has-more? paginator {:has_more false :data []}))))

    (testing "returns false when has_more is missing"
      (is (not (protocol/has-more? paginator {:data []}))))))

(deftest v1-paginator-next-page-params-test
  (let [paginator (v1/create-paginator)]
    (testing "returns starting-after when more pages exist"
      (let [response {:has_more true :data [{:id "obj_1"} {:id "obj_2"} {:id "obj_3"}]}
            current-params {:limit 10}
            result (protocol/next-page-params paginator response current-params)]
        (is (= {:limit 10 :starting-after "obj_3"} result))))

    (testing "returns nil when no more pages"
      (let [response {:has_more false :data [{:id "obj_1"}]}
            result (protocol/next-page-params paginator response {})]
        (is (nil? result))))

    (testing "returns nil when data is empty"
      (let [response {:has_more true :data []}
            result (protocol/next-page-params paginator response {})]
        (is (nil? result))))))

(deftest v1-paginator-get-items-test
  (let [paginator (v1/create-paginator)]
    (testing "returns data from response"
      (let [items [{:id "obj_1"} {:id "obj_2"}]
            response {:has_more false :data items}]
        (is (= items (protocol/get-items paginator response)))))

    (testing "returns nil when data is missing"
      (is (nil? (protocol/get-items paginator {:has_more false}))))))

;; V2 Paginator Tests
(deftest v2-paginator-has-more-test
  (let [paginator (v2/create-paginator)]
    (testing "returns true when next_page_url is present"
      (is (protocol/has-more? paginator {:next_page_url "https://api.stripe.com/v2/core/events?page=2" :data []})))

    (testing "returns true when next-page-url is present (kebab-case)"
      (is (protocol/has-more? paginator {:next-page-url "https://api.stripe.com/v2/core/events?page=2" :data []})))

    (testing "returns false when next_page_url is nil"
      (is (not (protocol/has-more? paginator {:next_page_url nil :data []}))))

    (testing "returns false when next_page_url is missing"
      (is (not (protocol/has-more? paginator {:data []}))))))

(deftest v2-paginator-next-page-params-test
  (let [paginator (v2/create-paginator)]
    (testing "returns _next-page-url when more pages exist"
      (let [next-url "https://api.stripe.com/v2/core/events?page=2"
            response {:next_page_url next-url :data [{:id "evt_1"}]}
            result (protocol/next-page-params paginator response {:limit 10})]
        (is (= {:_next-page-url next-url} result))))

    (testing "returns nil when no more pages"
      (let [response {:next_page_url nil :data [{:id "evt_1"}]}
            result (protocol/next-page-params paginator response {})]
        (is (nil? result))))))

(deftest v2-paginator-get-items-test
  (let [paginator (v2/create-paginator)]
    (testing "returns data from response"
      (let [items [{:id "evt_1"} {:id "evt_2"}]
            response {:data items}]
        (is (= items (protocol/get-items paginator response)))))))

(deftest v2-next-page-url-test
  (testing "extracts next page URL from params"
    (is (= "https://api.stripe.com/v2/events?page=2"
           (v2/next-page-url {:_next-page-url "https://api.stripe.com/v2/events?page=2"}))))

  (testing "returns nil when not present"
    (is (nil? (v2/next-page-url {:limit 10})))))
