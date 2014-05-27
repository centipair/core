(ns centipair.core.cms.models
  (:use 
     centipair.core.db.connection
     centipair.core.auth.session
     centipair.core.auth.user.models
     centipair.core.utilities.time
     clojurewerkz.cassaforte.cql
     clojurewerkz.cassaforte.query
     clojurewerkz.cassaforte.uuids))

(def site-table :site)
(def site-domain-table :site_domain)
(def site-admin-table :site_admin)
(def page-table :page)


(defn get-domain [domain_name]
  (first (select site-domain-table (where :domain_name domain_name))))

(defn create-site-domain [site-map]
  (insert site-domain-table {:domain_name (:domain_name site-map)
                             :site_id (:site_id site-map)}
  ))

(defn create-site-admin [site-map]
  (let [user-session (get-user-session)]
  (insert site-admin-table {:user_id (:user_id user-session)
                            :site_id (:site_id site-map)})))


(defn extract-site [map]
  (get map :site_id))

(defn get-user-sites [user-session]
  (select site-admin-table (where :user_id (:user_id user-session))))

(defn get-my-sites [] 
  (let [user-session (get-user-session)
        user-sites (get-user-sites user-session)
        user-site-ids (into [] (map extract-site user-sites))]
    (select site-table (where :site_id [:in user-site-ids]))
    ))


(defn is-site-admin? [site-id]
  (let [user-session (get-user-session)]
   (if (empty? user-session)
     false
     (if (empty? (select site-admin-table (where :user_id (:user_id user-session) :site_id site-id)))
       false
       true))))

(defn is-admin? []
  (let [user-session (get-user-session)]
    (if (nil? user-session)
      false
      true)))
      ;;(if (empty? (get-user-sites user-session))
      ;;  false
       ;; true))))

(defn create-site [site]
  (let [site-map (merge site {:site_id (random)})]
    (do
      (insert site-table site-map)
      (create-site-domain site-map)
      (create-site-admin site-map)
    site-map)))
