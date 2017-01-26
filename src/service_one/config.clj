(ns service-one.config
  (:require [aero.core :as aero]
            [clojure.java.io :as io]
            [integrant.core :as ig]))

(defn read-config []
  (aero/read-config (io/resource "config.edn")))

(defmethod ig/init-key :config [_ _]
  (read-config))
