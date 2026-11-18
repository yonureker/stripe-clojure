(ns stripe-clojure.unit.http.make-request-test
  "End-to-end tests of make-request with the HTTP send stubbed."
  (:require [clojure.test :refer [deftest is testing]]
            [stripe-clojure.http.request :as request]))

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
      (is (= "cus_1" (get-in (second sent) [:options :query-params "starting_after"]))))))
