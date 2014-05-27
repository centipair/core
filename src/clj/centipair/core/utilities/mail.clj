(ns centipair.core.utilities.mail
  (:use postal.core
        centipair.core.settings
        centipair.core.secret
        selmer.parser))

(defn send-registration-email [registration-request]
  (let [email-body (render-file "centipair/core/views/templates/email/registration.html" 
                                (merge site {:registration-key (str (:registration_key registration-request))}))]
    (send-message email-settings
                  {:from (:site_email site)
                   :to (:email registration-request)
                   :subject (str "Please activate your " (:site_name site) " account")
                   :body [{:type "text/html"
                           :content email-body}]}
                  )))
