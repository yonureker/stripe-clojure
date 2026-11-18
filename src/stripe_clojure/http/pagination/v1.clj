(ns stripe-clojure.http.pagination.v1
  "V1 pagination implementation.

   Stripe v1 list endpoints use cursor-based pagination with:
   - has_more: Boolean indicating if more results exist
   - starting_after: ID of the last object to paginate after
   - ending_before: ID of the first object to paginate before (reverse)

   Search endpoints return a next_page token instead, which is sent back as
   the page parameter."
  (:require [clojure.string :as str]
            [stripe-clojure.http.pagination.protocol :as protocol]))

(defn- response-has-more?
  "Check if response has more pages. Handles both snake_case and kebab-case keys."
  [response]
  (boolean (or (:has_more response)
               (:has-more response))))

(defn- next-page-token
  "The next_page token of a search result, if any."
  [response]
  (or (:next_page response)
      (:next-page response)))

(defn- param-name
  "The name a param key is sent under. Keyword keys are sent in snake_case."
  [k]
  (if (keyword? k)
    (str/replace (name k) "-" "_")
    (str k)))

(defn- set-param
  "Sets a page param on a plain copy of params, replacing the value the caller
   passed for the first page however its key is spelled (:starting_after,
   :starting-after or \"starting_after\"). The copy keeps sorted maps working."
  [params k value]
  (let [sent-as (param-name k)]
    (-> (into {} (remove #(= sent-as (param-name (key %)))) params)
        (assoc k value))))

(defn create-paginator
  "Creates a new V1 paginator."
  []
  (reify protocol/Paginator

    (has-more? [_ response]
      (response-has-more? response))

    (next-page-params [_ response current-params]
      (when (response-has-more? response)
        (if-let [token (next-page-token response)]
          (set-param current-params :page token)
          (when-let [last-id (-> response :data last :id)]
            (set-param current-params :starting-after last-id)))))

    (get-items [_ response]
      (:data response))))
