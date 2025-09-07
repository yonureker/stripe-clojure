# Stripe Clojure SDK

[![Clojars Project](https://img.shields.io/clojars/v/io.github.yonureker/stripe-clojure.svg)](https://clojars.org/io.github.yonureker/stripe-clojure)
[![GitHub Release](https://img.shields.io/github/v/release/yonureker/stripe-clojure)](https://github.com/yonureker/stripe-clojure/releases)
[![License](https://img.shields.io/github/license/yonureker/stripe-clojure)](LICENSE)

> **Production-ready Clojure SDK for the Stripe API**

A comprehensive, idiomatic Clojure library providing convenient access to the [Stripe API](https://stripe.com/docs/api). Officially [listed by Stripe](https://docs.stripe.com/sdks/community) as a community-supported SDK.

## ✨ Features

- **🎯 Complete API Coverage** - All Stripe API endpoints and resources
- **⚡ High Performance** - Zero-overhead design with intelligent rate limiting
- **🔒 Production Ready** - Comprehensive error handling, retries, and validation
- **🧪 Well Tested** - 551 tests with 100% coverage using stripe-mock
- **📖 Auto-Pagination** - Lazy sequences for handling large datasets
- **🔌 Event System** - Request/response lifecycle hooks for monitoring
- **🌊 Flexible** - Multiple client instances with isolated configurations
- **📋 Idiomatic** - Clean Clojure APIs with proper data structures

## 🚀 Quick Start

### Installation

Add to your `deps.edn`:
```clojure
{:deps {io.github.yonureker/stripe-clojure {:mvn/version "1.0.0"}}}
```

Or in `project.clj`:
```clojure
[io.github.yonureker/stripe-clojure "1.0.0"]
```

### Basic Usage

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

## 📚 Documentation

### Client Configuration

Create isolated client instances with custom configurations:

```clojure
(def production-client 
  (stripe/init-stripe 
    {:api-key "sk_live_..."
     :api-version "2025-08-27.basil"
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

#### Configuration Options

| Parameter | Default | Description |
|-----------|---------|-------------|
| `:api-key` | **Required** | Your Stripe secret API key |
| `:api-version` | `"2025-08-27.basil"` | Stripe API version to use |
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

### Per-Request Options

In addition to the global options set during initialization, many API methods allow you to override a subset of these options on a per-request basis (passed as part of `opts` map). For example, you can override:

- `:api-version`, and `:stripe-account` – to dynamically switch contexts.
- `:max-network-retries` and `:timeout` – if a particular call requires alternative networking parameters.
- `:full-response?` – to get more detailed request/response info for debugging.
- `:idempotency-key` – for safely retryable POST requests.
- `:expand` – to automatically expand linked objects in a single API request (reducing the need for additional fetches).
- `:auto-paginate?` – when set to `true`, automatically follows the pagination cursor to return the complete dataset as a lazy sequence.

These options provide flexibility to tailor behavior both globally and at a per-request level.

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

### Expanding Responses

Stripe API has an Expand feature that allows you to retrieve linked objects in a single call, effectively replacing the object ID with all its properties and values.

For example when you retrieve a payment intent, the `:customer` key only has the id of the customer.

```clojure
(def pi-retrieved (payment-intents/retrieve-payment-intent stripe-client "pi_1234"))

;; payment intent response
{:description "payment for order"
 :id "pi_1234"
 ...
 :customer "cus_1234" ;; only customer id returned
 :payment-method "pm_1234"
 }
```

Using expand, we can actually retrieve the whole customer object in the response without an additional request. We need to pass `:expand` key with a vector of fields in strings as part of `opts`.

```clojure
(def pi-with-customer-info (payment-intents/retrieve-payment-intent 
                                stripe-client 
                                "pi_1234" 
                                {:expand ["customer"]}))

;; response now has the customer object
{:description "payment for order"
 :id "pi_1234"
 ...
 :customer {:name "Jane Doe" :email "jane@example.com" ...} ;; now we have the customer object
 :payment_method "pm_1234"
 }
```

We can pass multiple fields with `:expand`:

```clojure
(def pi-with-customer-info (payment-intents/retrieve-payment-intent 
                                stripe-client 
                                "pi_1234" 
                                {:expand ["customer" "payment_method"]}))

;; both customer and payment_method is expanded
{:description "payment for order"
 :id "pi_1234"
 ...
 :customer {:name "Jane Doe" :email "jane@example.com" ...}
 :payment_method {:type "card" :last4 "4242" ...}
 }
```

### Auto-Pagination

stripe-clojure supports auto-pagination for both list and search endpoints that return paginated results. When you set `:auto-paginate? true`, the library automatically follows the `has_more` flag by issuing successive requests (using the `:starting_after` parameter) until all pages are fetched. The results are returned as a lazy sequence, so you can process data on demand without loading everything into memory. You can also control the number of results per API call using the `:limit` option.

**List/Search Example:**

```clojure
(ns my.project
  (:require [stripe-clojure.core :as stripe]
            [stripe-clojure.customers :as customers]))

(def stripe-client (stripe/init-stripe {:api-key "your_stripe_api_key"}))

;; Auto-paginate through a list endpoint with a per-page limit.
(def all-customers
  (customers/list-customers stripe-client {:limit 50} {:auto-paginate? true}))

;; Auto-paginate through a search endpoint using Stripe Search Query Language.
;; https://docs.stripe.com/search#search-query-language
(def search-query "email:'sally@rocketrides.io'")
(def matching-customers
  (customers/search-customers stripe-client {:query search-query :limit 50} {:auto-paginate? true}))

;; Process and print list results lazily.
(println "List Customers:")
(doseq [customer all-customers]
  (println (:id customer)))

;; Process and print search results lazily.
(println "Search Customers:")
(doseq [customer matching-customers]
  (println (:id customer)))
```

In this example, the list and search endpoints are both configured to auto-paginate. Results are processed as lazy sequences, with each API call fetching up to 50 items, until all matching data is retrieved.

### Request and Response Events

The Stripe Clojure client emits `request` and `response` events, allowing you to hook into the lifecycle of API requests. This can be useful for logging, debugging, or modifying requests and responses.

**Example Usage:**

```clojure
(ns your-namespace
  (:require [stripe-clojure.core :as stripe]))

;; Initialize the Stripe client
(def stripe-client (stripe/init-stripe {:api-key "sk_test_..."}))

;; Define an event handler function for requests
(defn on-request [request]
  ;; Do something with the request
  (println "Request event:" request))

;; Add the event handler function
(stripe/on stripe-client :request on-request)

;; Remove the event handler function
(stripe/off stripe-client :request on-request)
```

`request` object

```clojure
{:api_version "2020-05-28"
 :account "acct_TEST"              ;; Only present if provided
 :idempotency_key "abc123"         ;; Only present if provided
 :method "POST"
 :path "/v1/customers"
 :request_start_time 1565125303932 ;; Unix timestamp in milliseconds
}
```

`response` object

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

### Network Retries

Unless you explicitly set `:max-network-retries` to zero (either globally when initializing a Stripe instance or per request), stripe-clojure will automatically retry failed requests that are considered safe to repeat. Specifically, the library will retry requests that return one of the following HTTP status codes:

- **409 (Conflict):** Indicates a rare race condition.
- **429 (Too Many Requests):** Signals that the rate limit has been exceeded.
- **Statuses greater than 500:** Covering server errors such as 500, 503, and others.

### Error Handling

The SDK provides structured error information:

```clojure
(try
  (customers/retrieve-customer stripe-client "invalid_id")
  (catch Exception e
    (let [error-data (ex-data e)]
      (println "Error:" (:message error-data))
      (println "Type:" (:type error-data))
      (println "Status:" (:status error-data)))))
```

### Multiple Environments

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

## 🏗️ Advanced Features

### Connection Pooling

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

### Rate Limiting

Configure intelligent rate limiting that aligns with Stripe's limits:

```clojure
(def rate-limited-client
  (stripe/init-stripe
    {:api-key "sk_live_..."
     :rate-limits {:live {:default {:read 100 :write 50}
                          :files   {:read 20  :write 20}
                          :search  {:read 20  :write 0}}
                   :test {:default {:read 25  :write 25}}}}))
```

### Webhook Verification

Securely verify webhook signatures:

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

## 🧪 Testing

### Development Testing

The SDK includes comprehensive testing support with stripe-mock:

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

### Integration Testing

Use the test environment for integration tests:

```clojure
(def test-client (stripe/init-stripe {:api-key test-api-key}))

;; Create test data
(def test-customer 
  (customers/create-customer test-client 
    {:name "Test Customer"
     :email "test@example.com"}))

;; Test your application logic
(assert (= "Test Customer" (:name test-customer)))
```

## 📊 Performance

The Stripe Clojure SDK is designed for production performance:

- **Zero-overhead throttling** - Intelligent rate limiting with minimal impact
- **Connection pooling** - Reuse HTTP connections for better throughput  
- **Lazy pagination** - Process large datasets without memory issues
- **Smart retries** - Exponential backoff with jitter for resilience
- **Efficient serialization** - Optimized parameter flattening

Benchmark results show 200x performance improvement in throttling overhead compared to naive implementations.

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guide](CONTRIBUTING.md) for details.

### Development Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/yonureker/stripe-clojure.git
   cd stripe-clojure
   ```

2. Install dependencies:
   ```bash
   clj -P  # Download dependencies
   ```

3. Run tests:
   ```bash
   clj -M:test
   ```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

- **Documentation**: [Stripe API Docs](https://stripe.com/docs/api)
- **Issues**: [GitHub Issues](https://github.com/yonureker/stripe-clojure/issues)
- **Community**: [Clojurians Slack](https://clojurians.slack.com) (#stripe-clojure)
- **Security**: Please report security issues to [GitHub Issues](https://github.com/yonureker/stripe-clojure/issues)

## 🚀 What's New in 1.0.0

- **Complete API Coverage** - All Stripe endpoints implemented
- **Performance Optimizations** - Zero-overhead design for production use
- **Enhanced Error Handling** - Better error messages and debugging info
- **Production Hardening** - Comprehensive testing and validation
- **Improved Documentation** - Complete API documentation and examples

---

<div align="center">

**Built with ❤️ for the Clojure community**

[⭐ Star us on GitHub](https://github.com/yonureker/stripe-clojure) | [🐛 Report Issues](https://github.com/yonureker/stripe-clojure/issues) | [📖 Read the Docs](https://github.com/yonureker/stripe-clojure/wiki)

</div>