(ns centipair.core.api.admin
  (:use compojure.core
        noir.util.route
        centipair.core.cms.models)
  (:require [liberator.core :refer [resource defresource]]
            [cheshire.core :refer :all]))


(defresource site-list []
  :authorized? (fn [_] (is-admin?))
  :available-media-types ["application/json"]
  :handle-ok (generate-string (get-my-sites))
  )

(defresource page-list []
  :authorized? (fn [_] (is-admin?))
  :available-media-types ["application/json"]
  :handle-ok (generate-string [])
  )

(defresource site-edit [])

(defroutes api-admin-routes 
  (GET "/api/admin/site/list" [] (site-list))
  (GET "/api/admin/page/list" [] (page-list))
  )
