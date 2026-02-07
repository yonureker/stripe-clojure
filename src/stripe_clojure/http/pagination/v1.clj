(ns stripe-clojure.http.pagination.v1
  "V1 cursor-based pagination implementation.

   Stripe v1 API uses cursor-based pagination with:
   - has_more: Boolean indicating if more results exist
   - starting_after: ID of the last object to paginate after
   - ending_before: ID of the first object to paginate before (reverse)"
  (:require [stripe-clojure.http.pagination.protocol :as protocol]))

(defn- response-has-more?
  "Check if response has more pages. Handles both snake_case and kebab-case keys."
  [response]
  (boolean (or (:has_more response)
               (:has-more response))))

(defn create-paginator
  "Creates a new V1 cursor-based paginator."
  []
  (reify protocol/Paginator

    (has-more? [_ response]
      (response-has-more? response))

    (next-page-params [_ response current-params]
      (when (response-has-more? response)
        (let [items (or (:data response) [])
              last-id (-> items last :id)]
          (when last-id
            (assoc current-params :starting-after last-id)))))

    (get-items [_ response]
      (:data response))))
