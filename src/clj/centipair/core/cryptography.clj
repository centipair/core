(ns centipair.core.cryptography
  (:require
   [noir.util.crypt :as crypt]
   [crypto.random :as random]))

(defn random-base64 [length]
  (random/base64 length))

(defn rsa-decrypt [secret-key value])

(defn encrypt-password [raw-string] 
  (crypt/encrypt raw-string))

(defn check-password [raw-string encrypted-string]
  (crypt/compare raw-string encrypted-string))

(defn uuid [] (java.util.UUID/randomUUID))

(defn str-uuid [id]  (java.util.UUID/fromString id))
