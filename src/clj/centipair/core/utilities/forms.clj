(ns centipair.core.utilities.forms
  (:use centipair.core.auth.session
        centipair.core.utilities.appresponse
        ))

(defn valid-csrf? [form]
  (if (nil? (:csrfmiddlewaretoken form))
    false
    (if (= (get-session :csrfmiddlewaretoken) (:csrfmiddlewaretoken form))
      true
      false)))

;;TODO: CSRF disabled now for testing

(defn validate-form [check form proceed]
  (let [result (check form)]
    (if (:valid result)
      ;;valid form
      (proceed form)
      {:status-code (response-code :form-error)
       :message "Invalid data submitted"
       :errors (:errors result)})))

(defn valid-form? [check form proceed]
  (if (valid-csrf? form)
    (validate-form check form proceed)
    {:status-code (response-code :forbidden) :message "CSRF: Invalid Request"}))


(defn to-data [request]
  (let [form (:params request)]
    form
    ))
