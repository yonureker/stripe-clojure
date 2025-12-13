(ns stripe-clojure.http.pagination)

(defn- get-next-page-params
  "Extract parameters for the next page from a Stripe API response.
   Handles both snake_case (:has_more) and kebab-case (:has-more) keys
   to support the :kebabify-keys? option."
  [response]
  (when (or (:has_more response) (:has-more response))
    {:starting-after (-> response :data last :id)}))

(defn is-paginated-endpoint?
  "Determines if the given URL represents a paginated endpoint.
  
   For auto-pagination we want to include:
     - All GET calls to a base resource listing endpoint
       (which typically have a form like `/v1/<resource>` or with a trailing slash)
     - All endpoints ending with `/search`.
  
   This function assumes that `full-url` includes the base URL (e.g. 'https://api.stripe.com').
   Adjust the regex as needed if your API domain is different."
  [url]
  (or (re-matches #".*/search/?$" url)
      (re-matches #".*/v1/[\w-]+/?$" url)))

(defn paginate
  "Handles pagination for Stripe API requests.
   
   Parameters:
   - method: The HTTP method (:get, :post, etc.)
   - url: The API endpoint URL
   - params: The request parameters
   - options: Additional options, including :auto-paginate?
   - make-request-fn: A function that takes params and makes a single API request

   Returns:
   If :auto-paginate? is true, returns a lazy sequence of all items across all pages.
   Otherwise, returns the result of a single API call."
  [method url params options make-request-fn]
  (if (:auto-paginate? options)
    (lazy-seq
     (let [response (make-request-fn params)
           items (:data response)]
       (if-let [next-page-params (get-next-page-params response)]
         (concat items (paginate method url (merge params next-page-params) options make-request-fn))
         items)))
    (make-request-fn params)))
