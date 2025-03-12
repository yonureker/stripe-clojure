(ns stripe-clojure.http.util
  (:require [clojure.string :as str]))

;; -----------------------------------------------------------
;; Utility functions for formatting parameters and headers
;; -----------------------------------------------------------

(defn flatten-params
  "Recursively flattens a nested map into a single-level map with keys formatted
  in a way that matches Stripe API's expectations for form data.

  Nested maps are flattened using square bracket notation. Arrays (or collections)
  that are not strings have each element indexed in the key.

  Example:
  (flatten-params {:customer {:name \"Jane\"
                              :emails [\"jane@example.com\" \"jdoe@example.com\"]}})
  Might yield:
  {\"customer[name]\" \"Jane\",
   \"customer[emails][0]\" \"jane@example.com\",
   \"customer[emails][1]\" \"jdoe@example.com\"}

  Parameters:
  - params: A nested map of parameters.

  Returns:
  A flat map with formatted keys."
  [params]
  (letfn [(kebab->snake [s]
            (str/replace s "-" "_"))
          (flatten-key [prefix k]
            (let [base-name (-> k name kebab->snake)]
              (if (empty? prefix)
                base-name
                (str prefix "[" base-name "]"))))
          (flatten* [prefix acc [k v]]
            (cond
              (map? v)
              (reduce (partial flatten* (flatten-key prefix k)) acc v)

              (and (coll? v) (not (string? v)))
              (reduce (fn [a [i x]]
                        (if (map? x)
                          (reduce (partial flatten* (str (flatten-key prefix k) "[" i "]")) a x)
                          (assoc a (str (flatten-key prefix k) "[" i "]") x)))
                      acc
                      (map-indexed vector v))

              :else
              (assoc acc (flatten-key prefix k) v)))]
    (reduce (partial flatten* "") {} params)))

(defn format-expand
  "Formats the 'expand' parameter for a Stripe API request.

  It converts either a single expand field (as a string) or a sequence of fields 
  into a map with keys formatted as \"expand[<index>]\" and the value as the field name.

  Parameters:
  - expand: Either a string or a sequence of strings representing the fields to expand.

  Returns:
  A map with formatted expand parameters.

  Example:
  (format-expand \"customer\")
  => {\"expand[0]\" \"customer\"}"
  [expand]
  (if (seq expand)
    (into {} (map-indexed (fn [idx item] [(str "expand[" idx "]") item])
                          (if (string? expand) [expand] expand)))
    {}))

(defn keyword-to-header
  "Converts a keyword into a properly formatted HTTP header string.

  The conversion process involves:
  1. Converting the keyword to a string.
  2. Replacing dashes with underscores.
  3. Splitting on underscores.
  4. Capitalizing each section.
  5. Joining the sections with dashes.

  For example:
  :stripe-account => \"Stripe-Account\"

  Parameters:
  - k: A keyword representing the header.

  Returns:
  A well-formatted header string."
  [k]
  (-> (name k)
      (str/replace #"-" "_")
      (str/split #"_")
      (->> (map str/capitalize)
           (str/join "-"))))

(defn format-headers
  "Formats a map of HTTP headers for Stripe API requests.

  This function converts a map with keyword keys into a new map where
  the keys are proper HTTP header strings (using `keyword-to-header`) and
  the values remain unchanged.

  Parameters:
  - headers: A map with keyword keys.

  Returns:
  A new map with properly formatted HTTP header keys."
  [headers]
  (into {}
        (for [[k v] headers]
          [(keyword-to-header k) v])))

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