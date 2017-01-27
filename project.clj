(defproject service-one "0.0.1"
  :description "This is the service-one system"
  :url ""
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.1"]
                 [ring/ring-defaults "0.2.1"]
                 [ring/ring-jetty-adapter "1.5.0"]
                 [integrant "0.1.5"]

                 [com.taoensso/timbre "4.8.0"]
                 [org.slf4j/log4j-over-slf4j "1.7.14"]
                 [org.slf4j/jul-to-slf4j "1.7.14"]
                 [org.slf4j/jcl-over-slf4j "1.7.14"]
                 [ch.qos.logback/logback-classic "1.1.7"]

                 [aero "1.0.1"]
                 [digest "1.4.5"]
                 [clj-http "2.3.0"]
                 [cheshire "5.6.3"]
                 [ring/ring-json "0.4.0"]]

  :profiles {:dev {:source-paths ["src" "dev" "test" "it"]
                   :test-paths ["test" "it"]
                   :dependencies [[ring/ring-mock "0.3.0"]]}
             :uberjar {:aot :all}}

  :test-selectors {:default (complement :integration)
                   :integration :integration
                   :all (constantly true)}

  :main service-one.main

  :lein-release {:scm :git})
