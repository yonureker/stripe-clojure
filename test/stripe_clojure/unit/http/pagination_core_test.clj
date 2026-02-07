(ns stripe-clojure.unit.http.pagination-core-test
  (:require [clojure.test :refer [deftest is testing]]
            [stripe-clojure.http.pagination.core :as pagination]
            [stripe-clojure.http.api-version :as api-version]))

(deftest get-paginator-test
  (testing "returns V1 paginator for V1 version"
    (let [paginator (pagination/get-paginator api-version/V1)]
      (is (some? paginator))
      (is (satisfies? stripe-clojure.http.pagination.protocol/Paginator paginator))))

  (testing "returns V2 paginator for V2 version"
    (let [paginator (pagination/get-paginator api-version/V2)]
      (is (some? paginator))
      (is (satisfies? stripe-clojure.http.pagination.protocol/Paginator paginator))))

  (testing "returns V1 paginator for unknown version"
    (let [paginator (pagination/get-paginator :unknown)]
      (is (some? paginator)))))

(deftest paginate-test
  (testing "calls make-request-fn when auto-paginate is false"
    (let [call-count (atom 0)
          mock-request-fn (fn [_params]
                            (swap! call-count inc)
                            {:data [{:id "item_1"}] :has_more false})]
      (pagination/paginate :get "/v1/customers" {} {} mock-request-fn)
      (is (= 1 @call-count))))

  (testing "returns lazy sequence when auto-paginate is true"
    (let [mock-request-fn (fn [_params]
                            {:data [{:id "item_1"}] :has_more false})]
      (let [result (pagination/paginate :get "/v1/customers" {} {:auto-paginate? true} mock-request-fn)]
        (is (seq? result))
        (is (= [{:id "item_1"}] (take 10 result))))))

  (testing "handles multiple pages with V1 pagination"
    (let [page-count (atom 0)
          mock-request-fn (fn [params]
                            (swap! page-count inc)
                            (if (nil? (:starting-after params))
                              {:data [{:id "item_1"} {:id "item_2"}] :has_more true}
                              {:data [{:id "item_3"}] :has_more false}))]
      (let [result (pagination/paginate :get "/v1/customers" {} {:auto-paginate? true} mock-request-fn)]
        (is (= 3 (count result)))
        (is (= ["item_1" "item_2" "item_3"] (map :id result)))
        (is (= 2 @page-count)))))

  (testing "handles single page with V2 pagination (no next_page_url)"
    (let [mock-request-fn (fn [_params]
                            {:data [{:id "evt_1"} {:id "evt_2"}]
                             :next_page_url nil})]
      (let [result (pagination/paginate :get "/v2/core/events" {} {:auto-paginate? true} mock-request-fn)]
        (is (seq? result))
        (is (= [{:id "evt_1"} {:id "evt_2"}] (vec result))))))

  (testing "handles multiple pages with V2 pagination"
    (let [page-count (atom 0)
          mock-request-fn (fn [params]
                            (swap! page-count inc)
                            (if (nil? (:_next-page-url params))
                              {:data [{:id "evt_1"}]
                               :next_page_url "https://api.stripe.com/v2/core/events?page=2"}
                              {:data [{:id "evt_2"}]
                               :next_page_url nil}))]
      (let [result (pagination/paginate :get "/v2/core/events" {} {:auto-paginate? true} mock-request-fn)]
        (is (= 2 (count result)))
        (is (= ["evt_1" "evt_2"] (map :id result)))
        (is (= 2 @page-count)))))

  (testing "V2 pagination terminates when next-page-url is nil in params"
    (let [mock-request-fn (fn [_params]
                            ;; Response has next_page_url but paginator returns params without _next-page-url
                            {:data [{:id "evt_1"}]
                             :next_page_url nil})]
      (let [result (pagination/paginate :get "/v2/core/events" {} {:auto-paginate? true} mock-request-fn)]
        (is (= 1 (count result)))
        (is (= ["evt_1"] (map :id result))))))

  (testing "uses first-page-result when provided instead of re-fetching"
    (let [call-count (atom 0)
          mock-request-fn (fn [_params]
                            (swap! call-count inc)
                            {:data [{:id "item_2"}] :has_more false})
          first-page {:data [{:id "item_1"}] :has_more true}]
      (let [result (pagination/paginate :get "/v1/customers" {} {:auto-paginate? true} mock-request-fn first-page)]
        (is (= ["item_1" "item_2"] (map :id result)))
        (is (= 1 @call-count) "Should only fetch page 2, not page 1"))))

  (testing "first-page-result skips initial request for single page"
    (let [call-count (atom 0)
          mock-request-fn (fn [_params]
                            (swap! call-count inc)
                            {:data [] :has_more false})
          first-page {:data [{:id "item_1"}] :has_more false}]
      (let [result (pagination/paginate :get "/v1/customers" {} {:auto-paginate? true} mock-request-fn first-page)]
        (is (= ["item_1"] (map :id result)))
        (is (= 0 @call-count) "Should not make any requests when first page has all data"))))

  (testing "first-page-result returned directly when auto-paginate is false"
    (let [call-count (atom 0)
          mock-request-fn (fn [_params]
                            (swap! call-count inc)
                            {:data [{:id "item_from_request"}]})
          first-page {:data [{:id "item_1"}]}]
      (let [result (pagination/paginate :get "/v1/customers" {} {} mock-request-fn first-page)]
        (is (= first-page result))
        (is (= 0 @call-count)))))

  (testing "V2 first-page-result skips initial request"
    (let [call-count (atom 0)
          mock-request-fn (fn [params]
                            (swap! call-count inc)
                            (if (:_next-page-url params)
                              {:data [{:id "evt_2"}] :next_page_url nil}
                              {:data [{:id "should_not_appear"}] :next_page_url nil}))
          first-page {:data [{:id "evt_1"}]
                      :next_page_url "https://api.stripe.com/v2/core/events?page=2"}]
      (let [result (pagination/paginate :get "/v2/core/events" {} {:auto-paginate? true} mock-request-fn first-page)]
        (is (= ["evt_1" "evt_2"] (map :id result)))
        (is (= 1 @call-count) "Should only fetch page 2, not re-fetch page 1"))))

  (testing "V2 first-page-result with single page skips all requests"
    (let [call-count (atom 0)
          mock-request-fn (fn [_params]
                            (swap! call-count inc)
                            {:data [] :next_page_url nil})
          first-page {:data [{:id "evt_1"}] :next_page_url nil}]
      (let [result (pagination/paginate :get "/v2/core/events" {} {:auto-paginate? true} mock-request-fn first-page)]
        (is (= ["evt_1"] (map :id result)))
        (is (= 0 @call-count) "Should not make any requests when first page has all data"))))

  (testing "V2 pagination passes _next-page-url in params for make-request-fn"
    (let [received-params (atom [])
          mock-request-fn (fn [params]
                            (swap! received-params conj params)
                            (if (= 1 (count @received-params))
                              {:data [{:id "evt_1"}]
                               :next_page_url "https://api.stripe.com/v2/core/events?page=2"}
                              {:data [{:id "evt_2"}]
                               :next_page_url nil}))]
      (let [result (pagination/paginate :get "/v2/core/events" {} {:auto-paginate? true} mock-request-fn)]
        (is (= ["evt_1" "evt_2"] (map :id result)))
        ;; Second call should receive _next-page-url
        (is (= "https://api.stripe.com/v2/core/events?page=2"
               (:_next-page-url (second @received-params))))))))
