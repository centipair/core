(ns centipair.core.store.routes
  (:use compojure.core
        noir.util.route
        centipair.core.store.models
        centipair.core.utilities.forms
        centipair.core.utilities.appresponse
        centipair.core.store.forms)
  (:require [centipair.core.views.layout :as layout])
  )



(defn store-home []
  (layout/render "store/home.html"))

(defroutes store-routes 
  (GET "/store" [] (store-home))
  )
