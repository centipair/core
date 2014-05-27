(ns centipair.core.feed.models
  (:use 
     centipair.core.db.connection
     centipair.core.auth.session
     centipair.core.utilities.time
     centipair.core.utilities.mail
     clojurewerkz.cassaforte.cql
     clojurewerkz.cassaforte.query
     clojurewerkz.cassaforte.uuids
     ))

(def box-table :box)
(def box-handle-table :box_handle)
(def feed-table :feed)
(def feed-image-tbale :feed_image)

