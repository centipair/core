(ns centipair.core.auth.user.access
  (:use centipair.core.auth.user.models)
  )

(defn logged-in? [request]
  (println "checking")
  (let [user-session (get-user-session)]
    (if (nil? user-session)
      false
      true)))
