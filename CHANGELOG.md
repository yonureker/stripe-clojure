# Change Log

## [2.1.0] - 2025-12-18

**Stripe API Version: `2025-12-15.clover`**

### Removed (Deprecated)
- `link_account_sessions` - use `financial_connections/sessions` instead
- `linked_accounts` - use `financial_connections/accounts` instead
- `issuing/settlements` - removed from Stripe API
- `external_accounts` - use methods in `accounts` namespace instead

## [2.0.0] - 2025-12-18

**Stripe API Version: `2025-11-17.clover`**

### Breaking Changes
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
