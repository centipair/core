(ns centipair.core.cms.forms
  (:use centipair.core.utilities.validators
        centipair.core.cms.models)
    (:require [centipair.core.views.layout :as layout])
  )

(defn domain-exists? [value & message]
  (if (map-or-nil? value)
    value
    (if (nil? (get-domain value))
      value
      (domain-exists-failed message))))


(defn site-form [form]
  (validate 
   [:site_name (-> (:site_name form) required?)]
   [:domain_name (-> (:domain_name form) required? domain-exists?)]
   ))

(defn site-list-form []
  (layout/angular-list-form "Sites" 
                            [{:name "Name" :data "site_name"}
                             {:name "Domain Name" :data "domain_name"}]
                            [{:id "site_name" :type "text" :label "Site Name"}
                             {:id "domain_name" :type "text" :label "Domain Name"}]
                            "/admin/site/save"))
