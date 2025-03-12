# Change Log

## [0.2.0] - 2025-03-12

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