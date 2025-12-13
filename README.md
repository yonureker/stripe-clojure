# stripe-clojure

[![Clojars Project](https://img.shields.io/clojars/v/io.github.yonureker/stripe-clojure.svg)](https://clojars.org/io.github.yonureker/stripe-clojure)
[![GitHub Release](https://img.shields.io/github/v/release/yonureker/stripe-clojure)](https://github.com/yonureker/stripe-clojure/releases)
[![License](https://img.shields.io/github/license/yonureker/stripe-clojure)](LICENSE)

Clojure SDK for the [Stripe API](https://stripe.com/docs/api). Listed by Stripe as a [community-supported SDK](https://docs.stripe.com/sdks/community).

## Features

- Complete Stripe API coverage
- Auto-pagination with lazy sequences
- Connection pooling and rate limiting
- Automatic retries with exponential backoff
- Webhook signature verification
- Request/response event hooks

## Installation

Add to your `deps.edn`:
```clojure
{:deps {io.github.yonureker/stripe-clojure {:mvn/version "1.1.0"}}}
```

Or in `project.clj`:
```clojure
[io.github.yonureker/stripe-clojure "1.1.0"]
```

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

## Client Configuration

Create isolated client instances with custom configurations:

```clojure
(def production-client 
  (stripe/init-stripe 
    {:api-key "sk_live_..."
     :api-version "2025-11-17.clover"
     :max-network-retries 3
     :timeout 30000
     :use-connection-pool? true
     :pool-options {:threads 8 :timeout 10}
     :rate-limits {:live {:default {:read 50 :write 25}}}}))

(def development-client
  (stripe/init-stripe 
    {:api-key "sk_test_..."
     :full-response? true  ; Include headers and metadata
     :kebabify-keys? true  ; Convert snake_case to kebab-case
     :max-network-retries 1}))
```

### Configuration Options

| Parameter | Default | Description |
|-----------|---------|-------------|
| `:api-key` | **Required** | Your Stripe secret API key |
| `:api-version` | `"2025-11-17.clover"` | Stripe API version to use |
| `:stripe-account` | `nil` | Connect account ID for platform requests |
| `:max-network-retries` | `1` | Number of automatic retries for failed requests |
| `:timeout` | `80000` | Request timeout in milliseconds |
| `:host` | `"api.stripe.com"` | Stripe API hostname |
| `:port` | `443` | API port number |
| `:protocol` | `"https"` | Protocol to use (always use HTTPS in production) |
| `:rate-limits` | [See config](src/stripe_clojure/config.clj) | Custom rate limiting configuration |
| `:use-connection-pool?` | `false` | Enable HTTP connection pooling |
| `:pool-options` | [See config](src/stripe_clojure/config.clj) | Connection pool settings |
| `:full-response?` | `false` | Return complete HTTP response or just body |
| `:kebabify-keys?` | `false` | Convert response keys to kebab-case |

## Per-Request Options

Many API methods allow you to override options on a per-request basis via the `opts` map:

- `:api-version`, `:stripe-account` – to dynamically switch contexts
- `:max-network-retries`, `:timeout` – for alternative networking parameters
- `:full-response?` – for detailed request/response info
- `:idempotency-key` – for safely retryable POST requests
- `:expand` – to expand linked objects in a single request
- `:auto-paginate?` – to return complete dataset as a lazy sequence

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

## Auto-Pagination

Auto-pagination for list and search endpoints returns results as a lazy sequence. The library automatically follows the `has_more` flag using the `:starting_after` parameter until all pages are fetched.

```clojure
(ns my.project
  (:require [stripe-clojure.core :as stripe]
            [stripe-clojure.customers :as customers]))

(def stripe-client (stripe/init-stripe {:api-key "your_stripe_api_key"}))

;; Auto-paginate through a list endpoint with a per-page limit
(def all-customers
  (customers/list-customers stripe-client {:limit 50} {:auto-paginate? true}))

;; Auto-paginate through a search endpoint using Stripe Search Query Language
;; https://docs.stripe.com/search#search-query-language
(def search-query "email:'sally@rocketrides.io'")
(def matching-customers
  (customers/search-customers stripe-client {:query search-query :limit 50} {:auto-paginate? true}))

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
{:api_version "2020-05-28"
 :account "acct_TEST"              ;; Only present if provided
 :idempotency_key "abc123"         ;; Only present if provided
 :method "POST"
 :path "/v1/customers"
 :request_start_time 1565125303932 ;; Unix timestamp in milliseconds
}
```

`response` object:
```clojure
{:api_version "2020-05-28"
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

## Network Retries

Unless `:max-network-retries` is set to zero, requests are automatically retried for:

- **409 (Conflict):** Rare race condition
- **429 (Too Many Requests):** Rate limit exceeded
- **5xx:** Server errors

Retries use exponential backoff with jitter.

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

## Connection Pooling

Enable connection pooling for high-throughput applications:

```clojure
(def pooled-client
  (stripe/init-stripe 
    {:api-key "sk_live_..."
     :use-connection-pool? true
     :pool-options {:max-total 50
                    :max-per-route 20
                    :timeout 30
                    :threads 8}}))
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
   clj -M:test
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
