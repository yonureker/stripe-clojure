(ns stripe-clojure.apps.installs
  (:require [stripe-clojure.http.client :refer [request]]
            [stripe-clojure.config :as config]))

(def stripe-apps-installs-endpoint (config/stripe-endpoints :apps-installs))

(defn create-install
  "Creates an app install.
   \nStripe API docs: https://stripe.com/docs/api/apps/installs/create"
  ([stripe-client params]
   (create-install stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :post stripe-apps-installs-endpoint params opts)))

(defn retrieve-install
  "Retrieves an app install.
   \nStripe API docs: https://stripe.com/docs/api/apps/installs/retrieve"
  ([stripe-client install-id]
   (retrieve-install stripe-client install-id {}))
  ([stripe-client install-id opts]
   (request stripe-client :get
            (str stripe-apps-installs-endpoint "/" install-id)
            {}
            opts)))

(defn update-install
  "Reauthorizes an app install.
   \nStripe API docs: https://stripe.com/docs/api/apps/installs/update"
  ([stripe-client install-id params]
   (update-install stripe-client install-id params {}))
  ([stripe-client install-id params opts]
   (request stripe-client :post
            (str stripe-apps-installs-endpoint "/" install-id)
            params
            opts)))

(defn list-installs
  "Lists all app installs.
   \nStripe API docs: https://stripe.com/docs/api/apps/installs/list"
  ([stripe-client]
   (list-installs stripe-client {} {}))
  ([stripe-client params]
   (list-installs stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-apps-installs-endpoint params opts)))

(defn remove-install
  "Uninstalls an app from the account that installed it.
   \nStripe API docs: https://stripe.com/docs/api/apps/installs/uninstall"
  ([stripe-client install-id]
   (remove-install stripe-client install-id {}))
  ([stripe-client install-id opts]
   (request stripe-client :post
            (str stripe-apps-installs-endpoint "/" install-id "/uninstall")
            {}
            opts)))
