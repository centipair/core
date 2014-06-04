(defproject core "0.1.0-SNAPSHOT"
  :description "Distributed Social Network"
  :url "http://wemebox.com"
  :source-paths ["src/clj"]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [lib-noir "0.8.3"]
                 [ring-server "0.3.1"]
                 [selmer "0.6.6"]
                 [com.taoensso/timbre "3.2.1"]
                 [com.taoensso/tower "2.0.2"]
                 [markdown-clj "0.9.44"]
                 [environ "0.5.0"]
                 [clojurewerkz/cassaforte "1.3.0"]
                 [korma "0.3.1"]
                 [org.clojure/java.jdbc "0.3.3"]
                 [org.immutant/immutant "1.1.1"]
                 [com.draines/postal "1.11.1"]
                 [clj-time "0.7.0"]
                 [liberator "0.11.0"]
                 [crypto-random "1.2.0"]
                 [ring/ring-json "0.3.1"]
                 [cheshire "5.3.1"]
                 [hiccup "1.0.5"]
                 [clj-http "0.9.1"]]

  :repl-options {:init-ns centipair.core.repl
                 :init (centipair.core.repl/start-server)
                 :timeout 120000}
  :plugins [[lein-ring "0.8.10"]
            [lein-environ "0.5.0"]]
  :ring {:handler centipair.core.handler/app
         :init    centipair.core.handler/init
         :destroy centipair.core.handler/destroy}
  :profiles
  {:uberjar {:aot :all}
   :production {:ring {:open-browser? false
                       :stacktraces?  false
                       :auto-reload?  false}}
   :dev {:dependencies [[ring-mock "0.1.5"]
                        [ring/ring-devel "1.2.2"]]
         :env {:dev true}}}
  :immutant {:context-path "/"
             :nrepl-port 12345
             :virtual-host "wemebox.com"
             }
  :min-lein-version "2.0.0")
