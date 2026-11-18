(ns stripe-clojure.http.pagination.v1
  "V1 pagination implementation.

   Stripe v1 list endpoints use cursor-based pagination with:
   - has_more: Boolean indicating if more results exist
   - starting_after: ID of the last object to paginate after
   - ending_before: ID of the first object to paginate before (reverse)

   Search endpoints return a next_page token instead, which is sent back as
   the page parameter."
  (:require [stripe-clojure.http.pagination.protocol :as protocol]))

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

(defn- with-cursor
  "Points params at the page after `last-id`, replacing any cursor the caller
   passed for the first page."
  [params last-id]
  (-> params
      (dissoc :starting_after "starting_after")
      (assoc :starting-after last-id)))

(defn create-paginator
  "Creates a new V1 paginator."
  []
  (reify protocol/Paginator

    (has-more? [_ response]
      (response-has-more? response))

    (next-page-params [_ response current-params]
      (when (response-has-more? response)
        (if-let [token (next-page-token response)]
          (assoc current-params :page token)
          (when-let [last-id (-> response :data last :id)]
            (with-cursor current-params last-id)))))

    (get-items [_ response]
      (:data response))))
