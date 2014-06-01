(ns centipair.core.utilities.validators
  (:use noir.validation)
  (:require [noir.cookies :as cookies]
            [noir.session :as session]))


(defn form-errors [errors] 
  {:code "form_error" :errors errors})

(defn is-url? [value] 
  (re-matches #"^(https?:\/\/)?([\da-z\.-]+)\.([a-z\.]{2,6})([\/\w \.-]*)*\/?$" value))

(defn is-nil? [value] 
  (if (or (= nil value) (= "" value)) 
    true false))

(defn map-or-nil? [value] 
  (if (or (is-nil? value) (map? value)) 
    true false))

(defn failed-message [default-message message]
  (if (= nil message) 
    {:message default-message } {:message (first message)} ))

(defn required-failed [message]
  (failed-message "This field is required" message))

(defn email-failed [message]
  (failed-message "Enter a valid email" message))

(defn username-failed [message]
  (failed-message "This is an invalid username" message))

(defn greater-than-zero-failed [message]
  (failed-message "Enter a number greater than zero" message))

(defn unique-data-failed [message] 
  (failed-message "This value already exists please enter another" [message]))

(defn data-exists-failed [message] 
  (failed-message "This value does not exist" [message]))

(defn url-failed [message]
  (failed-message "This does not seem like a valid url" message))

(defn csrf-failed [message]
  (failed-message "This is not a valid post request" message))

(defn user-key-failed [message]
  (failed-message "This key might have expired or not valid" message))

(defn url? [value & message] (if (map-or-nil? value)
                               value
                               (if (empty? (is-url? value)) (url-failed message) value) )
  )

(defn username-exists-failed [message]
  (failed-message "This username already exists. Please choose another." message))

(defn email-exists-failed [message]
  (failed-message "This email already exists. Please choose another." message))

(defn email-should-exist-failed [message]
  (failed-message "This email doesn't exist in our system." message))

(defn domain-exists-failed [message]
  (failed-message "This domain already exists. Please choose another." message))

(defn app-key-failed [message]
  (failed-message "App Key invalid or does not exist" message))


(defn clean-required [value] 
  (if (is-nil? value)
    value
    (clojure.string/replace value #" " "")))

(defn required? [value & message]
  (let [check-value (clean-required value)]
  (if (is-nil? check-value) 
    (required-failed message) 
    value)))

(defn is-email-proxy? [v]
  (is-email? v)) 

(defn email? [value & message]
  (if (map-or-nil? value) 
    value 
    (if (is-email? value) value (email-failed message))))


(defn is-username?
  "Returns true if v is a valid username"
  [v]
  (if (nil? v)
    false
    (matches-regex? v #"^(?=.{3,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$")))



(defn username? [value & message]
  (if (map-or-nil? value)
    value
    (if (is-username? value) value (username-failed message))))

(defn greater-than-zero? [value & message] 
  (if (map-or-nil? value) value (if (<= value 0) 
                                  (greater-than-zero-failed message) 
                                  value)))

(defn csrf? [value & message] 
  (if (or (nil? value) (not= (session/get :csrftoken) value )) (csrf-failed message)))

(defn collect-error-old [value] 
  (if (map? (second value)) 
    {(keyword (first value)) (:message (second value))}))

(defn validate-all [list-values] 
  (filter (complement nil?) (map collect-error-old list-values)))

(defn validate-old [& rules]
  (reduce merge (into [] (validate-all (into [] rules)))))


(defn custom-email? [message value])



;; (validate [:email (-> "" required? email? )] )


;;desired form
;;(validate form 
;;          [:username required? email?] 
;;          [:email required? [email? "Please provide a valid email"]])


(defn combine-rule [form rule]
  (conj rule form))


(defn reduce-validator [value check]
  (if (map? value)
    value
    (if (= (type check) (type []))
      ((first check) value (second check))
      (check value))))

(defn validator [rule]
  (let [form-key (first rule)
        value (form-key (last rule))
        value-rules (into [] (rest (pop rule)))
        result (reduce reduce-validator value value-rules)]
    (if (map? result)
      {form-key (:message result)}
      result)))

(defn collect-error [errors]
  (reduce merge (filter map? (into [] errors))))


(defn validate [form & rules]
  (let [errors (collect-error (map validator (map (partial combine-rule form) rules)))]
    {:valid (empty? errors)
     :errors errors}))
