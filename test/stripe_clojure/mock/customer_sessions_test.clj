(ns stripe-clojure.mock.customer-sessions-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.customer-sessions :as cs]))

(deftest ^:integration create-customer-session-test
  (testing "Create customer session"
    (let [params {:customer "cus_mock"
                  :components {:payment_element {:enabled true}}}
          response (cs/create-customer-session stripe-client params)]
      (is (map? response))
      (is (= "customer_session" (:object response)))
      (is (= "cus_mock" (:customer response)))
      (is (boolean? (:livemode response)))
      (is (number? (:created response))))))
