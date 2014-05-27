(ns centipair.core.handler
  (:use [ring.middleware.json :only [wrap-json-params]])
       	;;centipair.core.cms.access)
  (:require [compojure.core :refer [defroutes]]
            [centipair.core.routes.home :refer [home-routes]]
            [centipair.core.api.routes :refer [api-routes]]
            [centipair.core.api.admin :refer [api-admin-routes]]
            [centipair.core.routes.admin :refer [admin-routes]]
            [centipair.core.middleware :as middleware]
            [noir.util.middleware :refer [app-handler]]
            [compojure.route :as route]
            [taoensso.timbre :as timbre]
            [taoensso.timbre.appenders.rotor :as rotor]
            [selmer.parser :as parser]
            [environ.core :refer [env]]))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server such as Tomcat
   put any initialization code here"
  []
  (timbre/set-config!
    [:appenders :rotor]
    {:min-level :info
     :enabled? true
     :async? false ; should be always false for rotor
     :max-message-per-msecs nil
     :fn rotor/appender-fn})

  (timbre/set-config!
    [:shared-appender-config :rotor]
    {:path "core.log" :max-size (* 512 1024) :backlog 10})

  (if (env :dev) (parser/cache-off!))
  (timbre/info "core started successfully"))

(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  (timbre/info "core is shutting down..."))

(defn allow-cross-origin
  "middleware function to allow crosss origin"
  [handler]
  (fn [request]
    (let [response (handler request)]
      (assoc-in response [:headers "Access-Control-Allow-Origin"]
                "*"))))


(def app (app-handler
           ;; add your application routes here
           [api-admin-routes admin-routes home-routes app-routes]
           ;; add custom middleware here
           :middleware []
           ;; add access rules here
           :access-rules [];;[{:uris ["/admin/*" ""]
                         ;;  :rule site-admin-access
                         ;;  :redirect "/admin-access-denied"}]
           ;; serialize/deserialize the following data formats
           ;; available formats:
           ;; :json :json-kw :yaml :yaml-kw :edn :yaml-in-html
           :formats [:json-kw :edn]))
