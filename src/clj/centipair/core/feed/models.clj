(ns centipair.core.feed.models
  (:use 
     centipair.core.db.connection
     centipair.core.auth.user.models
     centipair.core.utilities.time
     centipair.core.utilities.mail
     clojurewerkz.cassaforte.cql
     clojurewerkz.cassaforte.query
     clojurewerkz.cassaforte.uuids
     )
  (require [clj-time.core :as t]
           [clj-time.coerce :as ct]
           [centipair.core.cryptography :as crypto]))

(def box-table :box)
(def box-handle-table :box_handle)
(def feed-table :feed)
(def feed-image-table :feed_image)

(defn new-feed-id []
  (str (time-based)))


(defn save-feed [form]
  {:status-code 200 :message "saved"})


(defn create-box [box-name]
  (let [user-account (select-user-account (session-user-id))
        user-id (:user_id user-account)
        username (:username user-account)]
    (insert box-table {:box_id (time-based)
                       :user_id user-id
                       :box_name box-name
                       :created_date (ct/to-sql-time (t/now))
                       :active true})))

(defn create-user-box [user-id]
  (let [user-account (first (select-user-account user-id))
        username (:username user-account)]
    (insert box-table {:box_id (time-based)
                       :user_id user-id
                       :box_name username
                       :created_date (ct/to-sql-time (t/now))
                       :active true})))
