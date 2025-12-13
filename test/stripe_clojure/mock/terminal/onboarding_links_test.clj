(ns stripe-clojure.mock.terminal.onboarding-links-test
  (:require [stripe-clojure.test-util :refer [stripe-mock-client]]
            [clojure.test :refer [deftest is testing]]
            [stripe-clojure.terminal.onboarding-links :as onboarding-links]))

(deftest create-onboarding-link-test
  (testing "Create terminal onboarding link"
    (let [params {:link_type "apple_terms_and_conditions"
                  :link_options {:apple_terms_and_conditions {:merchant_display_name "Test Merchant"}}}
          response (onboarding-links/create-onboarding-link stripe-mock-client params)]
      (is (map? response))
      (is (= "terminal.onboarding_link" (:object response)))
      (is (string? (:redirect_url response))))))
