(ns centipair.core.routes.home
  (:use compojure.core
        centipair.core.auth.user.forms
        centipair.core.auth.user.models
        centipair.core.utilities.forms
        centipair.core.utilities.appresponse
        centipair.core.auth.session
        centipair.core.cryptography)
  (:require [centipair.core.views.layout :as layout]
            [noir.response :as response]
            [noir.io :as io]
            ))


(defn home-page []
  (layout/render "home.html"))

(defn about-page []
  (layout/render "about.html"))

(defn register-page []
  (layout/render "register.html"))


(defn register-submit [request]
  (send-response (valid-form? register-form (to-data request) register-user)))

(defn activate [registration-key]
  (if (activate-account (str-uuid registration-key))
    (layout/render "account-activation.html" {:title "Account activated" :message "Your account has been activated"})
    (layout/render "account-activation.html" {:title "Account activation error" :message "Invalid activation code"})))

(defn login-page []
  (layout/render "login.html"))

(defn login-submit [request]
  (send-response (valid-form? login-form (to-data request) login)))

(defn logout []
  (destroy-session)
  (response/redirect "/"))

(defn csrf []
  (send-response {:csrfmiddlewaretoken (get-csrf-token) :status-code 200}))

(defn invite [request] 
  (send-response (valid-form? early-access-check (to-data request) insert-early-access-email)))

(defn password-forgot []
  (layout/render "forgot-password.html" {:forgot_password_form (forgot-password-form)}))

(defn password-forgot-submit [request]
  (send-response (valid-form?  forgot-password-form-validation (to-data request) password-reset-email)))


(defn location []
  (layout/render "location.html"))


(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page))
  (GET "/register" [] (register-page))
  (POST "/register/submit" request (register-submit request))
  (GET "/login" [] (login-page))
  (POST "/login/submit" request (login-submit request))
  (GET "/logout" [] (logout))
  (GET "/activate/:key" [key] (activate key))
  (GET "/location" [] (location))
  (POST "/invite" request (invite request))
  (GET "/password/forgot" [] (password-forgot))
  (POST "/password/forgot/submit" request (password-forgot-submit request))
  (GET "/csrf" [] (csrf)))
