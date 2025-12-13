(ns stripe-clojure.mock.balance-settings-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.balance-settings :as balance-settings]))

(deftest retrieve-balance-settings-test
  (testing "Retrieve balance settings"
    (let [response (balance-settings/retrieve-balance-settings stripe-mock-client)]
      (is (map? response))
      (is (= "balance_settings" (:object response))))))

(deftest update-balance-settings-test
  (testing "Update balance settings"
    (let [params {:payments {:settlement_timing {:delay_days 2}}}
          response (balance-settings/update-balance-settings stripe-mock-client params)]
      (is (map? response))
      (is (= "balance_settings" (:object response))))))
