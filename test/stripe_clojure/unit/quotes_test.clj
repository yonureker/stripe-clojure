(ns stripe-clojure.unit.quotes-test
  (:require [clojure.test :refer [deftest is testing]]
            [stripe-clojure.http.client :as client]
            [stripe-clojure.quotes :as quotes]))

(defn- captured-opts
  "Calls get-pdf with the HTTP layer stubbed and returns the opts it sent."
  [& args]
  (let [captured (atom nil)]
    (with-redefs [client/request (fn [_client method endpoint _params opts]
                                   (reset! captured {:method method :endpoint endpoint :opts opts})
                                   (byte-array 0))]
      (apply quotes/get-pdf :mock-client args))
    @captured))

(deftest get-pdf-request-test
  (testing "get-pdf asks files.stripe.com for bytes"
    (is (= {:method :get
            :endpoint "/v1/quotes/qt_123/pdf"
            :opts {:base-url "https://files.stripe.com" :as :byte-array}}
           (captured-opts "qt_123"))))

  (testing "get-pdf forwards opts"
    (is (= {:base-url "https://files.stripe.com" :as :byte-array :stripe-account "acct_123"}
           (:opts (captured-opts "qt_123" {:stripe-account "acct_123"})))))

  (testing "a caller-supplied :base-url wins"
    (is (= "http://localhost:12111"
           (get-in (captured-opts "qt_123" {:base-url "http://localhost:12111"}) [:opts :base-url])))))
