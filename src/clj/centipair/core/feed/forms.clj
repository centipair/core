(ns centipair.core.feed.forms
  (:use centipair.core.utilities.validators
        centipair.core.auth.user.models
        centipair.core.feed.models))

(defn feed-form [form]
  (validate form [:content [required? "Please add some content"]]))
