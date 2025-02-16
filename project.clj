(defproject io.github.yonureker/stripe-clojure "0.1.2"
  :description "Clojure library for Stripe API"
  :url "http://github.com/yonureker/stripe-clojure"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.11.0"]
                 [cheshire "5.10.0"]
                 [clj-http "3.12.3"]]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :test {:dependencies [[org.clojure/test.check "1.1.1"]]}
             :dev {:dependencies [[org.clojure/tools.namespace "1.4.4"]
                                  [org.clojure/tools.nrepl "0.2.13"]]
                   :plugins [[cider/cider-nrepl "0.28.5"]]}}
  :test-selectors {:default (complement :integration)
                   :integration :integration
                   :all (constantly true)}
  :source-paths ["src"]
  :test-paths ["test"]
  :files ["src" "LICENSE" "README.md"])
