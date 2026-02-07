# Contributing to stripe-clojure

Contributions are welcome! This document covers the process for contributing to this project.

## Getting Started

1. Fork and clone the repository:
   ```bash
   git clone https://github.com/yonureker/stripe-clojure.git
   cd stripe-clojure
   ```

2. Install dependencies:
   ```bash
   clj -P
   ```

3. Install [stripe-mock](https://github.com/stripe/stripe-mock) for running integration tests:
   ```bash
   brew install stripe/stripe-mock/stripe-mock
   brew services start stripe-mock
   ```

## Running Tests

Run the full test suite (requires stripe-mock and a test API key):

```bash
export STRIPE_TEST_API_KEY='sk_test_...'
clj -M:test -e :skip-mock
```

Run only unit tests (no external dependencies needed):

```bash
clj -M:test --namespace-regex "stripe-clojure\.unit\..*"
```

## Project Structure

- `src/stripe_clojure/` - Source code
  - `config.clj` - API endpoints and default configuration
  - `core.clj` - Client initialization and event management
  - `http/` - HTTP layer (request, response, pagination, encoding, retry, throttle)
  - `v2/` - V2 API resource wrappers
  - `schemas/` - Malli schemas for validation
  - `webhooks.clj` - Webhook signature verification
- `test/stripe_clojure/` - Tests
  - `unit/` - Unit tests (no network required)
  - `mock/` - Integration tests against stripe-mock

## Adding a New Stripe Resource

1. Add the endpoint to `config.clj` in `stripe-endpoints` (v1) or `stripe-v2-endpoints` (v2)
2. Create a resource file following the thin-wrapper pattern (see existing files for examples)
3. Add unit tests covering function existence, arities, docstrings, and request call behavior
4. Add mock tests for integration verification

## Code Style

- Follow existing conventions in the codebase
- All API resource functions use multi-arity with an optional `opts` map as the last parameter
- Delete operations pass `{}` (not `nil`) as the params argument
- Use `kebab-case` for Clojure identifiers, `snake_case` for Stripe API field names

## Submitting Changes

1. Create a feature branch from `main`
2. Make your changes with clear, focused commits
3. Ensure all tests pass
4. Open a pull request with a description of the changes

## Reporting Issues

Open an issue on [GitHub Issues](https://github.com/yonureker/stripe-clojure/issues) with:
- A clear description of the problem
- Steps to reproduce (if applicable)
- Expected vs actual behavior
- Library and Clojure version information
