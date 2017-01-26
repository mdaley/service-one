(ns service-one.utils
  (:require [cheshire.core :as json]
            [clojure.test :as t]
            [integrant.core :as ig]))

(defn parse-body
  [{headers :headers :as response}]
  (if (re-matches #"application\/json.*" (get headers "Content-Type"))
    (update-in response [:body] #(json/parse-string % true))
    response))

(defn with-sys
  [config f]
  (let [sys (ig/init config)]
    (try
      (f sys)
      (finally
        (ig/halt! sys)))))

(defn with-app
  [config request]
  (with-sys
    config
    (fn [{app :app}]
      (parse-body (app request)))))
