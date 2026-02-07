(ns stripe-clojure.unit.http.events-test
  (:require [clojure.test :refer [deftest is testing]]
            [stripe-clojure.http.events :as events]))

(deftest build-event-data-test
  (testing "Event data building with kebab-case transformation"
    (let [base-event {:method :post 
                      :url "/v1/customers"
                      :api-version "2024-12-18"
                      :request-start-time 1234567890}
          additional-event {:status 200 
                           :request-end-time 1234567900
                           :elapsed 10}
          result (events/build-event-data base-event additional-event true)]
      (is (= "POST" (:method result)))
      (is (= "/v1/customers" (:path result)))
      (is (= "2024-12-18" (:api-version result)))
      (is (= 1234567890 (:request-start-time result)))
      (is (= 200 (:status result)))
      (is (= 10 (:elapsed result)))))

  (testing "Event data building without kebab-case transformation"
    (let [base-event {:method :get 
                      :url "/v1/accounts"
                      :idempotency-key "idem_123"}
          additional-event {:request-id "req_456"}
          result (events/build-event-data base-event additional-event false)]
      (is (= "GET" (:method result)))
      (is (= "/v1/accounts" (:path result)))
      (is (= "idem_123" (:idempotency_key result)))
      (is (= "req_456" (:request_id result))))))

(deftest listener-management-test
  (testing "Add and remove event listeners"
    (let [client {:config {:listeners (atom [])}}
          handler1 (fn [_] (println "handler1"))
          handler2 (fn [_] (println "handler2"))]
      
      ;; Add listeners
      (events/add-listener client :request handler1)
      (events/add-listener client :response handler2)
      (is (= 2 (count @(-> client :config :listeners))))
      
      ;; Remove specific listener
      (events/remove-listener client :request handler1)
      (is (= 1 (count @(-> client :config :listeners))))
      (is (= :response (-> @(-> client :config :listeners) first :type)))))

  (testing "Invalid event type validation"
    (let [client {:config {:listeners (atom [])}}
          handler (fn [_] (println "test"))]
      (is (thrown-with-msg? 
           clojure.lang.ExceptionInfo 
           #"Unsupported event type"
           (events/add-listener client :invalid-type handler)))
      (is (thrown-with-msg? 
           clojure.lang.ExceptionInfo 
           #"Unsupported event type"
           (events/remove-listener client :invalid-type handler))))))

(deftest emit-event-test
  (testing "Event emission to registered listeners"
    (let [received-events (atom [])
          handler (fn [event-data] (swap! received-events conj event-data))
          listeners (atom [{:type :request :handler handler}])
          base-data {:method :post :url "/v1/test"}
          additional-data {:status 201}]
      
      (events/emit-event listeners :request base-data additional-data false)
      (is (= 1 (count @received-events)))
      (let [event (first @received-events)]
        (is (= "POST" (:method event)))
        (is (= "/v1/test" (:path event)))
        (is (= 201 (:status event))))))

  (testing "Event emission with listener exceptions"
    (let [good-events (atom [])
          good-handler (fn [event-data] (swap! good-events conj event-data))
          bad-handler (fn [_] (throw (Exception. "Handler failed")))
          listeners (atom [{:type :request :handler good-handler}
                          {:type :request :handler bad-handler}])
          base-data {:method :get :url "/v1/test"}]
      
      ;; Should not throw despite bad handler
      (is (nil? (events/emit-event listeners :request base-data {} false)))
      ;; Good handler should still receive event
      (is (= 1 (count @good-events)))))

  (testing "No emission when no listeners"
    (let [empty-listeners (atom [])]
      (is (nil? (events/emit-event empty-listeners :request {} {} false))))))