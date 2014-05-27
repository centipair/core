(ns centipair.core.api.forms
  (:use centipair.core.utilities.validators
         centipair.core.auth.user.forms
         centipair.core.api.models))


(defn valid-app-key? [value & message]
  (if (map-or-nil? value)
    value
    (if (nil? (select-app-key value))
      (app-key-failed message)
      value)))

(defn api-request-token-form [form]
  (validate
   [:device_id (-> (:device_id form) (required?))]
   [:app_key (-> (:app_key form) (required? "App key invalid or missing") valid-app-key?)]))
 

(defn api-login-form [form]
  (validate
   [:username (-> (:username form) (required? "Username is required"))]
   [:password (-> (:password form) (required? "Password is required"))]
   [:app_key (-> (:app_key form) (required? "App key invalid or missing"))]))
