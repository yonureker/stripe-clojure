(ns stripe-clojure.http.api-version
  "API version detection and constants for Stripe v1 and v2 APIs.

   Key differences between v1 and v2:
   - Request encoding: v1 uses form-urlencoded, v2 uses JSON
   - Pagination: v1 uses cursor-based (starting_after), v2 uses URL-based (next_page_url)
   - Field expansion: v1 uses 'expand' parameter, v2 uses 'include' parameter
   - Idempotency: v1 has 24h window, v2 has 30d window"
  (:require [clojure.string :as str]))

(def V1
  "Stripe API version 1 identifier."
  :v1)

(def V2
  "Stripe API version 2 identifier."
  :v2)

(defn detect-version
  "Auto-detect API version from endpoint path.

   Returns :v2 for paths starting with '/v2/', otherwise returns :v1.
   Returns :v1 for nil or empty endpoints.

   Examples:
   (detect-version \"/v1/customers\")  ; => :v1
   (detect-version \"/v2/core/events\") ; => :v2"
  [endpoint]
  (if (and endpoint (str/starts-with? endpoint "/v2/"))
    V2
    V1))

(defn v1?
  "Returns true if the API version is v1."
  [version]
  (= version V1))

(defn v2?
  "Returns true if the API version is v2."
  [version]
  (= version V2))
