(ns stripe-clojure.http.util
  (:require [clojure.string :as str]))

;; -----------------------------------------------------------
;; Utility functions for formatting parameters and headers
;; -----------------------------------------------------------

(defn flatten-params
  "Recursively flattens a nested map into a single-level map with keys formatted
    in a way that matches Stripe API's expectations for form data.
  
    Transformations performed:
    1. Kebab-case keys are converted to snake_case (e.g., :first-name → \"first_name\")
    2. Nested maps use bracket notation (e.g., {:customer {:name \"Jane\"}} → {\"customer[name]\" \"Jane\"})
    3. Arrays/collections use indexed brackets (e.g., {:items [{:price \"x\"}]} → {\"items[0][price]\" \"x\"})
    4. String values are preserved as-is (not treated as collections)
  
    Examples:
    
    Simple nested map:
    (flatten-params {:customer {:name \"Jane\", :email \"jane@example.com\"}})
    => {\"customer[name]\" \"Jane\", \"customer[email]\" \"jane@example.com\"}
    
    Array of primitives:
    (flatten-params {:tags [\"premium\", \"subscription\"]})
    => {\"tags[0]\" \"premium\", \"tags[1]\" \"subscription\"}
    
    Complex nested structure with arrays of objects:
    (flatten-params {:subscription-schedule 
                    {:phases [{:items [{:price \"price_123\", :quantity 1}]
                              :iterations 12}]}})
    => {\"subscription_schedule[phases][0][items][0][price]\" \"price_123\",
        \"subscription_schedule[phases][0][items][0][quantity]\" 1,
        \"subscription_schedule[phases][0][iterations]\" 12}
    
    Parameters:
    - params: A nested map of parameters.
  
    Returns:
    A flat map with formatted keys suitable for Stripe API requests."
  ^clojure.lang.IPersistentMap [params]
  (let [^java.util.HashMap kebab-cache (java.util.HashMap.)
        result (transient {})]

    (letfn [(^String get-snake-case [^String s]
              (or (.get kebab-cache s)
                  (let [converted (str/replace s "-" "_")]
                    (.put kebab-cache s converted)
                    converted)))

            (^String build-key [^String prefix ^clojure.lang.Keyword k]
              (let [k-str (name k)
                    base-name (get-snake-case k-str)]
                (if (empty? prefix)
                  base-name
                  (str prefix "[" base-name "]"))))

            (process-map [^String prefix m]
              (reduce-kv (fn [_ k v]
                           (let [new-prefix (build-key prefix k)]
                             (process-value new-prefix v)))
                         nil
                         m))

            (process-indexed-coll [^String prefix coll]
              (let [cnt (count coll)]
                (loop [idx 0]
                  (when (< idx cnt)
                    (let [item (nth coll idx)
                          key-with-idx (str prefix "[" idx "]")]
                      (process-value key-with-idx item)
                      (recur (unchecked-inc idx)))))))

            (process-value [^String prefix v]
              (cond
                (map? v) (process-map prefix v)

                (and (coll? v) (not (string? v)))
                (if (counted? v)
                  (process-indexed-coll prefix v)
                  (process-indexed-coll prefix (vec v)))

                :else (assoc! result prefix v)))]

      (when params
        (process-map "" params))
      (persistent! result))))

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
    (let [values (if (string? expand) [expand] expand)
          result (transient {})]
      (if (counted? values)
        ;; Fast path for vectors and other counted collections
        (let [cnt (count values)]
          (loop [idx 0]
            (when (< idx cnt)
              (let [key (str "expand[" idx "]")
                    val (nth values idx)]
                #_{:clj-kondo/ignore [:unused-value]}
                (assoc! result key val)
                (recur (unchecked-inc idx))))))
        ;; Fallback for non-counted collections
        (loop [idx 0, items (seq values)]
          (when items
            (let [key (str "expand[" idx "]")
                  val (first items)]
              #_{:clj-kondo/ignore [:unused-value]}
              (assoc! result key val)
              (recur (unchecked-inc idx) (next items))))))
      (persistent! result))))

(defn underscore-to-kebab
  "Converts a string from underscore_case to kebab-case.

  Examples:
  - \"hello_mello\" => \"hello-mello\"
  - \"some_example_string\" => \"some-example-string\"

  Parameters:
  - s: A string in underscore_case.

  Returns:
  A kebab-case version of the input string."
  [^String s]
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
  (letfn [(transform-key [k]
            (keyword (underscore-to-kebab (name k))))
          (transform-value [v]
            (cond
              (map? v) (transform-keys v)
              (vector? v) (mapv transform-value v)
              :else v))]
    (if (map? m)
      (persistent!
       (reduce-kv (fn [acc k v]
                    (assoc! acc (transform-key k) (transform-value v)))
                  (transient {})
                  m))
      m)))