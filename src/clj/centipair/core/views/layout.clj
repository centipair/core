(ns centipair.core.views.layout
  (:use hiccup.core)
  (:require [selmer.parser :as parser]
            [clojure.string :as s]
            [ring.util.response :refer [content-type response]]
            [compojure.response :refer [Renderable]]
            [centipair.core.auth.session :as session]))

(def template-path "centipair/core/views/templates/")

(deftype RenderableTemplate [template params]
  Renderable
  (render [this request]
    (content-type
      (->> (assoc params
                  (keyword (s/replace template #".html" "-selected")) "active"
                  :servlet-context (:context request)
                  :csrfmiddlewaretoken (session/get-csrf-token)
                  :base "centipair/core/views/templates/base.html")
        (parser/render-file (str template-path template))
        response)
      "text/html; charset=utf-8")))

(defn render [template & [params]]
  (RenderableTemplate. template params))


(defn placeholder [input]
  (if (contains? input :placeholder)
    (:placeholder input)
    ""
    )
  )

(defn angular-input [input]
  [:div {:class (str "formGroup " "[{errors." (:id input) "Class}]")}
   [:label {:class "control-label" :for (:id input)} (:label input)]
   [:input {:type (:type input) :class "form-control" :id (:id input) :placeholder (placeholder input) :ng-model (str "form." (:id input))}]
   [:label {:class "control-label" :for (:id input)} (str "[{errors." (:id input) "Class}]")]])

(defn form-inputs [inputs url]
  (conj (reduce conj [:form {:role "form" :name "form"}] (map angular-input inputs)) 
        [:button {:type "submit" 
                  :class "btn btn-default btn-primary" 
                  :url url :submit ""} "Submit"])
  )

(defn angular-form [title inputs submit-url]
  [:div 
   [:div {:ng-show "allErrorss" :class "form-group has-error"}
    [:label {:class "control-label"} "[{errors.__all__}]"]]
   (form-inputs inputs submit-url)])

(defn list-header [field]
  [:th (:name field)]
  )

(defn list-data [field]
  [:td (str "[{data." (:data field) "}]")])

(defn list-fields-header [list-fields]
  (reduce conj [:tr] (map list-header list-fields )))

(defn list-fields-data [list-fields]
  (reduce conj [:tr {:ng-repeat "data in listData" :class "data-row" :ng-click "editData(data)"}] (map list-data list-fields ))
  )

(defn angular-list-form [title list-fields inputs submit-url]
  (html [:div 
   [:table {:class "table table hover"}
    (list-fields-header list-fields)
    (list-fields-data list-fields)
    ]
   (angular-form title inputs submit-url)]))

(defn htmlize [form]
  (html form))
