(ns stripe-clojure.mock.tokens-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.tokens :as tokens]))

(deftest create-card-token-test
  (testing "Create card token"
    (let [params {:card {:number "4242424242424242"
                         :exp_month 12
                         :exp_year 2025
                         :cvc "123"}}
          response (tokens/create-card-token stripe-mock-client params)]
      (is (map? response))
      (is (= "token" (:object response)))
      (is (string? (:id response)))
      (is (map? (:card response)))
      (is (contains? (:card response) :brand))
      (is (string? (get-in response [:card :brand]))))))

(deftest create-bank-account-token-test
  (testing "Create bank account token"
    (let [params {:bank_account {:country "US"
                                 :currency "usd"
                                 :account_holder_name "Jenny Rosen"
                                 :account_holder_type "individual"
                                 :routing_number "110000000"
                                 :account_number "000123456789"}}
          response (tokens/create-bank-account-token stripe-mock-client params)]
      (is (map? response))
      (is (= "token" (:object response)))
      (is (string? (:id response))))))

(deftest create-pii-token-test
  (testing "Create pii token"
    (let [params {:pii {:id_number "000000000"}}
          response (tokens/create-pii-token stripe-mock-client params)]
      (is (map? response))
      (is (= "token" (:object response)))
      (is (string? (:id response))))))

(deftest create-account-token-test
  (testing "Create account token"
    (let [params {:account {:business_type "individual"
                            :individual {:first_name "John"
                                         :last_name "Doe"}}}
          response (tokens/create-account-token stripe-mock-client params)]
      (is (map? response))
      (is (= "token" (:object response)))
      (is (string? (:id response))))))

(deftest create-person-token-test
  (testing "Create person token"
    (let [params {:person {:first_name "John" :last_name "Doe" :relationship {:owner true}}}
          response (tokens/create-person-token stripe-mock-client params)]
      (is (map? response))
      (is (= "token" (:object response)))
      (is (= "card" (:type response)))
      (is (string? (:id response))))))

(deftest create-cvc-update-token-test
  (testing "Create cvc update token"
    (let [params {:cvc_update {:cvc "123"}}
          response (tokens/create-cvc-update-token stripe-mock-client params)]
      (is (map? response))
      (is (= "token" (:object response)))
      (is (string? (:id response))))))

(deftest retrieve-token-test
  (testing "Retrieve token"
    (let [response (tokens/retrieve-token stripe-mock-client "tok_mock")]
      (is (map? response))
      (is (= "token" (:object response)))
      (is (string? (:id response)))))) 