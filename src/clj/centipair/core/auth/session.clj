(ns centipair.core.auth.session
  (:require
   [centipair.core.cryptography :as cryptography]
   [centipair.core.utilities.time :as time]
   [noir.session :as session]
   [noir.cookies :as cookies]))

(defn generate-session-id [] (cryptography/random-base64 32))

(defn set-session [key value]
  (session/put! key value))

(defn get-session [key]
  (session/get key))

(defn set-secure-cookies [k v]
  (cookies/put! k {:value v,  :secure "Secure", :http-only "HttpOnly", :path "/"}))

(defn get-cookies [k]
  (cookies/get k))

(defn set-cookies [k v]
  (cookies/put! k {:value v, :http-only "HttpOnly" :path "/" :expires (time/cookie-expire-time 100)}))

(defn delete-cookies [k]
  (cookies/put! k {:value "", :http-only "HttpOnly" :path "/", :expires -1}))

;;Session id is set as auth_token
(defn set-user-session [user_id, session_id])


(defn generate-csrf-token [] (cryptography/random-base64 32))

(defn set-csrf-token [] 
  (let [csrf-token (generate-csrf-token)]
  (:csrfmiddlewaretoken (set-session :csrfmiddlewaretoken csrf-token))))

(defn get-csrf-token [] 
  (let [csrf-token (get-session :csrfmiddlewaretoken)]
    (if (nil? csrf-token)
      (set-csrf-token)
      csrf-token)))


(defn authenticated? 
  "Check for auth_token in 
   get and cookies depending
   on web or mobile api"
  [request])
