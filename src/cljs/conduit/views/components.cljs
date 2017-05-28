(ns conduit.views.components)

(defn large-textarea
  [attrs]
  [:fieldset.form-group
   [:textarea.form-control.form-control-lg (merge {:rows 8} attrs)]])

(defn textarea
  [attrs]
  [:fieldset.form-group
   [:textarea.form-control (merge {:rows 8} attrs)]])

(defn large-input
  [attrs]
  [:fieldset.form-group
   [:input.form-control.form-control-lg (merge {:type :text} attrs)]])

(defn input
  [attrs]
  [:fieldset.form-group
   [:input.form-control (merge {:type :text} attrs)]])

(defn error-list
  [errors]
  (when errors
    [:ul.error-messages
     (map (fn [[k v]] [:li {:key k} (str (name k) " " (first v))]) errors)]))