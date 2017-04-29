(ns conduit.views.components)

(defn article-meta
  [{{:keys [username name image]} :profile
    {:keys [date likes]}          :article}]
  [:div.article-meta
   [:a {:href (str "#/profile/" username)} [:img {:src image}]]
   [:div.info
    [:a.author {:href (str "#/profile/" username)} name]
    [:span.date date]]
   [:button.btn.btn-outline-primary.btn-sm.pull-xs-right
    [:i.ion-heart] " " likes]])

(defn article-preview
  [{profile                                               :profile
    {:keys [slug title description tag-list] :as article} :article}]
  [:div.article-preview
   [article-meta {:article article :profile profile}]
   [:a.preview-link {:href (str "#/article/" slug)}
    [:h1 title]
    [:p description]
    [:span "Read more..."]
    [:ul.tag-list
     (map #(do [:li.tag-default.tag-pill.tag-outline %]) tag-list)]]])

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