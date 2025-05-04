# Change Log

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