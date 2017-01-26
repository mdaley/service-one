(ns service-one.handler-test
  (:require [clojure.test :refer :all]
            [service-one
             [utils :refer :all]]
            [integrant.core :as ig]
            [ring.mock.request :as mock]))

(def sys-config {:app {}})

(deftest test-app
  (testing "ping"
    (let [response (with-app sys-config (mock/request :get "/ping"))]
      (is (= (:status response) 200))
      (is (= (:body response) "pong"))))

  (testing "not-found route"
    (let [response (with-app sys-config (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
