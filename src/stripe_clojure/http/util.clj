(ns stripe-clojure.http.util
  (:require [clojure.string :as str]))

;; -----------------------------------------------------------
;; Utility functions for formatting parameters and headers
;; -----------------------------------------------------------

;; Use a single recursive helper function that takes the accumulator
(declare flatten-value)

(defn- get-snake-case 
  [^String s]
  (str/replace s "-" "_"))

(defn- build-key 
  "Efficiently builds a key string with optional prefix and bracket notation."
  [^String prefix k]
  (let [k-str (if (keyword? k) (name k) (str k))
        base-name (if (keyword? k) (get-snake-case k-str) k-str)]
    (if (empty? prefix)
      base-name
      ;; Use StringBuilder for efficient string concatenation
      (let [sb (StringBuilder. (+ (.length prefix) (.length base-name) 2))]
        (.append sb prefix)
        (.append sb "[")
        (.append sb base-name)
        (.append sb "]")
        (.toString sb)))))

(defn- flatten-collection
  [acc ^String prefix coll]
  (let [items (if (counted? coll) coll (vec coll))
        cnt (count items)]
    (loop [idx 0
           current-acc acc]
      (if (< idx cnt)
        (let [item (nth items idx)
              key-with-idx (build-key prefix idx)
              next-acc (flatten-value current-acc key-with-idx item)]
          (recur (unchecked-inc idx) next-acc))
        current-acc))))

(defn- flatten-map
  [acc ^String prefix m]
  (reduce-kv (fn [current-acc k v]
               (let [new-prefix (build-key prefix k)]
                 (flatten-value current-acc new-prefix v)))
             acc
             m))

(defn- flatten-value
  [acc ^String prefix v]
  (cond
    (map? v) (flatten-map acc prefix v)

    (and (coll? v) (not (string? v)))
    (flatten-collection acc prefix v)

    :else (assoc! acc prefix v)))

(defn flatten-params
  "Recursively flattens a nested map into a single-level map with keys formatted
    in a way that matches Stripe API's expectations for form data.

    Transformations performed:
    1. Kebab-case keys are converted to snake_case (e.g., :first-name → \"first_name\")
    2. Nested maps use bracket notation (e.g., {:customer {:name \"Jane\"}} → {\"customer[name]\" \"Jane\"})
    3. Arrays/collections use indexed brackets (e.g., {:items [{:price \"x\"}]} → {\"items[0][price]\" \"x\"})
    4. String values are preserved as-is (not treated as collections)

    Parameters:
    - params: A nested map of parameters.

    Returns:
    A flat map with formatted keys suitable for Stripe API requests."
  [params]
  (when params
    (let [result (transient {})
          final-result (flatten-map result "" params)]
      (persistent! final-result))))

(defn format-expand
  "Converts an expand parameter (either a single string or a sequence of strings) 
  into the indexed format required by Stripe's API.

  Transformations performed:
  1. Single string is wrapped in a vector first
  2. Each value is assigned an indexed key: \"expand[0]\", \"expand[1]\", etc.
  3. Returns an empty map if the input is nil or empty

  Examples:
  
  Single field:
  (format-expand \"customer\")
  => {\"expand[0]\" \"customer\"}
  
  Multiple fields:
  (format-expand [\"customer\", \"invoice.subscription\"])
  => {\"expand[0]\" \"customer\", \"expand[1]\" \"invoice.subscription\"}
  
  Empty input:
  (format-expand nil)
  => {}

  Parameters:
  - expand: Either a string or a sequence of strings representing the fields to expand.

  Returns:
  A map with formatted expand parameters suitable for Stripe API requests."
  ^clojure.lang.IPersistentMap [expand]
  (if-not (seq expand)
    {}
    (let [values (if (string? expand) [expand] expand)]
      (if (counted? values)
        ;; Fast path for vectors and other counted collections
        (let [cnt (count values)
              result (transient {})]
          (loop [idx 0]
            (if (< idx cnt)
              (let [key (str "expand[" idx "]")
                    val (nth values idx)]
                #_{:clj-kondo/ignore [:unused-value]}
                (assoc! result key val)
                (recur (unchecked-inc idx)))
              (persistent! result))))
        ;; Fallback for non-counted collections - use reduce for simplicity
        (into {} (map-indexed (fn [idx val]
                                [(str "expand[" idx "]") val])
                              values))))))

(defn underscore-to-kebab
  "Converts a string from underscore_case to kebab-case.

  Examples:
  - \"hello_mello\" => \"hello-mello\"
  - \"some_example_string\" => \"some-example-string\"

  Parameters:
  - s: A string in underscore_case.

  Returns:
  A kebab-case version of the input string."
  ^String [^String s]
  (str/replace s \_ \-))

(defn transform-keys
  "Recursively transforms all keys in a map from underscore_case to kebab-case.

  This function walks through the map (including nested maps and vectors)
  and converts each keyword key to kebab-case. It leverages transients for
  efficient accumulation on larger maps.

  Parameters:
  - m: A map whose keys need to be transformed. If `m` is not a map, it's returned unchanged.

  Returns:
  A new map with all keys converted to kebab-case.

  Example:
  (transform-keys {:some_key {:nested_key \"value\"}})
  => {:some-key {:nested-key \"value\"}}"
  [m]
  (letfn [(transform-key ^clojure.lang.Keyword [k]
            (keyword (underscore-to-kebab (name k))))
          (transform-value [v]
            (cond
              (map? v) (transform-keys v)
              (vector? v) (mapv transform-value v)
              :else v))]
    (if (map? m)
      (persistent!
       (reduce-kv (fn [^clojure.lang.ITransientMap acc k v]
                    (assoc! acc (transform-key k) (transform-value v)))
                  (transient {})
                  m))
      m)))