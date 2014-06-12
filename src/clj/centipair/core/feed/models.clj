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
(def box-admin-table :box_admin)
(def box-connect-table :box_connect)
(def box-connect-index-table :box_connect_index)

(def feed-table :feed)
(def feed-image-table :feed_image)


(defn new-feed-id []
  (str (time-based)))


(defn save-feed [form]
  {:status-code 200 :message "saved"})





(defn create-box-handle [box-name box-id]
  (insert box-handle-table {:box_name box-name
                            :box_id box-id}))

(defn create-box-admin [user-id box-id]
  (insert box-admin-table {:user-id user-id
                           :box_id box-id}))

(defn create-box-connect [user-id box-id]
  (insert box-connect-table {:user-id user-id
                             :box_id box-id}))

(defn create-box-connect-index [user-id box-id]
  (insert box-connect-index-table {:user-id user-id
                             :box_id box-id}))

(defn create-box-tail [user-id box-id box-name]
  (do
    (create-box-handle box-name box-id)
    (create-box-admin user-id box-id)
    (create-box-connect user-id box-id)
    (create-box-connect-index user-id box-id))
  {:status-code 200 :message "box created"}
  )


(defn create-box [box-name]
  (let [user-account (select-user-account (session-user-id))
        user-id (:user_id user-account)
        username (:username user-account)
        box-id (time-based)]
    (insert box-table {:box_id box-id
                       :user_id user-id
                       :box_name box-name
                       :created_date (ct/to-sql-time (t/now))
                       :active true})
    (create-box-tail user-id box-id box-name)))


(defn create-user-box [user-id]
  (let [box-id (time-based)
        user-account (first (select-user-account user-id))
        username (:username user-account)]
    (insert box-table {:box_id box-id
                       :user_id user-id
                       :box_name username
                       :created_date (ct/to-sql-time (t/now))
                       :active true})
    (create-box-tail user-id box-id username)))


(defn delete-box [box-name]
  (let [box-id (first (select box-handle-table (where :box_name box-name)))]
    
    )
  )
