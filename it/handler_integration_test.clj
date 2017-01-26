(ns handler-integration-test
  (:require [clojure.test :refer :all]
            [service-one.main :refer [-main]]
            [integrant.core :as ig]
            [clj-http.client :as http]))

(defn start-service
  [f]
  (let [system (-main)]
    (try
      (f)
      (finally
        (ig/halt! system)))))

(use-fixtures :once start-service)

(deftest ^:integration handler

  (testing "ping"
    (let [resp (http/get "http://localhost:8080/ping" {:as :string})]
      (are [expected actual] (= expected actual)
        200 (:status resp)
        "pong" (:body resp))))

  (testing "not-found"
    (let [resp (http/get "http://localhost:8080/not-found" {:throw-exceptions false
                                                            :as :string})]
      (are [expected actual] (= expected actual)
        404 (:status resp)
        "Not Found" (:body resp)))))
