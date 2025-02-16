(ns stripe-clojure.mock.tax.settings-test
  (:require [stripe-clojure.test-util :refer [stripe-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.tax.settings :as settings]))

(deftest ^:integration retrieve-settings-test
  (testing "Retrieve tax settings"
    (let [response (settings/retrieve-settings stripe-client)]
      (is (map? response))
      (is (= "tax.settings" (:object response))))))

(deftest ^:integration update-settings-test
  (testing "Update tax settings"
    (let [params {:defaults {:tax_code "txcd_mock"}}
          response (settings/update-settings stripe-client params)]
      (is (map? response))
      (is (= "tax.settings" (:object response)))))) 