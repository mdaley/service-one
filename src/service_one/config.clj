(ns service-one.config
  (:require [aero.core :as aero]
            [clojure.java.io :as io]
            [integrant.core :as ig])
  (:import [java.io Reader]
           [java.util Properties]))

(defn read-config []
  (aero/read-config (io/resource "config.edn")))

(defmethod ig/init-key :config [_ _]
  (read-config))

(defn read-file-to-properties
  [file-name]
  (with-open [^Reader reader (io/reader file-name)]
    (let [props (Properties.)]
      (.load props reader)
      (into {} (for [[k v] props] [k v])))))

(def version
  (memoize
   (fn [service-name]
     (let [pom-path (format "META-INF/maven/%s/%s/pom.properties" service-name service-name)]
       (if-let [path (.getResource (ClassLoader/getSystemClassLoader) pom-path)]
         ((read-file-to-properties path) "version")
         "development")))))
