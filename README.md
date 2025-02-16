
# Stripe Clojure Library

![Clojars Project](https://img.shields.io/clojars/v/io.github.yonureker/stripe-clojure.svg)


The Stripe Clojure library provides convenient access to the Stripe API from applications written in Clojure.

> [!CAUTION]
> This library is still under development and the API is not stable. Breaking changes are expected.

## Installation

#### Leiningen / Boot

In `project.clj` file, add the dependency to `:dependencies`

```clojure
[io.github.yonureker/stripe-clojure "0.1.3"]
```

#### deps.edn

If you are using `deps.edn` file, add the dependency to `:deps `

```clojure
io.github.yonureker/stripe-clojure {:mvn/version "0.1.3"}
```

## Usage

All you need is to initialize a stripe client instance with the secret API key and pass it to any method provided by the library.

```clojure
;; In your ns statement:
(ns my.ns
  (:require [stripe-clojure.core :as stripe]
            [stripe-clojure.customers :as customers]))

;; client instance has to be initialized once
;; it is reusable
(def stripe-client (stripe/init-stripe {:api-key <your-api-key>}))

;; Create a customer with name and email
(customers/create-customer stripe-client {:name "Onur Eker"
                                          :email "yonureker@gmail.com})
```


**Examples:**

```clojure
;; passing params and opts
(customers/create-customer client-instance {:name "Onur" :email "yonureker@gmail.com"} 
                                           {:max-network-retries 5 :idempotency-key "key_1234"})

;; passing id and opts
(payment-intents/retrieve-payment-intent client-instance "pi_1234" {:max-network-retries 5})

;; passing id only
(customers/retrieve-customer client-instance "pi_1234")

;; passing opts only
(customers/list-customers client-instance {} {:auto-paginate? true})

```

## Client Instances

Each client instance is designed to be fully isolated. When you create a Stripe client instance, its configuration becomes immutable—even though you can override certain options on a per-request basis, the underlying instance remains unchanged.

This approach lets you create multiple, independent client instances with different configurations. You can pass these instances around throughout your application as needed, allowing for distinct settings (such as API keys, timeouts, or rate limits) in different parts of your code.
#### Requiring instance from another namespace:
```clojure
(ns my.other.ns
  ;; import previously creeated client instance
  (:require [my.ns :refer [stripe-client]]
            [stripe-clojure.customers :as customers]))

(customers/retrieve-customer stripe-client "cus_12345")
```

#### Initializing and using multiple instances:

```clojure
(ns my.other-other.ns
  (:require [stripe-clojure.core :as stripe]
            [stripe-clojure.customers :as customers]))

;; Initializing
(def us-client (stripe/init-stripe {:api-key <us-api-key>}))
(def eu-client (stripe/init-stripe {:api-key <eu-api-key>}))
(def no-retry-client (stripe/init-stripe {:api-key <some-api-key>
                                          :max-network-retries 0}))

;; Using with requests
(customers/retrieve-customer us-client "cus_123445")
(customers/list-customers eu-client {:limit 10})
(customers/create-customer no-retry-client {:name "onur"
                                            :email "yonureker@gmail.com})

;; They can be shutdown, releasing any internal and pooled resources.
(stripe/shutdown-stripe-client! eu-client)
```


## Client instance configuration

Use a configuration map to initialize a stripe client instance:

```clojure
(ns my.ns
  (:require [stripe-clojure.core :as stripe])

(def stripe-instance (stripe/init-stripe {:api-key "test_key_123"
                                          :api-version "2020-05-28"
                                          :max-network-retries 3
                                          :timeout 2000
                                          :rate-limits {:live {:default {:read 5 :write 5}
                                                               :search {:read 10 :write 0}}
                                                        :test {:default {:read 20 :write 20}}}
                                          :use-connection-pool? true
                                          :pool-options {:timeout 5
                                                         :threads 4
                                                         :default-per-route 2
                                                         :insecure? false}
                                          :full-response? false}))


```

These fields can be configured when creating a stripe client instance.


| Option              | Default            | Description                                                                                                                                                                                                                                       |
| ------------------- | ------------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `:api-key`        | `N/A`             | Secret API Key from your Stripe account. (REQUIRED)                                                                                                                                  |
| `:api-version`        | `nil`             | Stripe API version to be used. If not set, stripe-clojure will use the latest version at the time of release.                                                                                                                                        |
| `:stripe-account`        | `nil`             | For Connect, the Stripe account to use.                                                                                                                                        |
| `:max-network-retries` | 1                  | The amount of times a request should be retried.                                                                                                                                                                              |
| `:timeout`           | 80000              | Maximum time each request can take in ms.                                                                                                                                                                           |
| `:host`              | `api.stripe.com` | Host that requests are made to.                                                                                                                                                                                                                   |
| `:port`              | 443                | Port that requests are made to.                                                                                                                                                                                                                   |
| `:protocol`          | `https`          | `'https'` or `'http'`. `http` is never appropriate for sending requests to Stripe servers, and we strongly discourage `http`, even in local testing scenarios, as this can result in your credentials being transmitted over an insecure channel. |
| `:rate-limits`         | `See config.clj`             | Custom rate limits to use.|
`:use-connection-pool?`         | `false`             | To enable connection pool.|
`:pool-options`         | `See config.clj`             | Configuration for behavior of the HTTP connection pool.|
`:full-response?`         | `false`             | Option to enable the complete HTTP response (including metadata, headers, status etc.); or just the `:body` (e.g. created customer object)|

`:api-version`, `:stripe-account`, `:max-network-retries`, `:timeout`, and `:full-response?` can be overriden on a per-request basis.

## Per-request Options

In addition to the global options set during initialization, many API methods allow you to override a subset of these options on a per-request basis (passed as part of `opts` map). For example, you can override:

- `:api-version`, and `:stripe-account` – to dynamically switch contexts.
- `:max-network-retries` and `:timeout` – if a particular call requires alternative networking parameters.
- `:full-response?` – to get more detailed request/response info for debugging.
- `:idempotency-key` – for safely retryable POST requests.
- `:expand` – to automatically expand linked objects in a single API request (reducing the need for additional fetches).
- `:auto-paginate?` – when set to `true`, automatically follows the pagination cursor to return the complete dataset as a lazy sequence.

These options provide flexibility to tailor behavior both globally and at a per-request level.

## Network retries

Unless you explicitly set `:max-network-retries` to zero (either globally when initializing a Stripe instance or per request), stripe-clojure will automatically retry failed requests that are considered safe to repeat. Specifically, the library will retry requests that return one of the following HTTP status codes:

- **409 (Conflict):** Indicates a rare race condition.
- **429 (Too Many Requests):** Signals that the rate limit has been exceeded.
- **Statuses greater than 500:** Covering server errors such as 500, 503, and others.

There's one important exception: failed POST requests that do not include an `:idempotency-key` will never be retried, regardless of your `:max-network-retries` setting. This prevents unintended duplicate actions when the operation isn't safely repeatable.

## Expanding responses

Stripe API has an Expand feature that allows you to retrieve linked objects in a single call, effectively replacing the object ID with all its properties and values.

For example when you retrive a payment intent, the `:customer` key only has the id of the customer.

```clojure
...

(def pi-retrieved (payment-intents/retrieve-payment-intent stripe-instance "pi_1234"))

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
                                stripe-instance 
                                "pi_1234" 
                                {:expand ["customer"]}))

;; response now has the customer object
{:description "payment for order"
 :id "pi_1234"
 ...
 :customer {:name "onur" :email "yonureker@gmail.com" ...} ;; now we have the customer object
 :payment_method "pm_1234"
 }

```

We can pass multiple fields with `:expand`, 

```clojure
(def pi-with-customer-info (payment-intents/retrieve-payment-intent 
                                stripe-instance 
                                "pi_1234" 
                                {:expand ["customer" "payment_method"]}))

;; both customer and payment_method is expanded
{:description "payment for order"
 :id "pi_1234"
 ...
 :customer {:name "onur" :email "yonureker@gmail.com" ...}
 :payment_method {:type "card" :last4 "4242" ...}
 }
```

## Auto-Pagination

stripe-clojure supports auto-pagination for both list and search endpoints that return paginated results. When you set `:auto-paginate? true`, the library automatically follows the `has_more` flag by issuing successive requests (using the `:starting_after` parameter) until all pages are fetched. The results are returned as a lazy sequence, so you can process data on demand without loading everything into memory. You can also control the number of results per API call using the `:limit` option.

**List/Search Example:**

```clojure:example/autopaginate_combined.clj
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

## Connection Pooling

stripe-clojure can use a connection pool to manage HTTP connections. Connection pooling is disabled by default. You can enable it by setting `:use-connection-pool? true` when initializing a stripe client instance and configuring the pool with `:pool-options`. Here is an example:

```clojure
(def stripe-client (stripe/init-stripe {:api-key "your_stripe_api_key"
                                          :use-connection-pool? true
                                          :pool-options {:timeout 5
                                                         :threads 4
                                                         :default-per-route 2
                                                         :insecure? false}}))
```

## Testing

stripe-clojure comes with a suite of tests in the `test/` folder, and you can run these tests with your own Stripe test API key.

By default, the test API key is defined in `src/stripe_clojure/config.clj` like so:

```clojure:src/stripe_clojure/config.clj
(def api-keys
  {:test (or (System/getenv "STRIPE_TEST_API_KEY") "test_api_key")})
```

To test with your own key, set the environment variable `STRIPE_TEST_API_KEY` before running the tests. For instance, on Unix-like systems you can do:

```bash
export STRIPE_TEST_API_KEY='sk_test_1234567890abcdef'
```

Install stripe-mock and run it. Instructions are available [here](https://github.com/stripe/stripe-mock?tab=readme-ov-file#homebrew)

Then, run the tests using Leiningen:

```bash
lein test
```