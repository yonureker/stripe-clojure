(ns stripe-clojure.unit.test-helpers
  "Reusable test assertion helpers for Stripe API resource tests.

   Provides data-driven test helpers that eliminate repetitive test patterns
   across API resource test namespaces."
  (:require [clojure.test :refer [is testing]]
            [stripe-clojure.http.client :as client]))

(defn check-endpoint
  "Asserts that an endpoint config map contains the expected path for a given key."
  [endpoints-map endpoint-key expected-path]
  (testing (str "endpoint " endpoint-key " is correctly configured")
    (is (= expected-path (get endpoints-map endpoint-key)))))

(defn check-functions-exist
  "Asserts that all given vars resolve to functions.

   Parameters:
   - fn-vars: A sequence of vars (e.g., [#'ns/fn1 #'ns/fn2])"
  [fn-vars]
  (doseq [v fn-vars]
    (testing (str (:name (meta v)) " exists")
      (is (fn? (deref v))))))

(defn check-function-arities
  "Asserts that each function has the expected set of arities.

   Parameters:
   - arity-specs: A sequence of [var expected-arities] pairs
     where expected-arities is a set of integers.
     e.g., [[#'ns/create #{2 3}] [#'ns/list #{1 2 3}]]"
  [arity-specs]
  (doseq [[v expected-arities] arity-specs]
    (testing (str (:name (meta v)) " has correct arities " expected-arities)
      (let [arities (set (map count (:arglists (meta v))))]
        (doseq [arity expected-arities]
          (is (contains? arities arity)))))))

(defn check-docstrings
  "Asserts that all given vars have docstrings, optionally matching patterns.

   Parameters:
   - doc-specs: A sequence of either:
     - vars (just checks docstring exists)
     - [var & patterns] pairs (checks docstring exists AND matches each pattern)"
  [doc-specs]
  (doseq [spec doc-specs]
    (let [[v & patterns] (if (var? spec) [spec] spec)
          doc (:doc (meta v))]
      (testing (str (:name (meta v)) " has docstring")
        (is (string? doc)))
      (doseq [pattern patterns]
        (testing (str (:name (meta v)) " docstring matches " pattern)
          (is (re-find pattern doc)))))))

(defn check-request-calls
  "Asserts that API functions call `request` with the correct HTTP method,
   endpoint URL, params, and opts.

   Parameters:
   - specs: A sequence of maps, each with:
     - :api-fn   - the function to call (e.g., v2-accounts/create-account)
     - :args     - args to pass AFTER the client (e.g., [{:email \"x\"} {:opt 1}])
     - :method   - expected HTTP method (:get, :post, :delete)
     - :endpoint - expected endpoint string
     - :params   - expected params passed to request
     - :opts     - expected opts passed to request"
  [specs]
  (doseq [{:keys [api-fn args method endpoint params opts]} specs]
    (let [captured (atom nil)]
      (with-redefs [client/request
                    (fn [_client m e p o]
                      (reset! captured {:method m :endpoint e :params p :opts o})
                      {:id "mock_result"})]
        (apply api-fn :mock-client args))
      (let [fn-name (str endpoint " " method " " args)]
        (testing (str fn-name " uses correct HTTP method")
          (is (= method (:method @captured))))
        (testing (str fn-name " hits correct endpoint")
          (is (= endpoint (:endpoint @captured))))
        (testing (str fn-name " forwards params")
          (is (= params (:params @captured))))
        (testing (str fn-name " forwards opts")
          (is (= opts (:opts @captured))))))))
