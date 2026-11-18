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

(deftest v1-paginator-search-next-page-params-test
  (let [paginator (v1/create-paginator)]
    (testing "search results continue with the next_page token"
      (let [response {:object "search_result" :has_more true :next_page "tok_2"
                      :data [{:id "cus_1"}]}
            result (protocol/next-page-params paginator response {:query "email:'a@b.c'" :limit 1})]
        (is (= {:query "email:'a@b.c'" :limit 1 :page "tok_2"} result))))

    (testing "search results continue with a kebab-case next-page token"
      (let [response {:has-more true :next-page "tok_2" :data [{:id "cus_1"}]}
            result (protocol/next-page-params paginator response {:query "q"})]
        (is (= {:query "q" :page "tok_2"} result))))

    (testing "the token replaces the one used for the previous page"
      (let [response {:has_more true :next_page "tok_3" :data [{:id "cus_2"}]}
            result (protocol/next-page-params paginator response {:query "q" :page "tok_2"})]
        (is (= {:query "q" :page "tok_3"} result))))

    (testing "a string \"page\" key from the caller is replaced"
      (let [response {:has_more true :next_page "tok_3" :data [{:id "cus_2"}]}
            result (protocol/next-page-params paginator response {"query" "q" "page" "tok_2"})]
        (is (= {"query" "q" :page "tok_3"} result))))

    (testing "returns nil when the search has no more pages"
      (let [response {:has_more false :next_page nil :data [{:id "cus_1"}]}]
        (is (nil? (protocol/next-page-params paginator response {:query "q"})))))))

(deftest v1-paginator-replaces-caller-cursor-test
  (let [paginator (v1/create-paginator)
        response {:has_more true :data [{:id "obj_2"}]}]
    (testing "a snake_case :starting_after from the caller is replaced"
      (is (= {:limit 1 :starting-after "obj_2"}
             (protocol/next-page-params paginator response {:limit 1 :starting_after "obj_1"}))))

    (testing "a string \"starting_after\" key from the caller is replaced"
      (is (= {:limit 1 :starting-after "obj_2"}
             (protocol/next-page-params paginator response {:limit 1 "starting_after" "obj_1"}))))

    (testing "a kebab-case :starting-after from the caller is replaced"
      (is (= {:limit 1 :starting-after "obj_2"}
             (protocol/next-page-params paginator response {:limit 1 :starting-after "obj_1"}))))))

(deftest v1-paginator-sorted-map-params-test
  (let [paginator (v1/create-paginator)
        response {:has_more true :data [{:id "obj_2"}]}]
    (testing "sorted-map params with keyword keys"
      (is (= {:limit 1 :starting-after "obj_2"}
             (protocol/next-page-params paginator response (sorted-map :limit 1))))
      (is (= {:limit 1 :starting-after "obj_2"}
             (protocol/next-page-params paginator response
                                        (sorted-map :limit 1 :starting_after "obj_1")))))

    (testing "sorted-map params with string keys"
      (is (= {"limit" 1 :starting-after "obj_2"}
             (protocol/next-page-params paginator response
                                        (sorted-map "limit" 1 "starting_after" "obj_1")))))

    (testing "sorted-map params on a search"
      (is (= {"query" "q" :page "tok_2"}
             (protocol/next-page-params paginator
                                        {:has_more true :next_page "tok_2" :data [{:id "cus_1"}]}
                                        (sorted-map "query" "q")))))))

(deftest v1-paginator-next-page-params-kebab-test
  (let [paginator (v1/create-paginator)]
    (testing "returns starting-after with kebab-case :has-more response"
      (let [response {:has-more true :data [{:id "obj_1"} {:id "obj_2"}]}
            result (protocol/next-page-params paginator response {:limit 5})]
        (is (= {:limit 5 :starting-after "obj_2"} result))))

    (testing "returns nil with kebab-case :has-more false"
      (let [response {:has-more false :data [{:id "obj_1"}]}
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

(deftest v2-paginator-next-page-params-kebab-test
  (let [paginator (v2/create-paginator)]
    (testing "returns _next-page-url with kebab-case :next-page-url response"
      (let [next-url "https://api.stripe.com/v2/core/events?page=3"
            response {:next-page-url next-url :data [{:id "evt_1"}]}
            result (protocol/next-page-params paginator response {:limit 10})]
        (is (= {:_next-page-url next-url} result))))

    (testing "returns nil with kebab-case :next-page-url nil"
      (let [response {:next-page-url nil :data [{:id "evt_1"}]}
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
