(ns centipair.core.utilities.appresponse
  (:require [noir.response :as response]
            [noir.cookies :as cookies]
            [cheshire.core :refer :all]))

(def response-codes {:forbidden 403
                     :server-error 500
                     :redirect 301
                     :form-error 422
                     :not-found 404
                     :ok 200})

(defn response-code [code]
  (code response-codes))

(defn send-response [response-map]
  (response/status (:status-code response-map) (generate-string response-map)))

(defn send-status [code content]
  (response/status code content))


