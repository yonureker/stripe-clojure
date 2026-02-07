(ns stripe-clojure.unit.delete-params-test
  "Regression tests ensuring all delete functions pass {} (not nil) as params.
   This covers the nil→{} bug fix across all affected resource files."
  (:require [clojure.test :refer [deftest]]
            [stripe-clojure.unit.test-helpers :as h]
            [stripe-clojure.accounts :as accounts]
            [stripe-clojure.coupons :as coupons]
            [stripe-clojure.products :as products]
            [stripe-clojure.plans :as plans]
            [stripe-clojure.subscriptions :as subscriptions]
            [stripe-clojure.invoice-items :as invoice-items]
            [stripe-clojure.subscription-items :as subscription-items]
            [stripe-clojure.tax-ids :as tax-ids]
            [stripe-clojure.customers :as customers]
            [stripe-clojure.radar.value-list-items :as value-list-items]
            [stripe-clojure.radar.value-lists :as value-lists]
            [stripe-clojure.terminal.configurations :as terminal-configs]
            [stripe-clojure.terminal.locations :as terminal-locations]
            [stripe-clojure.terminal.readers :as terminal-readers]
            [stripe-clojure.ephemeral-keys :as ephemeral-keys]
            [stripe-clojure.apple-pay.domains :as apple-pay-domains]
            [stripe-clojure.webhook-endpoints :as webhook-endpoints]
            [stripe-clojure.config :as config]))

;; Accounts delete functions

(deftest accounts-delete-params-test
  (h/check-request-calls
   [{:api-fn accounts/delete-account
     :args ["acct_123"]
     :method :delete
     :endpoint (str (:accounts config/stripe-endpoints) "/acct_123")
     :params {}
     :opts {}}
    {:api-fn accounts/delete-external-account
     :args ["acct_123" "ba_456"]
     :method :delete
     :endpoint (str (:accounts config/stripe-endpoints) "/acct_123/external_accounts/ba_456")
     :params {}
     :opts {}}
    {:api-fn accounts/delete-bank-account
     :args ["acct_123" "ba_789"]
     :method :delete
     :endpoint (str (:accounts config/stripe-endpoints) "/acct_123/bank_accounts/ba_789")
     :params {}
     :opts {}}
    {:api-fn accounts/delete-person
     :args ["acct_123" "person_456"]
     :method :delete
     :endpoint (str (:accounts config/stripe-endpoints) "/acct_123/persons/person_456")
     :params {}
     :opts {}}
    {:api-fn accounts/delete-people
     :args ["acct_123" "person_789"]
     :method :delete
     :endpoint (str (:accounts config/stripe-endpoints) "/acct_123/people/person_789")
     :params {}
     :opts {}}]))

;; Coupons

(deftest coupons-delete-params-test
  (h/check-request-calls
   [{:api-fn coupons/delete-coupon
     :args ["coupon_123"]
     :method :delete
     :endpoint (str (:coupons config/stripe-endpoints) "/coupon_123")
     :params {}
     :opts {}}]))

;; Products

(deftest products-delete-params-test
  (h/check-request-calls
   [{:api-fn products/delete-product
     :args ["prod_123"]
     :method :delete
     :endpoint (str (:products config/stripe-endpoints) "/prod_123")
     :params {}
     :opts {}}
    {:api-fn products/delete-feature
     :args ["prod_123" "feat_456"]
     :method :delete
     :endpoint (str (:products config/stripe-endpoints) "/prod_123/features/feat_456")
     :params {}
     :opts {}}]))

;; Plans

(deftest plans-delete-params-test
  (h/check-request-calls
   [{:api-fn plans/delete-plan
     :args ["plan_123"]
     :method :delete
     :endpoint (str (:plans config/stripe-endpoints) "/plan_123")
     :params {}
     :opts {}}]))

;; Subscriptions

(deftest subscriptions-delete-params-test
  (h/check-request-calls
   [{:api-fn subscriptions/cancel-subscription
     :args ["sub_123"]
     :method :delete
     :endpoint (str (:subscriptions config/stripe-endpoints) "/sub_123")
     :params {}
     :opts {}}
    {:api-fn subscriptions/delete-discount
     :args ["sub_123"]
     :method :delete
     :endpoint (str (:subscriptions config/stripe-endpoints) "/sub_123/discount")
     :params {}
     :opts {}}]))

;; Invoice items

(deftest invoice-items-delete-params-test
  (h/check-request-calls
   [{:api-fn invoice-items/delete-invoice-item
     :args ["ii_123"]
     :method :delete
     :endpoint (str (:invoice-items config/stripe-endpoints) "/ii_123")
     :params {}
     :opts {}}]))

;; Subscription items

(deftest subscription-items-delete-params-test
  (h/check-request-calls
   [{:api-fn subscription-items/delete-subscription-item
     :args ["si_123"]
     :method :delete
     :endpoint (str (:subscription-items config/stripe-endpoints) "/si_123")
     :params {}
     :opts {}}]))

;; Tax IDs

(deftest tax-ids-delete-params-test
  (h/check-request-calls
   [{:api-fn tax-ids/delete-tax-id
     :args ["txi_123"]
     :method :delete
     :endpoint (str (:tax-ids config/stripe-endpoints) "/txi_123")
     :params {}
     :opts {}}]))

;; Customers delete functions

(deftest customers-delete-params-test
  (h/check-request-calls
   [{:api-fn customers/delete-customer
     :args ["cus_123"]
     :method :delete
     :endpoint (str (:customers config/stripe-endpoints) "/cus_123")
     :params {}
     :opts {}}
    {:api-fn customers/delete-source
     :args ["cus_123" "src_456"]
     :method :delete
     :endpoint (str (:customers config/stripe-endpoints) "/cus_123/sources/src_456")
     :params {}
     :opts {}}
    {:api-fn customers/delete-discount
     :args ["cus_123"]
     :method :delete
     :endpoint (str (:customers config/stripe-endpoints) "/cus_123/discount")
     :params {}
     :opts {}}
    {:api-fn customers/delete-tax-id
     :args ["cus_123" "txi_456"]
     :method :delete
     :endpoint (str (:customers config/stripe-endpoints) "/cus_123/tax_ids/txi_456")
     :params {}
     :opts {}}
    {:api-fn customers/delete-bank-account
     :args ["cus_123" "ba_456"]
     :method :delete
     :endpoint (str (:customers config/stripe-endpoints) "/cus_123/bank_accounts/ba_456")
     :params {}
     :opts {}}
    {:api-fn customers/delete-card
     :args ["cus_123" "card_456"]
     :method :delete
     :endpoint (str (:customers config/stripe-endpoints) "/cus_123/cards/card_456")
     :params {}
     :opts {}}]))

;; Radar

(deftest radar-delete-params-test
  (h/check-request-calls
   [{:api-fn value-list-items/delete-value-list-item
     :args ["rsli_123"]
     :method :delete
     :endpoint (str (:radar-value-list-items config/stripe-endpoints) "/rsli_123")
     :params {}
     :opts {}}
    {:api-fn value-lists/delete-value-list
     :args ["rsl_123"]
     :method :delete
     :endpoint (str (:radar-value-lists config/stripe-endpoints) "/rsl_123")
     :params {}
     :opts {}}]))

;; Terminal

(deftest terminal-delete-params-test
  (h/check-request-calls
   [{:api-fn terminal-configs/delete-configuration
     :args ["tmc_123"]
     :method :delete
     :endpoint (str (:terminal-configurations config/stripe-endpoints) "/tmc_123")
     :params {}
     :opts {}}
    {:api-fn terminal-locations/delete-location
     :args ["tml_123"]
     :method :delete
     :endpoint (str (:terminal-locations config/stripe-endpoints) "/tml_123")
     :params {}
     :opts {}}
    {:api-fn terminal-readers/delete-reader
     :args ["tmr_123"]
     :method :delete
     :endpoint (str (:terminal-readers config/stripe-endpoints) "/tmr_123")
     :params {}
     :opts {}}]))

;; Ephemeral keys

(deftest ephemeral-keys-delete-params-test
  (h/check-request-calls
   [{:api-fn ephemeral-keys/delete-ephemeral-key
     :args ["ephkey_123"]
     :method :delete
     :endpoint (str (:ephemeral-keys config/stripe-endpoints) "/ephkey_123")
     :params {}
     :opts {}}]))

;; Apple Pay domains

(deftest apple-pay-domains-delete-params-test
  (h/check-request-calls
   [{:api-fn apple-pay-domains/delete-apple-pay-domain
     :args ["apwc_123"]
     :method :delete
     :endpoint (str (:apple-pay-domains config/stripe-endpoints) "/apwc_123")
     :params {}
     :opts {}}]))

;; Webhook endpoints

(deftest webhook-endpoints-delete-params-test
  (h/check-request-calls
   [{:api-fn webhook-endpoints/delete-webhook-endpoint
     :args ["we_123"]
     :method :delete
     :endpoint (str (:webhook-endpoints config/stripe-endpoints) "/we_123")
     :params {}
     :opts {}}]))
