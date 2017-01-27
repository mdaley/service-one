(ns service-one.handler
  (:require [compojure
             [core :refer :all]
             [route :as route]]
            [service-one.business-errors :refer :all]
            [integrant.core :as ig]
            [ring.middleware
             [defaults :refer [site-defaults wrap-defaults]]
             [json :refer [wrap-json-response
                           wrap-json-body]]]
            [ring.util.response :as resp]
            [taoensso.timbre :as log]
            [service-one.config :as config])
  (:import (org.slf4j MDC)))

(defn- healthcheck []
  (str "version " (config/version "service-one")))

(defn ->app-routes
  []
  (defroutes app-routes
    (routes
     (GET "/ping" [] "pong")
     (GET "/healthcheck" [] (healthcheck)))
    (route/not-found "Not Found")))

(defn wrap-exception-handling
  [handler]
  (fn [req]
    (try
      (handler req)
      (catch Exception e
        (let [error-id (str (java.util.UUID/randomUUID))]
          (MDC/put "error-id" error-id)
          (log/error e (str "unexpected error " error-id))
          {:status 500
           :body {:msg "An unexpected error occurred"
                  :errorId error-id}}))
      (finally
        (MDC/clear)))))

(defn ->app []
  (-> (->app-routes)
      (wrap-json-body {:keywords? true
                       :bigdecimals? true
                       :malformed-response {:status 400
                                            :headers {"Content-Type" "application/json"}
                                            :body {:error "Invalid JSON"}}})
      wrap-exception-handling
      wrap-json-response
      (wrap-defaults (update-in site-defaults
                                [:security :anti-forgery]
                                (constantly false)))))

(defmethod ig/init-key :app [_ sys]
  (->app))
