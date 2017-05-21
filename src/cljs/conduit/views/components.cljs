(ns conduit.views.components)

(defn article-meta
  [{date  :date
    likes :likes
    favorites-count :favoritesCount
    {username :username image :image} :author}]
  [:div.article-meta
   [:a {:href (str "#/profile/" username)} [:img {:src image}]]
   [:div.info
    [:a.author {:href (str "#/profile/" username)} username]
    [:span.date date]]
   [:button.btn.btn-outline-primary.btn-sm.pull-xs-right
    [:i.ion-heart] (str " " favorites-count) likes]])

(defn article-preview
  [{:keys [slug title description tag-list author] :as article}]
  [:div.article-preview
   {:key slug}
   [article-meta article]
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

(defn error-list
  [errors]
  (when errors
    [:ul.error-messages
     (map (fn [[k v]] [:li {:key k} (str (name k) " " (first v))]) errors)]))