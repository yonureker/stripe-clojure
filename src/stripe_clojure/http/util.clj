(ns stripe-clojure.http.util
  (:require [clojure.string :as str]))

(defn flatten-params
  "Flatten a nested map into a flat map with dotted keys, handling arrays correctly for Stripe API."
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
  "Format the expand parameter for Stripe API requests.
   
   This function converts a list of fields to expand into the format required by Stripe's API.
   
   Parameters:
   - `expand`: A list or string of fields to expand.
   
   Returns:
   A map with the formatted expand parameters."
  [expand]
  (if (seq expand)
    (into {} (map-indexed (fn [idx item] [(str "expand[" idx "]") item])
                          (if (string? expand) [expand] expand)))
    {}))

(defn keyword-to-header
  "Convert a keyword to a properly formatted header string.
   
   This function converts a keyword (e.g., :stripe-account) into a header string
   (e.g., \"Stripe-Account\") required by Stripe's API.
   
   Parameters:
   - `k`: A keyword representing the header.
   
   Returns:
   A string formatted as a header."
  [k]
  (-> (name k)
      (str/replace #"-" "_")
      (str/split #"_")
      (->> (map str/capitalize)
           (str/join "-"))))

(defn format-headers
  "Format a map of headers for Stripe API requests.
   
   This function converts a map of headers with keywords as keys into a map with
   properly formatted header strings.
   
   Parameters:
   - `headers`: A map of headers with keywords as keys.
   
   Returns:
   A map with properly formatted header strings."
  [headers]
  (into {}
        (for [[k v] headers]
          [(keyword-to-header k) v])))

(defn underscore-to-kebab
  "Converts a string with underscores to kebab-case.
   
   Example:
   - `hello_mello` → `hello-mello`
   - `some_example_string` → `some-example-string`
   
   Parameters:
   - `s`: The input string with underscores.
   
   Returns:
   A kebab-case string."
  [^String s]
  (str/replace s \_ \-))

(defn transform-keys
  "Recursively transforms keys in a map (including nested maps and vectors) from underscore to kebab-case.
   
   Parameters:
   - `m`: The input map (can be nested).
   
   Returns:
   A new map with all keys transformed to kebab-case."
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