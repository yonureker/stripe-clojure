# Change Log

## [3.0.0] - 2026-11-18

**Stripe API Version: `2026-10-28.endive`**

This release moves to Stripe's `2026-10-28.endive` API, adds every endpoint that was still missing, and fixes bugs in pagination, proxies, rate limiting and events. No function was removed or renamed.

### Breaking Changes
Nothing was removed, but a few things behave differently. Check these before you upgrade.

- **New default API version.** Requests now use `2026-10-28.endive`, and Stripe changed some parameters in it. For example, `payment_method_types` is gone from Checkout Sessions, PaymentIntents and SetupIntents, and `billing_cycle_anchor` is now a map like `{:type "now"}`. [Stripe's changelog](https://docs.stripe.com/changelog/endive) has the full list. If you need more time, set `:api-version "2026-04-22.dahlia"` on your client.
- **`:full-response?` no longer returns `:request` and `:http-client`.** The request included your API key, so it should never have been there.
- **Keyword values lose their colon.** `{:currency :usd}` now sends `usd`. Before, it sent `:usd`, which Stripe rejected for fields like currency and saved as it was in free-text fields like metadata.
- **`:proxy` is now really used.** Earlier versions accepted the option but still connected directly. If you have it set, make sure the proxy lets requests through to Stripe.
- **`{:mock true}` respects your other options.** They used to be ignored. An invalid option next to `:mock` now throws, and `{:mock false}` gives you a normal client.

### New Endpoints
The library now covers everything in Stripe's current API. That is 134 new functions.
- v1: `apps/installs`, `billing/feedback_options`, `product_catalog/trial_offers`, `tax/locations`, `three_d_secure/authentications`
- v1, on existing resources: unreject an account, pause a subscription, list payment records, close a treasury financial account, retrieve and list the refunds of a charge, retrieve an entitlements feature, and three terminal reader actions (collect inputs, collect payment method, confirm payment intent)
- v1 test helpers: test clocks, refunds, terminal readers, issuing and treasury
- v2: `core/vault` bank accounts (US and GB), `money_management`, `commerce/product_catalog/imports`

### New Features
- `:on-pagination-error :throw` makes auto-pagination raise an error when a page fails. By default it still stops quietly at that page.
- `:connect-timeout` sets how long to wait for a connection. The default is 30 seconds.
- `:kebabify-keys?` now works per request, not only on the client.
- `:as :byte-array` returns the raw response body.
- `scripts/stripe-mock.sh` starts stripe-mock with the right API spec for the tests. Set `STRIPE_MOCK_PORT` to run it on another port.

### Bug Fixes
- Auto-pagination stopped after the first page on v2 endpoints and on search endpoints.
- Auto-pagination returned nothing when `:full-response?` was on.
- Auto-pagination could repeat the same page when `starting_after` was passed in a sorted map or a large params map.
- Pages after the first skipped rate limits and event listeners. Every page is now throttled and reported like any other request.
- `quotes/get-pdf` never worked. It now returns the PDF as a byte array.
- `send-meter-events` called the wrong host.
- The `accounts/*-people` functions always got a 404 from Stripe.
- `:search` and `:meter` rate limits were never applied, and restricted test keys (`rk_test_...`) were treated as live keys.
- Request events were missing the idempotency key, and carried `:account nil` when no account was set.
- Opening a connection could hang for the whole request timeout (80 seconds). It now gives up after 30.

### Deprecated
- `accounts/verify-bank-account`. Stripe has no such endpoint.
- `accounts/create-people`, `retrieve-people`, `update-people`, `delete-people` and `list-people`. Use the `person` versions instead (`create-person`, `list-persons` and so on). The old names still work.

### Performance
- About 30% less overhead per request inside the library.

### Changes
- Updated cheshire to 6.2.1 and malli to 0.20.3. The library still passes its tests with the versions 2.3.0 used, in case you pin them.
- Shorter v2 docstrings, with corrected docs links.

## [2.3.0] - 2026-04-23

**Stripe API Version: `2026-04-22.dahlia`**

### New Endpoints
- v1: `issuing/settlements` (retrieve, update)
- v1: `radar/payment_evaluations` (create)
- v1: `sigma/saved_queries` (update)
- v1: `terminal/refunds` (create)
- v2: `core/account_links` (create)
- v2: `core/account_tokens` (create, retrieve)

### Bug Fixes
- Fixed nil opts passed to `make-request` causing schema validation error
- Fixed `me/humanize` called twice on validation failure path
- Fixed inconsistent `System/currentTimeMillis` reads in response event timing

### Performance
- Compiled Malli validator at load time for per-request schema validation

### Changes
- Bumped Stripe API version from `2026-01-28.clover` to `2026-04-22.dahlia`

## [2.2.0] - 2026-02-06

**Stripe API Version: `2026-01-28.clover`**

### New Features
- V2 API support with automatic version detection (JSON encoding, URL-based pagination, `include` parameter)
- Version-aware auto-pagination: cursor-based for v1, URL-based for v2
- Multimethod encoding dispatch by API version (content-type, param encoding, field expansion)
- Protocol-based pagination with v1 and v2 implementations
- `raw-request` function for calling arbitrary Stripe endpoints (beta features, undocumented APIs)
- HTTP proxy support via `:proxy` client configuration option

### Bug Fixes
- Fixed V2 auto-pagination using `next_page_url` as the actual request URL instead of passing it as a query parameter
- Fixed auto-pagination fetching the first page twice (now passes pre-fetched result)
- Fixed idempotency keys being generated for GET/DELETE instead of POST requests
- Fixed `list-invoice-payments` missing params arity for filtering
- Fixed webhook `parse-signature` dropping duplicate `v1` signatures during secret rotation
- Fixed `verify-signature` to check all `v1` signatures (supports secret rotation)
- Fixed double/triple key transformation on error responses when `kebabify-keys?` is true
- Fixed potential NPE in `send-stripe-api-request` when exception `ex-data` lacks `:status`
- Fixed `nil` passed as params in delete request calls across 13 resource files
- Fixed event handler key transformation using `(comp keyword underscore-to-kebab name)` for proper kebab-case conversion
- Fixed `mask-api-key` bounds check for short API keys
- Fixed `format-indexed-params` threading `assoc!` return value through loop binding
- Fixed `identical?` to `=` for keyword comparison in request processing
- Fixed network error responses losing diagnostic info (message, type) when flowing through `process-response`
- Fixed V2 pagination potentially duplicating expand/include params on subsequent pages when `next_page_url` already contains them

### Changes
- Added `:kebabify-keys?` to `merge-client-config` for per-request override consistency
- Removed orphaned `:test-clocks` config endpoint (no corresponding resource)
- Removed unused `[clojure.core :as str]` require from config namespace
- Removed `attach-response-metadata` (leaky abstraction)
- Removed `is-paginated-endpoint?` regex
- DRY refactoring with shared test helpers (`check-endpoint`, `check-functions-exist`, `check-function-arities`, `check-docstrings`, `check-request-calls`)
- DRY refactoring with shared `format-indexed-params` utility for expand/include formatting
- Pagination implementation refactored from `defrecord` to `reify`

## [2.1.0] - 2025-12-18

**Stripe API Version: `2025-12-15.clover`**

### Removed (Deprecated)
- `link_account_sessions` - use `financial_connections/sessions` instead
- `linked_accounts` - use `financial_connections/accounts` instead
- `issuing/settlements` - removed from Stripe API
- `external_accounts` - use methods in `accounts` namespace instead

## [2.0.0] - 2025-12-18

**Stripe API Version: `2025-11-17.clover`**

### Breaking Changes ⚠️ ⚠️ ⚠️
- Requires Java 11+ (previously Java 8+)
- Removed `:use-connection-pool?` and `:pool-options` configuration options (deprecated with warning)

### Changes
- Replaced `clj-http` with `hato` for HTTP/2 support
- Connection pooling is now automatic via HTTP/2 multiplexing
- Removed `commons-codec` dependency (uses pure Java for hex encoding in webhooks)

## [1.1.0] - 2025-12-12

### API Update

Updated to Stripe API version `2025-11-17.clover`.

#### New Endpoints
- `balance-settings` - Retrieve and update balance settings
- `payment-attempt-records` - List and retrieve payment attempt records
- `payment-records` - Report payments, payment attempts, and refunds
- `tax/associations` - Find tax associations
- `terminal/onboarding-links` - Create terminal onboarding links
- `payment-intents/list-amount-details-line-items` - List line items for payment intents

#### Bug Fixes
- Fixed auto-pagination returning duplicate items when `starting_after` parameter wasn't being passed correctly
- Fixed pagination not working with `:kebabify-keys? true` (now checks both `:has_more` and `:has-more`)
- Fixed reflection warning in retry logic (`Thread/sleep` type hint)

## [1.0.0] - 2025-01-16

### 🚀 Major Release - Production Ready

This is the first major release of the Stripe Clojure SDK, marking it as production-ready with significant performance improvements, enhanced reliability, and comprehensive feature set.

#### ✨ New Features
- **Zero-overhead Optional Throttling**: Smart rate limiting that only activates when needed, with true zero-overhead fast path
- **Enhanced Webhook Security**: Improved webhook verification with better error handling and timing attack protection
- **Advanced Client Configuration**: Flexible client instances with isolated configurations for multi-tenancy

#### 🎯 Performance Improvements
- **Optimized Util Functions**: 15-30% performance improvements with efficient string operations and type hints
- **Memory Efficient**: Minimal allocations with excellent garbage collection behavior
- **StringBuilder Optimization**: Faster key building for parameter flattening
- **Reflection Elimination**: Comprehensive type hints to remove reflection overhead

#### 🔧 Critical Bug Fixes
- **Fixed `format-expand` Function**: Resolved critical bug where loop didn't return values, breaking expand parameters
- **Vector Payload Support**: Verified and enhanced support for array/vector parameter flattening
- **Parameter Processing**: Improved nested parameter handling for complex API requests

#### 📊 Developer Experience
- **Comprehensive Testing**: 551+ tests with 100% coverage using stripe-mock
- **Better Error Messages**: Enhanced error context and validation
- **Documentation**: Extensive documentation with real-world examples
- **Production Benchmarks**: Detailed performance analysis and optimization guides

#### 🏗️ Infrastructure
- **Intelligent Rate Limiting**: Respects Stripe's actual server-side limits (25-100 req/s)
- **Connection Pooling**: Optional HTTP connection pooling for high-throughput scenarios
- **Request Lifecycle**: Event system for monitoring and debugging
- **Schema Validation**: Malli-based validation for client and request parameters

#### 🔒 Security Enhancements
- **Environment Variable Security**: Secure API key management with environment variables
- **Constant-time Comparisons**: Webhook signature verification resistant to timing attacks
- **Input Validation**: Enhanced validation for webhook payloads and signatures

#### 📈 Scalability
- **Multi-client Support**: Isolated client instances for microservices and multi-tenant applications
- **Auto-pagination**: Lazy sequences for efficient handling of large datasets
- **Thread Safety**: Full thread-safe design for concurrent applications
- **Resource Management**: Proper cleanup and resource management for long-running applications

This release establishes the Stripe Clojure SDK as a mature, production-ready library suitable for high-scale financial applications.

## [0.3.0] - 2025-05-04

- [#17](https://github.com/yonureker/stripe-clojure/issues/17): Fixed flattening params issue with vector payload 
- [#18](https://github.com/yonureker/stripe-clojure/pull/18): Bumped stripe version to `2025-04-30.basil`.
- [#19](https://github.com/yonureker/stripe-clojure/pull/19): Adds support for invoice payment resources. 
- [#20](https://github.com/yonureker/stripe-clojure/pull/20): Remove support for `list-upcoming-line-items` and `retrieve-upcoming` methods on Invoice resource. 
- [#21](https://github.com/yonureker/stripe-clojure/pull/21): Remove support for `create-usage-record` and `list-usage-record-summaries` methods on Subscription items. 
- [#23](https://github.com/yonureker/stripe-clojure/pull/23): Fixes a mock test with invoices. 
- [#16](https://github.com/yonureker/stripe-clojure/pull/16): Stripe docs API url fix in `create-session` docstring. 

## [0.2.2] - 2025-04-15

- Fixes a version issue
- Improved util functions with Java
- Used clj-http features instead of manual formatting of request headers

## [0.2.1] - 2025-03-12

- Replaced project.clj with deps.edn
- Implemented automated testing for pull requests using GitHub Actions.
- Added malli schemas
- Params for Stripe client initialization and request opts are now validated
- Idempotency keys are auto generated if missing
- Cleaned up logic for max network retries (now either from client config or opts)

## [0.1.4] - 2025-02-23

### Added

- Added `kebabify-keys?` to client initialization options.
- Added `on` and `off` event listeners to the `stripe-client`.

### Changed

- Updated the Stripe API version to `2025-01-27.acacia`.
- Optimized the performance of functions in `util` namespace.


## [0.0.1] - 2025-02-16

### Added

- Initial release of the Stripe Clojure library.
