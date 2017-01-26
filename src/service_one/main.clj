(ns service-one.main
  (:gen-class)
  (:require [service-one
             [handler :as handler]
             [server :as server]
             [config :as config]]
            [integrant.core :as ig]))

(def system-config
  {:server {:config (ig/ref :config)
            :handler (ig/ref :app)}
   :app {}
   :config nil})

(defn -main [& args]
  (ig/init system-config))
