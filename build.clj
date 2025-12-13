(ns build
  (:require [clojure.tools.build.api :as b]))

;; Project configuration
(def lib 'io.github.yonureker/stripe-clojure)
(def version "1.1.0")
(def class-dir "target/classes")
(def basis (b/create-basis {:project "deps.edn"}))
(def jar-file (format "target/%s-%s.jar" (name lib) version))

;; Clean target directory
(defn clean [_]
  (println "Cleaning target directory...")
  (b/delete {:path "target"}))

;; Build JAR file
(defn jar [_]
  (println "Building JAR file...")
  (clean nil)
  (b/write-pom {:class-dir class-dir
                :lib lib
                :version version
                :basis basis
                :src-dirs ["src"]
                :scm {:url "https://github.com/yonureker/stripe-clojure"}
                :description "Clojure library for Stripe API"
                :licenses [{:name "MIT License"
                            :url "https://opensource.org/licenses/MIT"}]})
  (b/copy-dir {:src-dirs ["src" "resources"]
               :target-dir class-dir})
  (b/jar {:class-dir class-dir
          :jar-file jar-file})
  (println "Created" jar-file))

;; Install to local Maven repository
(defn install [_]
  (println "Installing to local Maven repository...")
  (jar nil)
  (b/install {:basis basis
              :lib lib
              :version version
              :jar-file jar-file
              :class-dir class-dir})
  (println "Installed" (str lib) version "to local Maven repository"))