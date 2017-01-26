(ns service-one.server
  (:require [integrant.core :as ig]
            [ring.adapter.jetty :as jetty]))

(defmethod ig/init-key :server
  [_ {:keys [handler config]}]
  (jetty/run-jetty handler
                   (-> (:webserver config)
                       (assoc :join? false))))

(defmethod ig/halt-key! :server [_ server]
  (.stop server))
