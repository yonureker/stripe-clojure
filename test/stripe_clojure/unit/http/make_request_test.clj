(ns stripe-clojure.unit.http.make-request-test
  "End-to-end tests of make-request with the HTTP send stubbed."
  (:require [clojure.test :refer [deftest is testing]]
            [stripe-clojure.http.request :as request]
            [stripe-clojure.http.throttle :as throttle]))

(def ^:private config
  {:api-key "sk_test_123"
   :api-version "2026-10-28.endive"
   :protocol "https"
   :host "api.stripe.com"
   :port 443
   :max-network-retries 0})

(defn- ok [body]
  {:status 200 :headers {"request-id" "req_123"} :body body})

(defn- with-stubbed-responses
  "Runs `f` with send-stripe-api-request returning `responses` in order.
   Returns the result of `f` (realized when lazy) and the requests sent."
  [responses f]
  (let [queue (atom responses)
        sent (atom [])]
    (with-redefs [request/send-stripe-api-request
                  (fn [method url options]
                    (swap! sent conj {:method method :url url :options options})
                    (let [[remaining] (swap-vals! queue rest)]
                      (first remaining)))]
      (let [result (f)]
        {:result (cond-> result (seq? result) doall)
         :sent @sent}))))

(deftest v2-auto-pagination-test
  (testing "follows a relative next_page_url on the API host"
    (let [{:keys [result sent]}
          (with-stubbed-responses
            [(ok {:data [{:id "evt_1"}] :next_page_url "/v2/core/events?page=abc&limit=1"})
             (ok {:data [{:id "evt_2"}] :next_page_url nil})]
            #(request/make-request :get "/v2/core/events" {:limit 1} {:auto-paginate? true} config))]
      (is (= ["evt_1" "evt_2"] (map :id result)))
      (is (= ["https://api.stripe.com/v2/core/events"
              "https://api.stripe.com/v2/core/events?page=abc&limit=1"]
             (map :url sent)))
      (is (= {"limit" 1} (-> sent first :options :query-params)))
      (is (nil? (-> sent second :options :query-params))
          "the page URL already carries the params")))

  (testing "follows an absolute next_page_url as is"
    (let [{:keys [result sent]}
          (with-stubbed-responses
            [(ok {:data [{:id "evt_1"}] :next_page_url "https://api.stripe.com/v2/core/events?page=abc"})
             (ok {:data [{:id "evt_2"}] :next_page_url nil})]
            #(request/make-request :get "/v2/core/events" {} {:auto-paginate? true} config))]
      (is (= ["evt_1" "evt_2"] (map :id result)))
      (is (= "https://api.stripe.com/v2/core/events?page=abc" (-> sent second :url)))))

  (testing "resolves a relative next_page_url against a custom :base-url"
    (let [{:keys [sent]}
          (with-stubbed-responses
            [(ok {:data [{:id "evt_1"}] :next_page_url "/v2/core/events?page=abc"})
             (ok {:data [{:id "evt_2"}] :next_page_url nil})]
            #(request/make-request :get "/v2/core/events" {}
                                   {:auto-paginate? true :base-url "http://localhost:12111"}
                                   config))]
      (is (= "http://localhost:12111/v2/core/events?page=abc" (-> sent second :url)))))

  (testing "follows kebab-case next-page-url when keys are kebabified"
    (let [{:keys [result]}
          (with-stubbed-responses
            [(ok {:data [{:id "evt_1"}] :next_page_url "/v2/core/events?page=abc"})
             (ok {:data [{:id "evt_2"}] :next_page_url nil})]
            #(request/make-request :get "/v2/core/events" {} {:auto-paginate? true}
                                   (assoc config :kebabify-keys? true)))]
      (is (= ["evt_1" "evt_2"] (map :id result))))))

(deftest search-auto-pagination-test
  (testing "search pages are requested with the page token, not starting_after"
    (let [{:keys [result sent]}
          (with-stubbed-responses
            [(ok {:object "search_result" :data [{:id "cus_1"}] :has_more true :next_page "tok_2"})
             (ok {:object "search_result" :data [{:id "cus_2"}] :has_more false :next_page nil})]
            #(request/make-request :get "/v1/customers/search" {:query "name:'Jane'" :limit 1}
                                   {:auto-paginate? true} config))]
      (is (= ["cus_1" "cus_2"] (map :id result)))
      (is (= {"query" "name:'Jane'" "limit" 1 "page" "tok_2"}
             (-> sent second :options :query-params))))))

(deftest list-auto-pagination-cursor-test
  (testing "a starting_after passed for the first page does not leak into later pages"
    (let [params (into {:starting_after "cus_0"} (map (fn [n] [(keyword (str "filter" n)) n])) (range 9))
          {:keys [result sent]}
          (with-stubbed-responses
            [(ok {:data [{:id "cus_1"}] :has_more true})
             (ok {:data [{:id "cus_2"}] :has_more false})]
            #(request/make-request :get "/v1/customers" params {:auto-paginate? true} config))]
      (is (= ["cus_1" "cus_2"] (map :id result)))
      (is (= "cus_0" (get-in (first sent) [:options :query-params "starting_after"])))
      (is (= "cus_1" (get-in (second sent) [:options :query-params "starting_after"])))))

  (testing "sorted-map params paginate like any other map"
    (let [{:keys [result sent]}
          (with-stubbed-responses
            [(ok {:data [{:id "cus_1"}] :has_more true})
             (ok {:data [{:id "cus_2"}] :has_more false})]
            #(request/make-request :get "/v1/customers" (sorted-map :limit 1 :starting_after "cus_0")
                                   {:auto-paginate? true} config))]
      (is (= ["cus_1" "cus_2"] (map :id result)))
      (is (= {"limit" 1 "starting_after" "cus_1"} (-> sent second :options :query-params))))))

(deftest auto-pagination-full-response-test
  (testing "auto-pagination yields items even when full-response? is set"
    (let [{:keys [result]}
          (with-stubbed-responses
            [(ok {:data [{:id "cus_1"}] :has_more true})
             (ok {:data [{:id "cus_2"}] :has_more false})]
            #(request/make-request :get "/v1/customers" {:limit 1}
                                   {:auto-paginate? true :full-response? true} config))]
      (is (= [{:id "cus_1"} {:id "cus_2"}] result))))

  (testing "full-response? still returns the envelope without auto-pagination"
    (let [{:keys [result]}
          (with-stubbed-responses
            [(ok {:data [{:id "cus_1"}] :has_more true})]
            #(request/make-request :get "/v1/customers" {:limit 1} {:full-response? true} config))]
      (is (= 200 (:status result)))
      (is (= [{:id "cus_1"}] (get-in result [:body :data]))))))

(deftest per-request-kebabify-keys-test
  (testing ":kebabify-keys? can be turned on for a single request"
    (let [{:keys [result]}
          (with-stubbed-responses
            [(ok {:id "cus_1" :default_source nil :invoice_settings {:default_payment_method "pm_1"}})]
            #(request/make-request :get "/v1/customers/cus_1" {} {:kebabify-keys? true} config))]
      (is (= {:id "cus_1" :default-source nil :invoice-settings {:default-payment-method "pm_1"}}
             result))))

  (testing ":kebabify-keys? can be turned off for a single request"
    (let [{:keys [result]}
          (with-stubbed-responses
            [(ok {:id "cus_1" :default_source nil})]
            #(request/make-request :get "/v1/customers/cus_1" {} {:kebabify-keys? false}
                                   (assoc config :kebabify-keys? true)))]
      (is (= {:id "cus_1" :default_source nil} result)))))

(defn- recorded-events
  "Makes a stubbed request with listeners attached and returns the request
   and response events it emitted."
  [method url params opts config]
  (let [events (atom [])
        record #(swap! events conj %)
        listeners (atom [{:type :request :handler record}
                         {:type :response :handler record}])]
    (with-stubbed-responses
      [(ok {:id "cus_1"})]
      #(request/make-request method url params opts (assoc config :listeners listeners)))
    @events))

(deftest event-fields-test
  (testing "events carry the idempotency key that was sent"
    (let [[request-event response-event]
          (recorded-events :post "/v1/customers" {:name "Jane"} {:idempotency-key "idem_123"} config)]
      (is (= "idem_123" (:idempotency_key request-event)))
      (is (= "idem_123" (:idempotency_key response-event)))))

  (testing "events carry the generated idempotency key of a POST"
    (let [[request-event] (recorded-events :post "/v1/customers" {:name "Jane"} {} config)]
      (is (string? (:idempotency_key request-event)))))

  (testing "events of a GET have no idempotency key"
    (let [[request-event response-event] (recorded-events :get "/v1/customers" {} {} config)]
      (is (not (contains? request-event :idempotency_key)))
      (is (not (contains? response-event :idempotency_key)))))

  (testing "events carry the account only when one is set"
    (let [[without-account] (recorded-events :get "/v1/customers" {} {} config)
          [with-account] (recorded-events :get "/v1/customers" {} {:stripe-account "acct_123"} config)]
      (is (not (contains? without-account :account)))
      (is (= "acct_123" (:account with-account)))))

  (testing "the response event reports status, request id and timing"
    (let [[_ response-event] (recorded-events :get "/v1/customers" {} {} config)]
      (is (= 200 (:status response-event)))
      (is (= "req_123" (:request_id response-event)))
      (is (nat-int? (:elapsed response-event))))))

(deftest auto-pagination-instrumentation-test
  (let [pages [(ok {:data [{:id "cus_1"}] :has_more true})
               (ok {:data [{:id "cus_2"}] :has_more true})
               (ok {:data [{:id "cus_3"}] :has_more false})]
        paginate #(request/make-request :get "/v1/customers" {:limit 1} {:auto-paginate? true} %)]
    (testing "every page emits a request and a response event"
      (let [events (atom [])
            listeners (atom [{:type :request :handler #(swap! events conj [:request (:path %)])}
                             {:type :response :handler #(swap! events conj [:response (:status %)])}])]
        (with-stubbed-responses pages #(paginate (assoc config :listeners listeners)))
        (is (= [[:request "/v1/customers"] [:response 200]
                [:request "/v1/customers"] [:response 200]
                [:request "/v1/customers"] [:response 200]]
               @events))))

    (testing "every page goes through the throttler"
      (let [throttled (atom [])]
        (with-redefs [throttle/with-throttling (fn [_throttler method url _api-key f]
                                                 (swap! throttled conj [method url])
                                                 (f))]
          (with-stubbed-responses pages #(paginate config)))
        (is (= (repeat 3 [:get "https://api.stripe.com/v1/customers"]) @throttled))))

    (testing "pages wait for the rate limit"
      (let [throttler (throttle/create-throttler {:test {:default {:read 2}}})
            start (System/currentTimeMillis)]
        (with-stubbed-responses pages #(paginate (assoc config :throttler throttler)))
        (is (>= (- (System/currentTimeMillis) start) 400)
            "the third page waits for the two-per-second bucket to refill")))))

(deftest pagination-error-test
  (let [pages [(ok {:data [{:id "cus_1"}] :has_more true})
               {:status 429
                :headers {"request-id" "req_429"}
                :body "{\"error\":{\"type\":\"rate_limit_error\",\"message\":\"Too many requests\"}}"}
               (ok {:data [{:id "cus_3"}] :has_more false})]
        paginate (fn [opts]
                   #(request/make-request :get "/v1/customers" {:limit 1}
                                          (assoc opts :auto-paginate? true) config))]
    (testing "by default an error page ends the sequence"
      (let [{:keys [result sent]} (with-stubbed-responses pages (paginate {}))]
        (is (= [{:id "cus_1"}] result))
        (is (= 2 (count sent)) "no page is requested after the error")))

    (testing ":truncate is the default behavior"
      (let [{:keys [result]} (with-stubbed-responses pages (paginate {:on-pagination-error :truncate}))]
        (is (= [{:id "cus_1"}] result))))

    (testing ":throw raises the error map when the failed page is reached"
      (let [error (try
                    (with-stubbed-responses pages (paginate {:on-pagination-error :throw}))
                    (catch clojure.lang.ExceptionInfo e e))]
        (is (= "Auto-pagination failed: Too many requests" (ex-message error)))
        (is (= {:type "rate_limit_error"
                :message "Too many requests"
                :status 429
                :request-id "req_429"}
               (ex-data error)))))

    (testing ":throw also raises an error on the first page"
      (is (thrown-with-msg? clojure.lang.ExceptionInfo #"Auto-pagination failed"
                            (with-stubbed-responses (rest pages)
                              (paginate {:on-pagination-error :throw})))))

    (testing ":throw changes nothing when every page succeeds"
      (let [{:keys [result]} (with-stubbed-responses
                               [(first pages) (last pages)]
                               (paginate {:on-pagination-error :throw}))]
        (is (= [{:id "cus_1"} {:id "cus_3"}] result))))))
