(ns stripe-clojure.http.pagination.protocol
  "Pagination protocol for Stripe API responses.

   Defines a common interface for pagination across API versions:
   - v1: Cursor-based pagination with starting_after/ending_before
   - v2: URL-based pagination with next_page_url")

(defprotocol Paginator
  "Protocol for handling pagination of Stripe API responses."

  (has-more? [this response]
    "Returns true if there are more pages available.

     Parameters:
     - response: The API response map

     Returns:
     Boolean indicating if more pages exist.")

  (next-page-params [this response current-params]
    "Extracts parameters for fetching the next page.

     Parameters:
     - response: The current API response map
     - current-params: The parameters used for the current request

     Returns:
     A map of parameters for the next page request, or nil if no more pages.")

  (get-items [this response]
    "Extracts the data items from a response.

     Parameters:
     - response: The API response map

     Returns:
     A sequence of items from the response."))
