(ns stripe-clojure.http.pagination.v2
  "V2 URL-based pagination implementation.

   Stripe v2 API uses URL-based pagination with:
   - next_page_url: Full URL for the next page of results
   - previous_page_url: Full URL for the previous page of results

   Unlike v1's cursor-based approach, v2 provides complete URLs for navigation."
  (:require [stripe-clojure.http.pagination.protocol :as protocol]))

(defn- response-has-more?
  "Check if response has more pages. V2 uses next_page_url."
  [response]
  (boolean (or (:next_page_url response)
               (:next-page-url response))))

(defn create-paginator
  "Creates a new V2 URL-based paginator."
  []
  (reify protocol/Paginator

    (has-more? [_ response]
      (response-has-more? response))

    (next-page-params [_ response _current-params]
      (when (response-has-more? response)
        (let [next-url (or (:next_page_url response)
                           (:next-page-url response))]
          {:_next-page-url next-url})))

    (get-items [_ response]
      (:data response))))

(defn next-page-url
  "Extracts the next page URL from pagination params, if present."
  [params]
  (:_next-page-url params))
