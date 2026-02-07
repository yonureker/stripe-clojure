# stripe-clojure

[![Clojars Project](https://img.shields.io/clojars/v/io.github.yonureker/stripe-clojure.svg)](https://clojars.org/io.github.yonureker/stripe-clojure)
[![GitHub Release](https://img.shields.io/github/v/release/yonureker/stripe-clojure)](https://github.com/yonureker/stripe-clojure/releases)
[![License](https://img.shields.io/github/license/yonureker/stripe-clojure)](LICENSE)

Clojure SDK for the [Stripe API](https://stripe.com/docs/api). Listed by Stripe as a [community-supported SDK](https://docs.stripe.com/sdks/community).

**Stripe API Version: `2026-01-28.clover`**

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
{:deps {io.github.yonureker/stripe-clojure {:mvn/version "2.2.0"}}}
```

Or in `project.clj`:
```clojure
[io.github.yonureker/stripe-clojure "2.2.0"]
```

> **Note:** Version 2.x requires Java 11+. For Java 8 support, use version 1.1.0.

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
         '[stripe-clojure.v2.billing.meter-events :as v2-meter-events])

;; Create a V2 account
(v2-accounts/create-account stripe-client
  {:legal-entity {:name "Acme Inc."}})

;; List V2 events with field inclusion
(v2-events/list-events stripe-client {} {:include ["data.payload"]})

;; Send a meter event (high-throughput, up to 1000/sec)
(v2-meter-events/create-meter-event stripe-client
  {:event-name "api_requests"
   :payload {:stripe-customer-id "cus_xxx"
             :value 1}})
```

Available V2 resources:
- **Core:** `v2.core.accounts`, `v2.core.events`, `v2.core.event-destinations`, `v2.core.persons`, `v2.core.person-tokens`
- **Billing:** `v2.billing.meter-events`, `v2.billing.meter-event-session`, `v2.billing.meter-event-stream`, `v2.billing.meter-event-adjustments`

## Client Configuration

Create isolated client instances with custom configurations:

```clojure
(def production-client
  (stripe/init-stripe
    {:api-key "sk_live_..."
     :api-version "2026-01-28.clover"
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

### Configuration Options

| Parameter | Default | Description |
|-----------|---------|-------------|
| `:api-key` | **Required** | Your Stripe secret API key |
| `:api-version` | `"2026-01-28.clover"` | Stripe API version to use |
| `:stripe-account` | `nil` | Connect account ID for platform requests |
| `:max-network-retries` | `1` | Number of automatic retries for failed requests |
| `:timeout` | `80000` | Request timeout in milliseconds |
| `:host` | `"api.stripe.com"` | Stripe API hostname |
| `:port` | `443` | API port number |
| `:protocol` | `"https"` | Protocol to use (always use HTTPS in production) |
| `:proxy` | `nil` | Proxy configuration `{:host :port :user :password}` |
| `:rate-limits` | [See config](src/stripe_clojure/config.clj) | Custom rate limiting configuration |
| `:full-response?` | `false` | Return complete HTTP response or just body |
| `:kebabify-keys?` | `false` | Convert response keys to kebab-case |

## Per-Request Options

Many API methods allow you to override options on a per-request basis via the `opts` map:

- `:api-version`, `:stripe-account` – to dynamically switch contexts
- `:max-network-retries`, `:timeout` – for alternative networking parameters
- `:full-response?` – for detailed request/response info
- `:idempotency-key` – for safely retryable POST requests
- `:expand` – to expand linked objects in a single request (v1)
- `:include` – to include fields in the response (v2)
- `:auto-paginate?` – to return complete dataset as a lazy sequence
- `:base-url` – to override the API base URL for a single request
- `:custom-headers` – to add custom HTTP headers
- `:test-clock` – to use a Stripe test clock
- `:stripe-beta` – to access beta API features
- `:multipart` – for file upload requests

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

## Expanding Responses

Stripe's Expand feature retrieves linked objects in a single call, replacing the object ID with all its properties.

```clojure
(def pi-retrieved (payment-intents/retrieve-payment-intent stripe-client "pi_1234"))

;; payment intent response
{:description "payment for order"
 :id "pi_1234"
 ...
 :customer "cus_1234" ;; only customer id returned
 :payment-method "pm_1234"}
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
{:api_version "2026-01-28.clover"
 :account "acct_TEST"              ;; Only present if provided
 :idempotency_key "abc123"         ;; Only present if provided
 :method "POST"
 :path "/v1/customers"
 :request_start_time 1565125303932 ;; Unix timestamp in milliseconds
}
```

`response` object:
```clojure
{:api_version "2026-01-28.clover"
 :account "acct_TEST"              ;; Only present if provided
 :idempotency_key "abc123"         ;; Only present if provided
 :method "POST"
 :path "/v1/customers"
 :status 402
 :request_id "req_Ghc9r26ts73DRf"
 :elapsed 445                      ;; Elapsed time in milliseconds
 :request_start_time 1565125303932 ;; Unix timestamp in milliseconds
 :request_end_time 1565125304377   ;; Unix timestamp in milliseconds
}
```

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

## Network Retries

Unless `:max-network-retries` is set to zero, requests are automatically retried for:

- **409 (Conflict):** Rare race condition
- **429 (Too Many Requests):** Rate limit exceeded
- **5xx:** Server errors

Retry delay is determined by Stripe's `Retry-After` header when present, falling back to exponential backoff with jitter.

## Error Handling

API errors are returned as maps (not thrown as exceptions):

```clojure
(let [result (customers/retrieve-customer stripe-client "invalid_id")]
  (if (:type result)  ;; error responses have :type
    (do
      (println "Error:" (:message result))   ;; "No such customer: invalid_id"
      (println "Type:" (:type result))       ;; "invalid_request_error"
      (println "Code:" (:code result))       ;; "resource_missing"
      (println "Status:" (:status result)))  ;; 404
    (process-customer result)))
```

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
 :headers {"request-id" "req_abc123" "stripe-version" "2026-01-28.clover" ...}
 :body {:id "cus_123" :name "Jane Doe" ...}}
```

## Multiple Environments

Manage different environments with separate clients:

```clojure
(def us-client (stripe/init-stripe {:api-key us-api-key}))
(def eu-client (stripe/init-stripe {:api-key eu-api-key}))
(def test-client (stripe/init-stripe {:api-key test-api-key}))

;; Use appropriate client for each region
(customers/create-customer us-client customer-data)
(customers/create-customer eu-client customer-data)

;; Clean up when done
(stripe/shutdown-stripe-client! us-client)
```

## Rate Limiting

Configure rate limiting aligned with Stripe's limits:

```clojure
(def rate-limited-client
  (stripe/init-stripe
    {:api-key "sk_live_..."
     :rate-limits {:live {:default {:read 100 :write 50}
                          :files   {:read 20  :write 20}
                          :search  {:read 20  :write 0}}
                   :test {:default {:read 25  :write 25}}}}))
```

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

1. **Install stripe-mock:**
   ```bash
   brew install stripe/stripe-mock/stripe-mock
   brew services start stripe-mock
   ```

2. **Set your test API key:**
   ```bash
   export STRIPE_TEST_API_KEY='sk_test_...'
   ```

3. **Run tests:**
   ```bash
   clj -M:test -e :skip-mock
   ```

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
