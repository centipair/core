(ns centipair.core.db.connection
  (:require [clojurewerkz.cassaforte.client :as client])
  (:use clojurewerkz.cassaforte.cql))



;; Will connect to localhost
(client/connect! ["127.0.0.1"])
(use-keyspace "core")
