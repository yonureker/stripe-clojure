(ns stripe-clojure.files
  (:require [stripe-clojure.http.client :refer [request]]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [stripe-clojure.config :as config]))

(def stripe-files-endpoint (:files config/stripe-endpoints))
;; TBD endpoints and base urls are different

(defn- get-file-type [file-name]
  (case (str/lower-case (last (str/split file-name #"\.")))
    "pdf" "application/pdf"
    ("jpg" "jpeg") "image/jpeg"
    "png" "image/png"
    "application/octet-stream"))

(defn create-file
  "Creates a new file upload.

  Arguments:
    file-path: Path to the file to be uploaded
    purpose: Purpose of the file (e.g., 'dispute_evidence', 'identity_document')
    opts: Additional options (e.g., :stripe-account for Connect)

  Returns: 
    A File object if successful, an error object otherwise.

  Docs: https://stripe.com/docs/api/files/create"
  ([stripe-client file-path purpose]
   (create-file stripe-client file-path purpose {}))
  ([stripe-client file-path purpose opts]
   (let [file (io/file file-path)
         file-type (get-file-type (.getName file))
         multipart-params [{:name "purpose" :content purpose}
                          {:name "file"
                           :content file
                           :mime-type file-type}]]
     (request stripe-client :post stripe-files-endpoint
              {:multipart multipart-params}
              (assoc opts :content-type :multipart)))))

(defn retrieve-file
  "Retrieves the details of an existing file object.

  Arguments:
    id: The identifier of the file to be retrieved.
    opts: Additional options (e.g., :stripe-account for Connect)

  Returns:
    A File object if successful, an error object otherwise.

  Docs: https://stripe.com/docs/api/files/retrieve"
  ([stripe-client id]
   (retrieve-file stripe-client id {}))
  ([stripe-client id opts]
   (request stripe-client :get (str stripe-files-endpoint "/" id) {} opts)))

(defn list-files
  "Returns a list of file objects.

  Arguments:
    params: Map of parameters (e.g., :purpose, :limit)
    opts: Additional options (e.g., :stripe-account for Connect)

  Returns:
    A map with :data as a sequence of File objects if successful, 
    an error object otherwise.

  Docs: https://stripe.com/docs/api/files/list"
  ([stripe-client]
   (list-files stripe-client {}))
  ([stripe-client params]
   (list-files stripe-client params {}))
  ([stripe-client params opts]
   (request stripe-client :get stripe-files-endpoint params opts)))
