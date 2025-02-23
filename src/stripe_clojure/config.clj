(ns stripe-clojure.config
  (:require [clojure.core :as str]))

;; Default Configuration
(def api-keys
  {:test (or (System/getenv "STRIPE_TEST_API_KEY") "test_api_key")})

;; API Endpoints
(def stripe-api-namespace "v1")

(def stripe-endpoints
  {:account-links (str "/" stripe-api-namespace "/account_links")
   :accounts (str "/" stripe-api-namespace "/accounts")
   :application-fees (str "/" stripe-api-namespace "/application_fees")
   :apps-secrets (str "/" stripe-api-namespace "/apps/secrets")
   :balance (str "/" stripe-api-namespace "/balance")
   :balance-transactions (str "/" stripe-api-namespace "/balance_transactions")
   :billing-alerts (str "/" stripe-api-namespace "/billing/alerts")
   :billing-credit-balance-summary (str "/" stripe-api-namespace "/billing/credit_balance_summary")
   :billing-credit-balance-transactions (str "/" stripe-api-namespace "/billing/credit_balance_transactions")
   :billing-credit-grants (str "/" stripe-api-namespace "/billing/credit_grants")
   :billing-meter-event-adjustments (str "/" stripe-api-namespace "/billing/meter_event_adjustments")
   :billing-meter-events (str "/" stripe-api-namespace "/billing/meter_events")
   :billing-meters (str "/" stripe-api-namespace "/billing/meters")
   :billing-portal-sessions (str "/" stripe-api-namespace "/billing_portal/sessions")
   :billing-portal-configurations (str "/" stripe-api-namespace "/billing_portal/configurations")
   :charges (str "/" stripe-api-namespace "/charges")
   :checkout-sessions (str "/" stripe-api-namespace "/checkout/sessions")
   :climate-orders (str "/" stripe-api-namespace "/climate/orders")
   :climate-products (str "/" stripe-api-namespace "/climate/products")
   :climate-suppliers (str "/" stripe-api-namespace "/climate/suppliers")
   :confirmation-tokens (str "/" stripe-api-namespace "/confirmation_tokens")
   :coupons (str "/" stripe-api-namespace "/coupons")
   :credit-notes (str "/" stripe-api-namespace "/credit_notes")
   :crypto-onramp-sessions (str "/" stripe-api-namespace "/crypto/onramp_sessions")
   :crypto-onramp-quotes (str "/" stripe-api-namespace "/crypto/onramp_quotes")
   :customer-sessions (str "/" stripe-api-namespace "/customer_sessions")
   :customers (str "/" stripe-api-namespace "/customers")
   :disputes (str "/" stripe-api-namespace "/disputes")
   :entitlements-active-entitlements (str "/" stripe-api-namespace "/entitlements/active_entitlements")
   :entitlements-features (str "/" stripe-api-namespace "/entitlements/features")
   :ephemeral-keys (str "/" stripe-api-namespace "/ephemeral_keys")
   :events (str "/" stripe-api-namespace "/events")
   :files (str "/" stripe-api-namespace "/files")
   :file-links (str "/" stripe-api-namespace "/file_links")
   :financial-connections-accounts (str "/" stripe-api-namespace "/financial_connections/accounts")
   :financial-connections-sessions (str "/" stripe-api-namespace "/financial_connections/sessions")
   :financial-connections-transactions (str "/" stripe-api-namespace "/financial_connections/transactions")
   :forwarding-requests (str "/" stripe-api-namespace "/forwarding/requests")
   :identity-verification-reports (str "/" stripe-api-namespace "/identity/verification_reports")
   :identity-verification-sessions (str "/" stripe-api-namespace "/identity/verification_sessions")
   :invoices (str "/" stripe-api-namespace "/invoices")
   :invoice-items (str "/" stripe-api-namespace "/invoiceitems")
   :invoice-rendering-templates (str "/" stripe-api-namespace "/invoice_rendering_templates")
   :issuing-authorizations (str "/" stripe-api-namespace "/issuing/authorizations")
   :issuing-cardholders (str "/" stripe-api-namespace "/issuing/cardholders")
   :issuing-cards (str "/" stripe-api-namespace "/issuing/cards")
   :issuing-disputes (str "/" stripe-api-namespace "/issuing/disputes")
   :issuing-personalization-designs (str "/" stripe-api-namespace "/issuing/personalization_designs")
   :issuing-physical-bundles (str "/" stripe-api-namespace "/issuing/physical_bundles")
   :issuing-tokens (str "/" stripe-api-namespace "/issuing/tokens")
   :issuing-transactions (str "/" stripe-api-namespace "/issuing/transactions")
   :mandates (str "/" stripe-api-namespace "/mandates")
   :payment-intents (str "/" stripe-api-namespace "/payment_intents")
   :payment-links (str "/" stripe-api-namespace "/payment_links")
   :payment-method-configurations (str "/" stripe-api-namespace "/payment_method_configurations")
   :payment-method-domains (str "/" stripe-api-namespace "/payment_method_domains")
   :payment-methods (str "/" stripe-api-namespace "/payment_methods")
   :payouts (str "/" stripe-api-namespace "/payouts")
   :plans (str "/" stripe-api-namespace "/plans")
   :prices (str "/" stripe-api-namespace "/prices")
   :promotion-codes (str "/" stripe-api-namespace "/promotion_codes")
   :products (str "/" stripe-api-namespace "/products")
   :quotes (str "/" stripe-api-namespace "/quotes")
   :radar-early-fraud-warnings (str "/" stripe-api-namespace "/radar/early_fraud_warnings")
   :radar-value-list-items (str "/" stripe-api-namespace "/radar/value_list_items")
   :radar-value-lists (str "/" stripe-api-namespace "/radar/value_lists")
   :refunds (str "/" stripe-api-namespace "/refunds")
   :reporting-report-runs (str "/" stripe-api-namespace "/reporting/report_runs")
   :reporting-report-types (str "/" stripe-api-namespace "/reporting/report_types")
   :reviews (str "/" stripe-api-namespace "/reviews")
   :setup-attempts (str "/" stripe-api-namespace "/setup_attempts")
   :setup-intents (str "/" stripe-api-namespace "/setup_intents")
   :shipping-rates (str "/" stripe-api-namespace "/shipping_rates")
   :sigma-scheduled-query-runs (str "/" stripe-api-namespace "/sigma/scheduled_query_runs")
   :sources (str "/" stripe-api-namespace "/sources")
   :subscriptions (str "/" stripe-api-namespace "/subscriptions")
   :subscription-items (str "/" stripe-api-namespace "/subscription_items")
   :subscription-schedules (str "/" stripe-api-namespace "/subscription_schedules")
   :tax-calculations (str "/" stripe-api-namespace "/tax/calculations")
   :tax-registrations (str "/" stripe-api-namespace "/tax/registrations")
   :tax-settings (str "/" stripe-api-namespace "/tax/settings")
   :tax-transactions (str "/" stripe-api-namespace "/tax/transactions")
   :tax-codes (str "/" stripe-api-namespace "/tax_codes")
   :tax-ids (str "/" stripe-api-namespace "/tax_ids")
   :tax-rates (str "/" stripe-api-namespace "/tax_rates")
   :terminal-configurations (str "/" stripe-api-namespace "/terminal/configurations")
   :terminal-connection-tokens (str "/" stripe-api-namespace "/terminal/connection_tokens")
   :terminal-locations (str "/" stripe-api-namespace "/terminal/locations")
   :terminal-readers (str "/" stripe-api-namespace "/terminal/readers")
   :test-helpers-customers (str "/" stripe-api-namespace "/test_helpers/customers")
   :test-helpers-confirmation-tokens (str "/" stripe-api-namespace "/test_helpers/confirmation_tokens")
   :treasury-credit-reversals (str "/" stripe-api-namespace "/treasury/credit_reversals")
   :treasury-debit-reversals (str "/" stripe-api-namespace "/treasury/debit_reversals")
   :treasury-financial-accounts (str "/" stripe-api-namespace "/treasury/financial_accounts")
   :treasury-inbound-transfers (str "/" stripe-api-namespace "/treasury/inbound_transfers")
   :treasury-outbound-payments (str "/" stripe-api-namespace "/treasury/outbound_payments")
   :treasury-outbound-transfers (str "/" stripe-api-namespace "/treasury/outbound_transfers")
   :treasury-received-credits (str "/" stripe-api-namespace "/treasury/received_credits")
   :treasury-received-debits (str "/" stripe-api-namespace "/treasury/received_debits")
   :treasury-transaction-entries (str "/" stripe-api-namespace "/treasury/transaction_entries")
   :treasury-transactions (str "/" stripe-api-namespace "/treasury/transactions")
   :tokens (str "/" stripe-api-namespace "/tokens")
   :topups (str "/" stripe-api-namespace "/topups")
   :transfers (str "/" stripe-api-namespace "/transfers")
   :test-clocks (str "/" stripe-api-namespace "/test_helpers/test_clocks")
   :webhook-endpoints (str "/" stripe-api-namespace "/webhook_endpoints")})

(defn mask-api-key
  "Masks the middle portion of an API key, showing only the first 7 and last 4 characters.
   Example: 'sk_test_asdasdasadsad9Rglkf' -> 'sk_test...glkf'"
  [api-key]
  (when api-key
    (let [prefix (subs api-key 0 7)
          suffix (subs api-key (- (count api-key) 4))]
      (str prefix "..." suffix))))

(def base-api-version "2025-01-27.acacia")

(def default-client-config
  {:protocol "https"
   :host "api.stripe.com"
   :port 443
   :api-version base-api-version
   :max-network-retries 1
   :timeout 80000
   :full-response? false
   :kebabify-keys? false})

;; Connection pool options
(def default-connection-pool-options
  {:timeout 5
   :threads 4
   :default-per-route 2
   :insecure? false})

;; Stripe rate limits
;; https://docs.stripe.com/rate-limits
(def default-rate-limit-config
  {:live {:default {:read 100 :write 100}
          :files {:read 20 :write 20}     
          :search {:read 20 :write 0}     
          :meter {:read 1000 :write 1000}}
   :test {:default {:read 125 :write 125}   
          :files {:read 20 :write 20}     
          :search {:read 20 :write 0}
          :meter {:read 1000 :write 1000}}})

(def mock-mode {:protocol "http" :host "localhost" :port 12111 :api-key (:test api-keys)})
