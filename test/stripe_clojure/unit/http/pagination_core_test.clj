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

(deftest is-paginated-endpoint-test
  (testing "recognizes V1 list endpoints"
    (is (pagination/is-paginated-endpoint? "https://api.stripe.com/v1/customers"))
    (is (pagination/is-paginated-endpoint? "https://api.stripe.com/v1/charges"))
    (is (pagination/is-paginated-endpoint? "https://api.stripe.com/v1/invoices"))
    (is (pagination/is-paginated-endpoint? "https://api.stripe.com/v1/subscriptions")))

  (testing "recognizes V1 list endpoints with trailing slash"
    (is (pagination/is-paginated-endpoint? "https://api.stripe.com/v1/customers/")))

  (testing "recognizes search endpoints"
    (is (pagination/is-paginated-endpoint? "https://api.stripe.com/v1/customers/search"))
    (is (pagination/is-paginated-endpoint? "https://api.stripe.com/v1/charges/search"))
    (is (pagination/is-paginated-endpoint? "https://api.stripe.com/v1/invoices/search")))

  (testing "recognizes V2 list endpoints"
    (is (pagination/is-paginated-endpoint? "https://api.stripe.com/v2/core/events"))
    (is (pagination/is-paginated-endpoint? "https://api.stripe.com/v2/core/accounts")))

  (testing "does not recognize nested resource endpoints"
    (is (not (pagination/is-paginated-endpoint? "https://api.stripe.com/v1/customers/cus_123")))
    (is (not (pagination/is-paginated-endpoint? "https://api.stripe.com/v1/customers/cus_123/sources")))))

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
        (is (= ["evt_1"] (map :id result)))))))
