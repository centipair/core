(ns centipair.core.feed.routes
  (:use compojure.core
        noir.util.route
        centipair.core.feed.models
        centipair.core.utilities.forms
        centipair.core.utilities.appresponse
        centipair.core.feed.forms)
  (:require [centipair.core.views.layout :as layout]
            [noir.io :as io]))

(defn feed []
  (layout/render "feed.html" {:post_id (new-feed-id)}))

(defn handle-upload [file]
  (io/upload-file  (str (io/resource-path) "uploads") file)
  "Uploaded")

(defn feed-post [request]
  (send-response (valid-form? feed-form (to-data request) save-feed)))


(defn feed-data [] "This is feed data")

(def-restricted-routes feed-routes
  (GET "/feed" [] (feed))
  (GET "/feed/data" [] (feed-data))
  (POST "/feed/upload" [file] (handle-upload file))
  (POST "/feed/post" request (feed-post request)))
