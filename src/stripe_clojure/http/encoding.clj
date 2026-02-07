(ns stripe-clojure.http.encoding
  "Request encoding multimethods for Stripe API v1 and v2.

   Dispatches encoding based on API version:
   - v1: application/x-www-form-urlencoded with nested bracket notation
   - v2: application/json with native JSON encoding"
  (:require [stripe-clojure.http.api-version :as api-version]
            [stripe-clojure.http.util :as util]
            [cheshire.core :as json]))

;; -----------------------------------------------------------------------------
;; Content Type
;; -----------------------------------------------------------------------------

(defmulti content-type
  "Returns the appropriate Content-Type header for the API version."
  (fn [version] version))

(defmethod content-type api-version/V1 [_]
  "application/x-www-form-urlencoded")

(defmethod content-type api-version/V2 [_]
  "application/json")

(defmethod content-type :default [_]
  "application/x-www-form-urlencoded")

;; -----------------------------------------------------------------------------
;; Parameter Encoding
;; -----------------------------------------------------------------------------

(defn- keyword->snake-case
  "Converts a keyword to snake_case string."
  [k]
  (util/kebab-to-snake (name k)))

(defn- transform-keys-to-snake-case
  "Recursively transforms all keyword keys in a map to snake_case strings."
  [m]
  (cond
    (map? m)
    (into {}
          (map (fn [[k v]]
                 [(if (keyword? k) (keyword->snake-case k) k)
                  (transform-keys-to-snake-case v)]))
          m)

    (sequential? m)
    (mapv transform-keys-to-snake-case m)

    :else m))

(defmulti encode-params
  "Encodes request parameters based on API version.

   v1: Flattens params with bracket notation for form-urlencoded
   v2: Converts to JSON string with snake_case keys"
  (fn [version _params] version))

(defmethod encode-params api-version/V1 [_ params]
  (util/flatten-params params))

(defmethod encode-params api-version/V2 [_ params]
  (when (seq params)
    (json/generate-string (transform-keys-to-snake-case params))))

(defmethod encode-params :default [_ params]
  (util/flatten-params params))

;; -----------------------------------------------------------------------------
;; Field Expansion
;; -----------------------------------------------------------------------------

(defmulti format-expansion
  "Formats field expansion parameters based on API version.

   v1: Uses 'expand' parameter with indexed bracket notation
   v2: Uses 'include' parameter with indexed bracket notation"
  (fn [version _expand-fields] version))

(defmethod format-expansion api-version/V1 [_ expand-fields]
  (util/format-expand expand-fields))

(defmethod format-expansion api-version/V2 [_ include-fields]
  (util/format-indexed-params "include" include-fields))

(defmethod format-expansion :default [_ expand-fields]
  (util/format-expand expand-fields))

;; -----------------------------------------------------------------------------
;; Expansion Parameter Selection
;; -----------------------------------------------------------------------------

(defn get-expansion-fields
  "Gets the appropriate expansion fields based on API version.

   For user convenience, accepts both :expand and :include for any version
   and maps to the correct parameter:
   - v1: Uses 'expand' parameter (prefers :expand, falls back to :include)
   - v2: Uses 'include' parameter (prefers :include, falls back to :expand)

   This allows users to use either option without needing to remember
   which version uses which parameter name."
  [version opts]
  (if (api-version/v2? version)
    ;; V2: prefer :include, fall back to :expand for convenience
    (or (:include opts) (:expand opts))
    ;; V1: prefer :expand, fall back to :include for convenience
    (or (:expand opts) (:include opts))))
