(ns centipair.core.api.routes
  (:use compojure.core
        noir.util.route)
  (:require [liberator.core :refer [resource defresource]]
            [noir.response :as response]
            [clojure.data.json :as json]))


(defresource home [request]
  :authorized? (fn [_] (println "authorized reques")(println request) true)
  :available-media-types ["text/html"]
  :handle-ok (fn [_] (println "printing request......")(println request)(format "This is core api home")))

(defresource api-register [request]
  :allowed-methods [:post :get]
  :available-media-types ["application/json"]
  :handle-created (response/json {:message "response post"})
  :post! (fn [ctx]  (println ctx)(println "post handled"))
  :get! (fn [ctx] (println "tutururu"))
  )
(defresource api-login []
  :authorized? (fn [_] false)
  )

(defresource api-list-test [request]
    :available-media-types ["application/json"]
    :handle-ok (fn [_] (println "tutururudsfsdfsd") (json/json-str {:message "responsse ok"}))
  )


(def-restricted-routes api-routes
  (GET "/api/home" request (home request))
  (ANY "/api/register" request (api-register request))
  (GET "/api/list" request (api-list-test request)))
