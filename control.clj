(defcluster :default-cluster
  :clients [
    {:host "wemebox.com" :user "devasia"}
  ]
  :ssh-options "-p 827" )

(def production-deploy-path "/home/devasia/webapps")
(def production-code-path (str production-deploy-path "/core"))
(def production-source-repo "https://github.com/centipair/core")


(def restart-app-server "sudo /etc/init.d/jboss-as-standalone restart")


(deftask :deploy 
  "Deploys code to server from git repo"
  []
  (ssh 
   (run 
    (cd production-code-path 
        (run "git reset --hard")
        (run "git pull origin master")))))
