(ns user
  ^{:clojure.tools.namespace.repl/unload false}
  (:require [service-one.main :as main]
            [integrant.core :as ig]
            [taoensso.timbre :as log]))


(defonce sys (atom nil))


(defn stop []
  (when @sys
    (ig/halt! @sys)
    (reset! sys nil)
    (log/info "stopped.")))


(defn start
  ([] (start main/system-config))
  ([sys-config]
   (stop)
   (reset! sys (ig/init sys-config))
   (log/info (str "started " @sys))
   @sys))
