(ns centipair.core.api.models
  (:use 
   centipair.core.db.connection
   centipair.core.auth.user.models
   centipair.core.auth.session
   centipair.core.utilities.time
   clojurewerkz.cassaforte.cql
   clojurewerkz.cassaforte.query
   clojurewerkz.cassaforte.uuids)
  (:require 
   [centipair.core.cryptography :as crypto]))


(def api-app-key-table :api_app_key)

(defn create-app [platform version]
  (insert api-app-key-table {:app_key (crypto/random-base64 32) 
                             :platform platform 
                             :version version}))

(defn create-auth-token [user-profile]
  (let [auth_token (generate-session-id)
        session-map {:auth_token auth_token
                     :session_expire_time (set-time-expiry 23)
                     :user_id (:user_id user-profile)}]
    
    (insert-user-session session-map)  
    {:status-code 200 :message "login success" :auth_token auth_token}
    ))


(defn select-app-key [app-key]
  (first (select api-app-key-table (where :app_key app-key))))
