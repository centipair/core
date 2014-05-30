(ns centipair.core.cms.access
  (:use centipair.core.cms.models))

(defn site-admin-access [request]
  (println "checking admin")
  (is-admin?))
