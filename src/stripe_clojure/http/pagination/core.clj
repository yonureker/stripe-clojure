(ns stripe-clojure.http.pagination.core
  "Pagination support for Stripe API requests.

   Provides version-aware pagination that automatically selects the appropriate
   pagination strategy:
   - v1: Cursor-based pagination with starting_after/ending_before
   - v2: URL-based pagination with next_page_url"
  (:require [stripe-clojure.http.api-version :as api-version]
            [stripe-clojure.http.pagination.protocol :as protocol]
            [stripe-clojure.http.pagination.v1 :as v1]
            [stripe-clojure.http.pagination.v2 :as v2]))

(defn get-paginator
  "Returns the appropriate paginator for the API version."
  [version]
  (if (api-version/v2? version)
    (v2/create-paginator)
    (v1/create-paginator)))

(defn paginate
  "Handles pagination for Stripe API requests.

   Parameters:
   - method: The HTTP method (:get, :post, etc.)
   - url: The API endpoint URL
   - params: The request parameters
   - options: Additional options, including :auto-paginate?
   - make-request-fn: A function that takes params and makes a single API request
   - first-page-result: (optional) Pre-fetched first page to avoid duplicate request

   Returns:
   If :auto-paginate? is true, returns a lazy sequence of all items across all pages.
   Otherwise, returns the result of a single API call."
  ([method url params options make-request-fn]
   (paginate method url params options make-request-fn nil))
  ([method url params options make-request-fn first-page-result]
   (if (:auto-paginate? options)
     (let [version (api-version/detect-version url)
           paginator (get-paginator version)]
       (lazy-seq
        (let [response (or first-page-result (make-request-fn params))
              items (protocol/get-items paginator response)]
          (if-let [next-params (protocol/next-page-params paginator response params)]
            (if (api-version/v2? version)
              (if-let [_next-url (v2/next-page-url next-params)]
                (concat items (paginate method url next-params options make-request-fn))
                items)
              (concat items (paginate method url next-params options make-request-fn)))
            items))))
     (or first-page-result (make-request-fn params)))))
