# stripe-clojure

[![Clojars Project](https://img.shields.io/clojars/v/io.github.yonureker/stripe-clojure.svg)](https://clojars.org/io.github.yonureker/stripe-clojure)
[![GitHub Release](https://img.shields.io/github/v/release/yonureker/stripe-clojure)](https://github.com/yonureker/stripe-clojure/releases)
[![License](https://img.shields.io/github/license/yonureker/stripe-clojure)](LICENSE)

Clojure SDK for the [Stripe API](https://stripe.com/docs/api). Listed by Stripe as a [community-supported SDK](https://docs.stripe.com/sdks/community).

**Stripe API Version: `2026-10-28.endive`**

## Features

- Complete Stripe v1 and v2 API coverage
- V2 API support with automatic version detection
- Auto-pagination with lazy sequences (cursor-based for v1, URL-based for v2)
- HTTP/2 with automatic connection management
- Proxy support
- File upload support
- Rate limiting support
- Automatic retries with exponential backoff and Retry-After header support
- Webhook signature verification
- Request/response event hooks

## Installation

Add to your `deps.edn`:
```clojure
{:deps {io.github.yonureker/stripe-clojure {:mvn/version "3.0.0"}}}
```

Or in `project.clj`:
```clojure
[io.github.yonureker/stripe-clojure "3.0.0"]
```

> **Note:** Versions 2.0 and later require Java 11+. For Java 8 support, use version 1.1.0.

## Basic Usage

```clojure
(require '[stripe-clojure.core :as stripe]
         '[stripe-clojure.customers :as customers]
         '[stripe-clojure.payment-intents :as payment-intents])

;; Initialize client (reusable)
(def stripe-client (stripe/init-stripe {:api-key "sk_test_..."}))

;; Create a customer
(customers/create-customer stripe-client
  {:name "Jane Doe"
   :email "jane@example.com"})

;; Create a payment intent
(payment-intents/create-payment-intent stripe-client
  {:amount 2000
   :currency "usd"
   :customer "cus_123"})
```

### V2 API

V2 endpoints are automatically detected by their `/v2/` path prefix. The library handles all version differences transparently (JSON encoding, URL-based pagination, `include` parameter).

```clojure
(require '[stripe-clojure.v2.core.accounts :as v2-accounts]
         '[stripe-clojure.v2.core.events :as v2-events]
         '[stripe-clojure.v2.core.event-destinations :as v2-event-destinations])

;; Create a V2 account
(v2-accounts/create-account stripe-client
  {:legal_entity {:name "Acme Inc."}})

;; List V2 events with field inclusion
(v2-events/list-events stripe-client {} {:include ["data.payload"]})

;; Create a V2 event destination
(v2-event-destinations/create-event-destination stripe-client
  {:type "webhook_endpoint"
   :name "Production Events"
   :enabled_events ["*"]
   :webhook_endpoint {:url "https://example.com/stripe/events"}})
```

Available V2 resources:
- **Core:** `v2.core.accounts`, `v2.core.account-links`, `v2.core.account-tokens`, `v2.core.events`, `v2.core.event-destinations`, `v2.core.persons`, `v2.core.person-tokens`
- **Core vault:** `v2.core.vault.gb-bank-accounts`, `v2.core.vault.us-bank-accounts`
- **Billing:** `v2.billing.meter-events`, `v2.billing.meter-event-session`, `v2.billing.meter-event-stream`, `v2.billing.meter-event-adjustments`
- **Money management:** `v2.money-management.adjustments`, `v2.money-management.financial-accounts`, `v2.money-management.financial-addresses`, `v2.money-management.inbound-transfers`, `v2.money-management.outbound-payments`, `v2.money-management.outbound-setup-intents`, `v2.money-management.payout-intents`, `v2.money-management.payout-methods`, `v2.money-management.payout-methods-bank-account-spec`, `v2.money-management.received-credits`, `v2.money-management.recipient-verifications`, `v2.money-management.transaction-entries`, `v2.money-management.transactions`
- **Commerce:** `v2.commerce.product-catalog.imports`
- **Test helpers:** `v2.test-helpers.money-management.financial-addresses`, `v2.test-helpers.money-management.recipient-verifications`

## Client Configuration

Create isolated client instances with custom configurations:

```clojure
(def production-client
  (stripe/init-stripe
    {:api-key "sk_live_..."
     :api-version "2026-10-28.endive"
     :max-network-retries 3
     :timeout 30000
     :rate-limits {:live {:default {:read 50 :write 25}}}}))

(def development-client
  (stripe/init-stripe
    {:api-key "sk_test_..."
     :full-response? true  ; Include headers and metadata
     :kebabify-keys? true  ; Convert snake_case to kebab-case
     :max-network-retries 1}))

;; Client behind a corporate proxy
(def proxied-client
  (stripe/init-stripe
    {:api-key "sk_live_..."
     :proxy {:host "proxy.corp.com" :port 8080}}))
```

Create each client once and reuse it. Every client owns its own HTTP connections, so creating one per request wastes them.

### Configuration Options

| Parameter | Default | Description |
|-----------|---------|-------------|
| `:api-key` | **Required** | Your Stripe secret API key |
| `:api-version` | `"2026-10-28.endive"` | Stripe API version to use |
| `:stripe-account` | `nil` | Connect account ID for platform requests |
| `:max-network-retries` | `1` | Number of automatic retries for failed requests |
| `:timeout` | `80000` | Request timeout in milliseconds |
| `:connect-timeout` | `30000` | Time allowed for opening a connection, in milliseconds |
| `:host` | `"api.stripe.com"` | Stripe API hostname |
| `:port` | `443` | API port number |
| `:protocol` | `"https"` | Protocol to use (always use HTTPS in production) |
| `:proxy` | `nil` | Proxy configuration `{:host :port :user :password}` |
| `:rate-limits` | [See config](src/stripe_clojure/config.clj) | Custom rate limiting configuration |
| `:full-response?` | `false` | Return complete HTTP response or just body |
| `:kebabify-keys?` | `false` | Convert response keys to kebab-case |
| `:mock` | `false` | Use the local stripe-mock defaults (see [Testing](#testing)); other options still apply |

### Proxy

All requests go through the proxy when `:proxy` is set. For a proxy that asks for credentials, add `:user` and `:password`:

```clojure
(stripe/init-stripe
  {:api-key "sk_live_..."
   :proxy {:host "proxy.corp.com" :port 8080 :user "jane" :password "secret"}})
```

The credentials are only ever sent to the proxy. The JDK refuses Basic authentication on HTTPS tunnels by default, so a proxy that uses it also needs the JVM option `-Djdk.http.auth.tunneling.disabledSchemes=""`.

## Per-Request Options

Many API methods allow you to override options on a per-request basis via the `opts` map:

- `:api-version`, `:stripe-account`: to dynamically switch contexts
- `:max-network-retries`, `:timeout`: for alternative networking parameters
- `:full-response?`: for detailed request/response info
- `:kebabify-keys?`: to convert the keys of one response to kebab-case, or to turn that off
- `:idempotency-key`: for safely retryable POST requests
- `:expand`: to expand linked objects in a single request (v1)
- `:include`: to include fields in the response (v2)
- `:auto-paginate?`: to return complete dataset as a lazy sequence
- `:on-pagination-error`: `:truncate` (default) or `:throw`, see [Auto-Pagination](#auto-pagination)
- `:as`: `:byte-array` to receive the raw response body instead of parsed JSON
- `:base-url`: to override the API base URL for a single request
- `:custom-headers`: to add custom HTTP headers
- `:test-clock`: to use a Stripe test clock
- `:stripe-beta`: to access beta API features
- `:multipart`: for file upload requests

```clojure
;; passing params and opts
(customers/create-customer stripe-client {:name "John Doe" :email "john@example.com"}
                                         {:max-network-retries 5 :idempotency-key "key_1234"})

;; passing id and opts
(payment-intents/retrieve-payment-intent stripe-client "pi_1234" {:max-network-retries 5})

;; passing id only
(customers/retrieve-customer stripe-client "pi_1234")

;; passing opts only
(customers/list-customers stripe-client {} {:auto-paginate? true})
```

## Raw Requests

Use `raw-request` to call any Stripe endpoint directly -- useful for beta features, undocumented endpoints, or when you need to bypass the resource-specific functions:

```clojure
;; GET request
(stripe/raw-request stripe-client :get "/v1/customers" {:limit 10})

;; POST request with beta header
(stripe/raw-request stripe-client :post "/v1/beta/feature"
  {:param "value"}
  {:stripe-beta "feature_v1=true"})

;; Works with V2 endpoints too
(stripe/raw-request stripe-client :get "/v2/core/events" {} {:include ["data.payload"]})
```

## Expanding Responses

Stripe's Expand feature retrieves linked objects in a single call, replacing the object ID with all its properties.

```clojure
(def pi-retrieved (payment-intents/retrieve-payment-intent stripe-client "pi_1234"))

;; payment intent response
{:description "payment for order"
 :id "pi_1234"
 ...
 :customer "cus_1234" ;; only customer id returned
 :payment_method "pm_1234"}
```

Using expand, retrieve the whole customer object without an additional request:

```clojure
(def pi-with-customer-info (payment-intents/retrieve-payment-intent
                                stripe-client
                                "pi_1234"
                                {:expand ["customer"]}))

;; response now has the customer object
{:description "payment for order"
 :id "pi_1234"
 ...
 :customer {:name "Jane Doe" :email "jane@example.com" ...}
 :payment_method "pm_1234"}
```

Multiple fields can be expanded:

```clojure
(def pi-with-customer-info (payment-intents/retrieve-payment-intent
                                stripe-client
                                "pi_1234"
                                {:expand ["customer" "payment_method"]}))

;; both customer and payment_method are expanded
{:description "payment for order"
 :id "pi_1234"
 ...
 :customer {:name "Jane Doe" :email "jane@example.com" ...}
 :payment_method {:type "card" :last4 "4242" ...}}
```

For V2 endpoints, use `:include` instead of `:expand`:

```clojure
(require '[stripe-clojure.v2.core.events :as v2-events])

(v2-events/list-events stripe-client {} {:include ["data.payload"]})
```

For convenience, you can use either `:expand` or `:include` with any API version -- the library automatically maps to the correct parameter.

## Auto-Pagination

Pass `:auto-paginate? true` on any GET request to receive all pages as a lazy sequence. The library automatically detects the API version and uses the appropriate pagination strategy:

- **V1:** Cursor-based pagination via `has_more` and `starting_after`
- **V2:** URL-based pagination via `next_page_url`

```clojure
(ns my.project
  (:require [stripe-clojure.core :as stripe]
            [stripe-clojure.customers :as customers]
            [stripe-clojure.v2.core.events :as v2-events]))

(def stripe-client (stripe/init-stripe {:api-key "your_stripe_api_key"}))

;; V1: Auto-paginate through a list endpoint
(def all-customers
  (customers/list-customers stripe-client {:limit 50} {:auto-paginate? true}))

;; V1: Auto-paginate through a search endpoint
;; https://docs.stripe.com/search#search-query-language
(def matching-customers
  (customers/search-customers stripe-client
    {:query "email:'sally@rocketrides.io'" :limit 50}
    {:auto-paginate? true}))

;; V2: Auto-paginate through V2 events (URL-based pagination)
(def all-events
  (v2-events/list-events stripe-client {} {:auto-paginate? true}))

;; Process results lazily
(doseq [customer all-customers]
  (println (:id customer)))
```

Pages are fetched as the sequence is consumed. Each page is a request of its own: it is retried, counts against your rate limits and emits request and response events. The sequence contains the items themselves, also when `:full-response?` is set.

If a page fails, the sequence ends there by default. Pass `:on-pagination-error :throw` to get an exception instead when the failing page is reached. Its `ex-data` is the error map:

```clojure
(try
  (doseq [customer (customers/list-customers stripe-client {:limit 100}
                                             {:auto-paginate? true
                                              :on-pagination-error :throw})]
    (println (:id customer)))
  (catch clojure.lang.ExceptionInfo e
    (println "Stopped at a failed page:" (:status (ex-data e)) (:message (ex-data e)))))
```

## Request and Response Events

The client emits `request` and `response` events for logging, debugging, or modifying requests and responses.

```clojure
(ns your-namespace
  (:require [stripe-clojure.core :as stripe]))

(def stripe-client (stripe/init-stripe {:api-key "sk_test_..."}))

;; Define an event handler function for requests
(defn on-request [request]
  (println "Request event:" request))

;; Add the event handler function
(stripe/on stripe-client :request on-request)

;; Remove the event handler function
(stripe/off stripe-client :request on-request)
```

`request` object:
```clojure
{:api_version "2026-10-28.endive"
 :account "acct_TEST"              ;; Only present if a Stripe account is set
 :idempotency_key "abc123"         ;; Present on POST requests, and when provided
 :method "POST"
 :path "/v1/customers"
 :request_start_time 1565125303932 ;; Unix timestamp in milliseconds
}
```

`response` object:
```clojure
{:api_version "2026-10-28.endive"
 :account "acct_TEST"              ;; Only present if a Stripe account is set
 :idempotency_key "abc123"         ;; Present on POST requests, and when provided
 :method "POST"
 :path "/v1/customers"
 :status 402
 :request_id "req_Ghc9r26ts73DRf"
 :elapsed 445                      ;; Elapsed time in milliseconds
 :request_start_time 1565125303932 ;; Unix timestamp in milliseconds
 :request_end_time 1565125304377   ;; Unix timestamp in milliseconds
}
```

With auto-pagination, every page emits its own pair of events.

## File Uploads

Upload files to Stripe using the Files API:

```clojure
(require '[stripe-clojure.files :as files])

;; Upload a file
(files/create-file stripe-client "/path/to/document.pdf" "dispute_evidence")

;; Retrieve file details
(files/retrieve-file stripe-client "file_123")

;; List all files
(files/list-files stripe-client {:purpose "dispute_evidence"})
```

Supported file types: PDF, JPEG, PNG, CSV. The library automatically detects MIME types and routes uploads to `files.stripe.com`.

If you build a `:multipart` request yourself, pass files as a `File` or a byte array. An `InputStream` can only be read once, so a retried request would upload an empty file; set `:max-network-retries 0` if you have to use one.

## Quote PDFs

`quotes/get-pdf` downloads the PDF of a finalized quote from `files.stripe.com` and returns it as a byte array:

```clojure
(require '[stripe-clojure.quotes :as quotes]
         '[clojure.java.io :as io])

(io/copy (quotes/get-pdf stripe-client "qt_123") (io/file "quote.pdf"))
```

## Network Retries

Unless `:max-network-retries` is set to zero, requests are automatically retried for:

- **409 (Conflict):** Rare race condition
- **429 (Too Many Requests):** Rate limit exceeded
- **5xx:** Server errors

Retry delay is determined by Stripe's `Retry-After` header when present, falling back to exponential backoff with jitter.

## Error Handling

API errors are returned as maps (not thrown as exceptions). An error map carries the HTTP status as an integer under `:status`:

```clojure
(let [result (customers/retrieve-customer stripe-client "invalid_id")]
  (if (integer? (:status result))  ;; only error maps have an integer :status
    (do
      (println "Error:" (:message result))   ;; "No such customer: invalid_id"
      (println "Type:" (:type result))       ;; "invalid_request_error"
      (println "Code:" (:code result))       ;; "resource_missing"
      (println "Status:" (:status result)))  ;; 404
    (process-customer result)))
```

Do not test for `:type` or a non-nil `:status`: many successful objects have a `type` (payment methods, events) or a string `status` (payment intents, subscriptions). With `:full-response?` the status is always present, so check `(>= (:status response) 400)` there.

Error response structure:
```clojure
{:type "invalid_request_error"
 :code "resource_missing"
 :message "No such customer: 'invalid_id'"
 :param "id"
 :status 404
 :request-id "req_..."}
```

## Full Response Mode

By default, the library returns just the response body. Enable `:full-response?` to get the complete HTTP response including headers, status, and body:

```clojure
;; At client level
(def client (stripe/init-stripe {:api-key "sk_test_..." :full-response? true}))

;; Or per-request
(customers/retrieve-customer stripe-client "cus_123" {:full-response? true})

;; Full response structure
{:status 200
 :headers {"request-id" "req_abc123" "stripe-version" "2026-10-28.endive" ...}
 :body {:id "cus_123" :name "Jane Doe" ...}}
```

The full response never contains the outgoing request, so your API key is not part of it.

## Multiple Environments

Manage different environments with separate clients:

```clojure
(def us-client (stripe/init-stripe {:api-key us-api-key}))
(def eu-client (stripe/init-stripe {:api-key eu-api-key}))
(def test-client (stripe/init-stripe {:api-key test-api-key}))

;; Use appropriate client for each region
(customers/create-customer us-client customer-data)
(customers/create-customer eu-client customer-data)

;; Optional, see below
(stripe/shutdown-stripe-client! us-client)
```

`shutdown-stripe-client!` is kept for compatibility and currently does nothing. The JVM releases a client's connections once the client is no longer referenced.

## Rate Limiting

Configure rate limiting aligned with Stripe's limits:

```clojure
(def rate-limited-client
  (stripe/init-stripe
    {:api-key "sk_live_..."
     :rate-limits {:live {:default {:read 100 :write 50}
                          :files   {:read 20  :write 20}
                          :search  {:read 20  :write 0}
                          :meter   {:read 100 :write 100}}
                   :test {:default {:read 25  :write 25}}}}))
```

Limits are requests per second. Nothing is throttled unless you configure it.

- **Mode:** `:test` applies to test keys (`sk_test_...`, `rk_test_...`), `:live` to all others.
- **Bucket:** `:files` covers the Files API, `:search` every search endpoint, `:meter` meter events and the meter event stream, and `:default` everything else. A bucket you leave out falls back to `:default`.
- **Values:** `0` switches a limit off, and 500 or more is treated as unlimited.
- **Pagination:** every auto-paginated page counts as a request.

## Test Helpers

Stripe's test helper endpoints live under `stripe-clojure.test-helpers` and only work with test keys:

```clojure
(require '[stripe-clojure.test-helpers.test-clocks :as test-clocks])

(def clock (test-clocks/create-test-clock stripe-client {:frozen_time 1767225600}))

;; Move the clock forward by a month
(test-clocks/advance-test-clock stripe-client (:id clock) {:frozen_time 1769904000})
```

Available namespaces:
- `test-helpers.test-clocks`, `test-helpers.customers`, `test-helpers.confirmation-tokens`, `test-helpers.refunds`
- `test-helpers.terminal.readers`
- `test-helpers.issuing.authorizations`, `test-helpers.issuing.cards`, `test-helpers.issuing.personalization-designs`, `test-helpers.issuing.transactions`
- `test-helpers.treasury.inbound-transfers`, `test-helpers.treasury.outbound-payments`, `test-helpers.treasury.outbound-transfers`, `test-helpers.treasury.received-credits`, `test-helpers.treasury.received-debits`

## Webhook Verification

Verify webhook signatures:

```clojure
(require '[stripe-clojure.webhooks :as webhooks])

(defn handle-webhook [request]
  (let [payload (slurp (:body request))
        signature (get-in request [:headers "stripe-signature"])
        endpoint-secret "whsec_..."]
    
    (try
      (let [event (webhooks/construct-event payload signature endpoint-secret)]
        (println "Verified event:" (:type event))
        {:status 200})
      (catch Exception e
        (println "Webhook verification failed:" (.getMessage e))
        {:status 400}))))
```

## Testing

1. **Install stripe-mock** (0.206.0 or later):
   ```bash
   brew install stripe/stripe-mock/stripe-mock
   ```

2. **Start it with the API spec this library targets:**
   ```bash
   scripts/stripe-mock.sh
   ```
   The script downloads the matching OpenAPI spec once and serves it on port 12111. The spec built into stripe-mock is older, so `brew services start stripe-mock` alone fails part of the suite. Set `STRIPE_MOCK_PORT` to use another port, for the script and for the tests.

3. **Set your test API key** (a few tests call Stripe's test mode):
   ```bash
   export STRIPE_TEST_API_KEY='sk_test_...'
   ```

4. **Run tests:**
   ```bash
   clj -M:test -e :skip-mock
   ```

To use stripe-mock in your own tests, create the client with `(stripe/init-stripe {:mock true})`. It points at `localhost:12111`, or at `STRIPE_MOCK_HOST` and `STRIPE_MOCK_PORT` when they are set.

## Contributing

Contributions are welcome. See [CONTRIBUTING.md](CONTRIBUTING.md) for details.

```bash
git clone https://github.com/yonureker/stripe-clojure.git
cd stripe-clojure
clj -P        # Download dependencies
clj -M:test   # Run tests
```

## License

MIT License. See [LICENSE](LICENSE).

## Links

- [Stripe API Docs](https://stripe.com/docs/api)
- [GitHub Issues](https://github.com/yonureker/stripe-clojure/issues)
