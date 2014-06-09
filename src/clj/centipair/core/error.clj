(ns centipair.core.error
  (:use centipair.core.utilities.appresponse))

(defn log-error [error]
  (println "error log")
  (println error))

(defmacro try-catch [fn-name args & body]
  `(defn ~fn-name ~args
     (try
       ~@body
       (catch Exception e# 
         (log-error  
          (str "caught exception in : " (:name (meta #'~fn-name)) ~args " " (.getMessage e#)))
         (send-status 500 "Not working as expected.")))))

