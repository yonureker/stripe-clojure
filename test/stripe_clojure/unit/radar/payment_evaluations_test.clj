(ns stripe-clojure.unit.radar.payment-evaluations-test
  (:require [clojure.test :refer [deftest is testing]]
            [stripe-clojure.http.client :as client]
            [stripe-clojure.radar.payment-evaluations :as pe]
            [stripe-clojure.config :as config]))

(deftest endpoint-configuration-test
  (testing "endpoint is correctly configured"
    (is (= "/v1/radar/payment_evaluations"
           (:radar-payment-evaluations config/stripe-endpoints)))))

(deftest create-payment-evaluation-test
  (testing "create-payment-evaluation calls request with correct params"
    (let [captured (atom nil)
          params {:customer_details {:customer "cus_123"}
                  :payment_details {:payment_method "pm_123"}}]
      (with-redefs [client/request
                    (fn [_client m e p o]
                      (reset! captured {:method m :endpoint e :params p :opts o})
                      {:id "mock_result"})]
        (pe/create-payment-evaluation :mock-client params))
      (is (= :post (:method @captured)))
      (is (= "/v1/radar/payment_evaluations" (:endpoint @captured)))
      (is (= params (:params @captured)))
      (is (= {} (:opts @captured))))))

(deftest create-payment-evaluation-with-opts-test
  (testing "create-payment-evaluation forwards opts"
    (let [captured (atom nil)
          params {:customer_details {:customer "cus_123"}
                  :payment_details {:payment_method "pm_123"}}]
      (with-redefs [client/request
                    (fn [_client m e p o]
                      (reset! captured {:method m :endpoint e :params p :opts o})
                      {:id "mock_result"})]
        (pe/create-payment-evaluation :mock-client params {:idempotency-key "key_1"}))
      (is (= :post (:method @captured)))
      (is (= "/v1/radar/payment_evaluations" (:endpoint @captured)))
      (is (= params (:params @captured)))
      (is (= {:idempotency-key "key_1"} (:opts @captured))))))
