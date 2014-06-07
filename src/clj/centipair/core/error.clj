(ns centipair.core.error)

(defmacro try-catch [fn-name args & body]
  `(defn ~fn-name ~args
     (try
       ~@body
       (catch Exception e#  (str "caught exception in : " (:name (meta #'~fn-name)) ~args " " (.getMessage e#))))))
