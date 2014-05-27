(ns centipair.core.auth.user.models
    (:use 
     centipair.core.db.connection
     centipair.core.auth.session
     centipair.core.utilities.time
     centipair.core.utilities.mail
     clojurewerkz.cassaforte.cql
     clojurewerkz.cassaforte.query
     clojurewerkz.cassaforte.uuids
     [centipair.core.utilities.validators :only [is-username? is-email-proxy?]])
    (:require 
     [centipair.core.cryptography :as crypto]))


(def early-access-table :early_access)
(def user-account-table :user_account)
(def user-login-username-table :user_login_username)
(def user-login-email-table :user_login_email)
(def user-session-table :user_session)
(def user-session-index-table :user_session_index)
(def user-account-registration-table :user_account_registration)

(def login-error {:status-code 422 :errors {:__all__ "Username or password incorrect"}})
(def inactive-user-error {:status-code 422 :errors {:__all__ "This account is inactive. Please activate your account."}})


(defn insert-user-session [session-map]
  (do
    (insert user-session-table session-map)
    (insert user-session-index-table {:user_id (:user_id session-map)
                                      :auth_token (:auth_token session-map)})))


(defn create-user-session [user-account]
  (let [auth_token (generate-session-id)
        session-map {:auth_token auth_token
                     :session_expire_time (set-time-expiry 23)
                     :user_id (:user_id user-account)}]
    (do
      (insert-user-session session-map)
      (set-cookies :auth_token auth_token)
      {:status-code 200 :message "login success" :redirect "/admin/"})))

(defn get-user-session []
  (let [auth_token (get-cookies :auth_token)]
    (if (nil? auth_token)
      nil
      (first (select user-session-table (where :auth_token auth_token))))))


(defn delete-session-index [user_session]
  (delete user-session-index-table (where 
                                    :user_id (:user_id user_session) 
                                    :auth_token (:auth_token user_session))))

(defn delete-session [user_session]
  (do
    (delete user-session-table (where :auth_token (:auth_token user_session)))
    (delete-session-index user_session)))

(defn destroy-session []
  (let [user_session (get-user-session)]
    (if (nil? user_session)
      nil
      (delete-session user_session))))

(defn select-user-account [user_id]
  (select user-account-table (where :user_id user_id)))


(defn insert-user-login-username [user-map]
  (insert user-login-username-table {:user_id (:user_id user-map)
                                     :username (:username user-map)}))

(defn insert-user-login-email [user-map]
  (insert user-login-email-table {:user_id (:user_id user-map)
                                  :email (:email user-map)}))

(defn insert-user-account [user-map]
  (let [user_id (time-based)]
    (insert user-account-table {:user_id user_id
                                :username (:username user-map)
                                :email (:email user-map)
                                :first_name (:first_name user-map)
                                :last_name (:last_name user-map)
                                :active (:active user-map)
                                :password (crypto/encrypt-password (:password user-map))})
    user_id))

(defn insert-registration-request [user-map]
  (insert user-account-registration-table {:registration_key (:registration_key user-map)
                                           :user_id (:user_id user-map)})
  )


(defn register-user [user-map]
  (let [user_id (insert-user-account (conj user-map {:active false}))
        user-login-map {:user_id user_id
                        :email (:email user-map)
                        :username (:username user-map)
                        :registration_key (time-based)}]
    (do 
      (insert-user-login-username user-login-map)
      (insert-user-login-email user-login-map)
      (insert-registration-request user-login-map)
      (future (send-registration-email user-login-map))
      {:status-code 200 :message "registration success"})))


(defn select-user-username [username]
  (first (select user-login-username-table (where :username username))))


(defn select-user-email [email]
  (first (select user-login-email-table (where :email email))))

(defn get-user-login [username]
  (if (is-username? username)
    (select-user-username username)
    (if (is-email-proxy? username)
      (select-user-email username)
      nil)))

(defn get-user-account [user-login]
  (if (nil? user-login)
    nil
    (first (select-user-account (:user_id user-login)))))

(defn valid-user-password? [user-account form]
    (crypto/check-password (:password form) (:password user-account)))


(defn login-bkp [form]
  (let [user-login (get-user-login (:username form))
        user-account (get-user-account user-login)]
    (if (or (nil? user-login) (nil? user-account))
      login-error
      
      (if (valid-user-password? user-account form)
        (create-user-session user-account)
        login-error))))

(defn login [form]
  (let [user-login (get-user-login (:username form))
        user-account (get-user-account user-login)]
    (if (or (nil? user-login) (nil? user-account))
      login-error
      (if (:active user-account)
        (if (valid-user-password? user-account form)
          (create-user-session user-account)
          login-error)
        inactive-user-error))))


(defn activate-user-account [user-map]
  (update user-account-table {:active true} (where :user_id (:user_id user-map))))

(defn get-registration-key [registration-key]
  (first (select user-account-registration-table (where :registration_key registration-key))))

(defn delete-activation-key [user-map]
  (delete user-account-registration-table (where :registration_key (:registration_key user-map))))

(defn activate-account [registration-key]
  (let [registration-request (get-registration-key registration-key)]
    (if (nil? registration-request)
      false
      (do 
        (activate-user-account registration-request)
        (delete-activation-key registration-request)
        true))))

(defn insert-early-access-email [form]
  (insert early-access-table {:email (:email form)})
  {:status-code 200 :message "email saved"}
  )
