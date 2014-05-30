(ns centipair.core.auth.user.forms
   (:use centipair.core.utilities.validators
         centipair.core.auth.user.models
         centipair.core.views.layout))


(defn email-exists? [value & message]
  (if (map-or-nil? value)
    value
    (if (nil? (select-user-email value))
      value
      (email-exists-failed message))))

(defn username-exists? [value & message]
  (if (map-or-nil? value)
    value
    (if (nil? (select-user-username value))
      value
      (username-exists-failed message))))


(defn register-form [form]
  (validate form 
            [:username required? username? username-exists?]
            [:password required?]
            [:email required? email? email-exists?]
            [:tos [required? "You have to agree"]]))


(defn login-form [form]
  (validate form
   [:username [required? "Username is required"]]
   [:password [required? "Password is required"]]))

(defn early-access-check [form]
  (validate form
   [:email required? email?]
   ))


(defn user-profile-form []
  (htmlize (angular-form "Profile" 
                [{:id "first_name" :type "text" :label "First Name"}
                 {:id "middle_name" :type "text" :label "Middle Name"}
                 {:id "last_name" :type "text" :label "Last Name"}
                 {:id "email" :type "text" :label "Email"}
                 {:id "website" :type "text" :label "Website"}
                 {:id "phone_mobile" :type "text" :label "Phone (Mobile)"}
                 {:id "phone_fixed" :type "text" :label "Phone (Fixed Line)"}
                 {:id "address_line1" :type "text" :label "Address Line 1"}
                 {:id "street" :type "text" :label "Street"}
                 {:id "city" :type "text" :label "City"}
                 {:id "state" :type "text" :label "State"}
                 {:id "country" :type "text" :label "Country"}
                 {:id "birth_day" :type "text" :label "Birth Day"}
                 {:id "birth_month" :type "text" :label "Birth Month"}
                 {:id "birth_year" :type "text" :label "Birth Year"}
                 {:id "gender" :type "text" :label "Gender"}
                 {:id "chat_channel" :type "text" :label "Chat Service"}
                 {:id "chat_id" :type "text" :label "Chat Id"}
                 ]
                "/admin/profile/save"
                )))

(defn password-reset-form []
  (htmlize (angular-form "Password Reset" 
                [{:id "email" :type "text" :label "Enter your email"}]
                "/admin/password/reset")))

(defn password-reset-check [form]
  (validate 
   [:email (-> (:email form) (required? "Please enter registered email") email? )]))


(defn password-change-form []
  (htmlize (angular-form "Password Change" 
                [{:id "current_password" :type "text" :label "Current Password"}
                 {:id "new_password" :type "text" :label "New Password"}]
                "/admin/password/reset")))
